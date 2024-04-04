package com.maidscc.libraryManagementSystem.controllers;

import com.maidscc.libraryManagementSystem.dtos.PatronDto;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<User>> addPatron(@RequestBody SignupDto signupDto) {
        return userService.addPatron(signupDto);
    }

    @GetMapping("/allPatrons")
    public ResponseEntity<ApiResponse<List<PatronDto>>> getAllPatrons() {
        return userService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatronDto>> getPatronById(@PathVariable Long id) {
        return userService.getPatronById(id);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<User>> updatePatron(@PathVariable Long id, @RequestBody User updatedPatron) {
        return userService.updatePatron(id, updatedPatron);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<String>> deletePatron(@PathVariable Long id) {
        return userService.deletePatron(id);
    }
}



