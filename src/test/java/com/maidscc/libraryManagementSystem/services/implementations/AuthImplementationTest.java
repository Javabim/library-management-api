package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.dtos.LoginDto;
import com.maidscc.libraryManagementSystem.dtos.LoginResponse;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthImplementationTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtImplementation jwtImplementation;

    @InjectMocks
    private AuthImplementation authImplementation;

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn("librarian@example.com");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_WithValidCredentials_ShouldReturnSuccessResponse() {
        when(authenticationManager.authenticate(any()))
                .thenReturn(null);

        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmailAddress("john.doe@example.com");
        when(userRepository.findByEmailAddress(any()))
                .thenReturn(Optional.of(mockUser));

        when(jwtImplementation.generateToken(any()))
                .thenReturn("mockJwtToken");

        LoginDto loginDto = new LoginDto("john.doe@example.com", "password");

        ResponseEntity<ApiResponse<LoginResponse>> response = authImplementation.login(loginDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals("John", response.getBody().getData().firstName());
        assertEquals("Doe", response.getBody().getData().lastName());
        assertEquals("mockJwtToken", response.getBody().getData().token());
    }


}


