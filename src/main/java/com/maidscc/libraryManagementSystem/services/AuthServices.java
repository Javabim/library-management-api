package com.maidscc.libraryManagementSystem.services;

import com.maidscc.libraryManagementSystem.dtos.LoginDto;
import com.maidscc.libraryManagementSystem.dtos.LoginResponse;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthServices {
    ResponseEntity<ApiResponse<LoginResponse>> login(LoginDto loginDto);

    ResponseEntity<ApiResponse<String>> signup(SignupDto signupDto, UserRole userRole);

    ResponseEntity<ApiResponse<String>> signupPatron(SignupDto signupDto);

    ResponseEntity<ApiResponse<String>> signupLibrarian(SignupDto signupDto);
}
