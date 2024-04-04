package com.maidscc.libraryManagementSystem.controllers;


import com.maidscc.libraryManagementSystem.dtos.LoginDto;
import com.maidscc.libraryManagementSystem.dtos.LoginResponse;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServices authServices;

    @PostMapping("/signup/patron")
    public ResponseEntity<ApiResponse<String>> signupPatron(@RequestBody SignupDto signupDto) {
        return authServices.signupPatron(signupDto);
    }

    @PostMapping("/signup/librarian")
    public ResponseEntity<ApiResponse<String>> signupLibrarian(@RequestBody SignupDto signupDto) {
        return authServices.signupLibrarian(signupDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginDto loginDto){
        return authServices.login(loginDto);
    }
}
