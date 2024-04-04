package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.dtos.LoginDto;
import com.maidscc.libraryManagementSystem.dtos.LoginResponse;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.exception.MaidSccLibraryException;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.services.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthServices {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtImplementation jwtImplementation;
    private final PasswordEncoder passwordEncoder;
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto) {
        Authentication authenticationResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.emailAddress(), loginDto.password()));
        var user = userRepository.findByEmailAddress(loginDto.emailAddress())
                .orElseThrow(() -> new MaidSccLibraryException("User doesn't exist"));
        var jwt = jwtImplementation.generateToken(user);

        LoginResponse loginResponse = new LoginResponse(user.getFirstName(), user.getLastName(), jwt);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                loginResponse,
                "Login successful"
        );
        return new ResponseEntity<>(response, response.getStatus()
        );
    }

    public ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto, UserRole userRole) {
        var userExists = userRepository.findByEmailAddress(signupDto.emailAddress());
        if (userExists.isPresent()) {
            throw new MaidSccLibraryException("Email address already exists");
        }

        if (!signupDto.password().equals(signupDto.repeatPassword())) {
            throw new MaidSccLibraryException("Password and confirm password do not match");
        }

        User newUser = new User();
        newUser.setEmailAddress(signupDto.emailAddress());
        newUser.setPassword(passwordEncoder.encode(signupDto.password()));
        newUser.setFirstName(signupDto.firstName());
        newUser.setLastName(signupDto.lastName());
        newUser.setCountry(signupDto.country());
        newUser.setGender(signupDto.gender());
        newUser.setRole(userRole);
        newUser.setContactAddress(signupDto.contactAddress());
        newUser.setRepeatPassword(passwordEncoder.encode(signupDto.repeatPassword()));

        userRepository.save(newUser);

        ApiResponse<String> response = new ApiResponse<>("Successfully created account", HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<String>> signupPatron(SignupDto signupDto) {
        if (!UserRole.PATRON.equals(signupDto.role())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid role. Expected: PATRON", HttpStatus.BAD_REQUEST));
        }
        return signup(signupDto, UserRole.PATRON);
    }

    public ResponseEntity<ApiResponse<String>> signupLibrarian(SignupDto signupDto) {
        if (!UserRole.LIBRARIAN.equals(signupDto.role())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid role. Expected: LIBRARIAN", HttpStatus.BAD_REQUEST));
        }
        return signup(signupDto, UserRole.LIBRARIAN);
    }


}
