package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.dtos.PatronDto;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.exception.MaidSccLibraryException;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.services.UserService;
import com.maidscc.libraryManagementSystem.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<ApiResponse<User>> addPatron(SignupDto signupDto) {
        String email = UserUtil.getLoggedInUser();
        var librarian = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("Librarian not found"));

        if(!librarian.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to add a Patron");
        }

        Optional<User> existingUser = userRepository.findByEmailAddress(signupDto.emailAddress());
        if (existingUser.isPresent()) {
            throw new MaidSccLibraryException("A patron with the same email address already exists");
        }

        if (!signupDto.password().equals(signupDto.repeatPassword())) {
            throw new MaidSccLibraryException("Password and confirm password do not match");
        }

        User patron = new User();
        patron.setFirstName(signupDto.firstName());
        patron.setLastName(signupDto.lastName());
        patron.setEmailAddress(signupDto.emailAddress());
        patron.setPassword(passwordEncoder.encode(signupDto.password()));
        patron.setCountry(signupDto.country());
        patron.setGender(signupDto.gender());
        patron.setRepeatPassword(passwordEncoder.encode(signupDto.repeatPassword()));
        patron.setContactAddress(signupDto.contactAddress());
        patron.setRole(signupDto.role());

        userRepository.save(patron);

        return ResponseEntity.ok(new ApiResponse<>("Patron added successfully", HttpStatus.OK));
    }


    @Override
    public ResponseEntity<ApiResponse<List<PatronDto>>> getAllPatrons() {
        List<User> allUsers = userRepository.findAll();
        List<PatronDto> patrons = allUsers.stream()
                .filter(user -> user.getRole().equals(UserRole.PATRON))
                .map(this::convertToPatronDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>("Patrons retrieved successfully", HttpStatus.OK, patrons));
    }

    @Override
    public ResponseEntity<ApiResponse<PatronDto>> getPatronById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRole().equals(UserRole.PATRON)) {
                PatronDto patronDto = convertToPatronDto(user);
                return ResponseEntity.ok(new ApiResponse<>("Patron retrieved successfully", HttpStatus.OK, patronDto));
            } else {
                throw new MaidSccLibraryException("User is not a patron");
            }
        } else {
            throw new MaidSccLibraryException("Patron not found");
        }
    }

    private PatronDto convertToPatronDto(User user) {
        return new PatronDto(
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getCountry()
        );
    }



    @Override
    public ResponseEntity<ApiResponse<User>> updatePatron(Long id, User updatedPatron) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if(!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to update a patron");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new MaidSccLibraryException("Patron not found with id: " + id));

        existingUser.setFirstName(updatedPatron.getFirstName());
        existingUser.setLastName(updatedPatron.getLastName());
        existingUser.setEmailAddress(updatedPatron.getEmailAddress());
        existingUser.setContactAddress(updatedPatron.getContactAddress());
        existingUser.setCountry(updatedPatron.getCountry());
        existingUser.setGender(updatedPatron.getGender());


        userRepository.save(existingUser);

        return ResponseEntity.ok(new ApiResponse<>("Patron details updated successfully", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deletePatron(Long id) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if(!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to delete a patron");
        }

        if (!userRepository.existsById(id)) {
            throw new MaidSccLibraryException("Patron does not exist");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Patron deleted successfully", HttpStatus.OK));
    }

}
