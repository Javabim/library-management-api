package com.maidscc.libraryManagementSystem.controllers;

import com.maidscc.libraryManagementSystem.dtos.PatronDto;
import com.maidscc.libraryManagementSystem.dtos.SignupDto;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void testAddPatron() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        SignupDto signupDto = new SignupDto("John", "Doe", "john@example.com", "USA", "password", "password", "Male", "Address", UserRole.PATRON);
        ResponseEntity<ApiResponse<User>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(new User(), "Patron added successfully"), HttpStatus.CREATED);

        when(userService.addPatron(signupDto)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<User>> response = userController.addPatron(signupDto);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetAllPatrons() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        List<PatronDto> patrons = new ArrayList<>();
        patrons.add(new PatronDto("John", "Doe", "Male", "USA"));
        patrons.add(new PatronDto("Jane", "Smith", "Female", "Canada"));
        ResponseEntity<ApiResponse<List<PatronDto>>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(patrons, "All patrons retrieved successfully"), HttpStatus.OK);

        when(userService.getAllPatrons()).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<List<PatronDto>>> response = userController.getAllPatrons();

        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetPatronById() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        Long id = 1L;
        PatronDto patronDto = new PatronDto("John", "Doe", "Male", "USA"); // Adjust as per your PatronDto structure
        ResponseEntity<ApiResponse<PatronDto>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(patronDto, "Patron retrieved successfully"), HttpStatus.OK);

        when(userService.getPatronById(id)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<PatronDto>> response = userController.getPatronById(id);

        assertEquals(expectedResponse, response);
    }


    @Test
    void testUpdatePatron() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        Long id = 1L;
        User updatedPatron = new User();
        updatedPatron.setId(id);
        ResponseEntity<ApiResponse<User>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(updatedPatron, "Patron updated successfully"), HttpStatus.OK);

        when(userService.updatePatron(id, updatedPatron)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<User>> response = userController.updatePatron(id, updatedPatron);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testDeletePatron() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        Long id = 1L;
        ResponseEntity<ApiResponse<String>> expectedResponse = new ResponseEntity<>(new ApiResponse<>("Patron deleted successfully", HttpStatus.OK).getStatus());

        when(userService.deletePatron(id)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<String>> response = userController.deletePatron(id);

        assertEquals(expectedResponse, response);
    }
}
