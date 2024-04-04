package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.dtos.PatronDto;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.utils.UserUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn("librarian@example.com");

        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testAddPatron() {
        SignupDto signupDto = new SignupDto("John", "Doe", "patron@example.com", "USA", "password", "password", "Male", "123 Street, City", UserRole.PATRON);

        User librarian = new User();
        librarian.setRole(UserRole.LIBRARIAN);

        when(UserUtil.getLoggedInUser()).thenReturn("librarian@example.com");
        when(userRepository.findByEmailAddress("librarian@example.com")).thenReturn(Optional.of(librarian));
        when(userRepository.findByEmailAddress("patron@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        Set<ConstraintViolation<SignupDto>> violations = validator.validate(signupDto);
        assertTrue(violations.isEmpty(), "SignupDto validation failed: " + violations);

        ResponseEntity<ApiResponse<User>> response = userService.addPatron(signupDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Patron added successfully", response.getBody().getMessage());
    }



    @Test
    void testGetAllPatrons() {
        User patron = new User();
        patron.setRole(UserRole.PATRON);

        when(userRepository.findAll()).thenReturn(Collections.singletonList(patron));

        ResponseEntity<ApiResponse<List<PatronDto>>> response = userService.getAllPatrons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testGetPatronById() {
        User patron = new User();
        patron.setRole(UserRole.PATRON);

        when(userRepository.findById(1L)).thenReturn(Optional.of(patron));

        ResponseEntity<ApiResponse<PatronDto>> response = userService.getPatronById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdatePatron() {
        User librarian = new User();
        librarian.setRole(UserRole.LIBRARIAN);

        User updatedPatron = new User();
        updatedPatron.setFirstName("Updated");

        when(UserUtil.getLoggedInUser()).thenReturn("librarian@example.com");
        when(userRepository.findByEmailAddress("librarian@example.com")).thenReturn(Optional.of(librarian));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        ResponseEntity<ApiResponse<User>> response = userService.updatePatron(1L, updatedPatron);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Patron details updated successfully", response.getBody().getMessage());
    }

    @Test
    void testDeletePatron() {
        User librarian = new User();
        librarian.setRole(UserRole.LIBRARIAN);

        when(UserUtil.getLoggedInUser()).thenReturn("librarian@example.com");
        when(userRepository.findByEmailAddress("librarian@example.com")).thenReturn(Optional.of(librarian));
        when(userRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<ApiResponse<String>> response = userService.deletePatron(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Patron deleted successfully", response.getBody().getMessage());
    }
}

