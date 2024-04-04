package com.maidscc.libraryManagementSystem.services;

import com.maidscc.libraryManagementSystem.dtos.PatronDto;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ApiResponse<User>> addPatron(SignupDto signupDto);

    ResponseEntity<ApiResponse<List<PatronDto>>> getAllPatrons();

    ResponseEntity<ApiResponse<PatronDto>> getPatronById(Long id);

    ResponseEntity<ApiResponse<User>> updatePatron(Long id, User updatedPatron);

    ResponseEntity<ApiResponse<String>> deletePatron(Long id);
}
