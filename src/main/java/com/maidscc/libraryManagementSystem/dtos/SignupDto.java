package com.maidscc.libraryManagementSystem.dtos;

import com.maidscc.libraryManagementSystem.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignupDto(
        @NotEmpty(message = "First name cannot be empty")
        String firstName,
        @NotEmpty(message = "Last name cannot be empty")
        String lastName,
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email is not properly formatted")
        String emailAddress,
        @NotEmpty(message = "Country cannot be empty")
        String country,
        @NotEmpty(message = "Password cannot be empty")
        String password,
        @NotEmpty(message = "Confirm password cannot be empty")
        String repeatPassword,
        String gender,
        @NotEmpty(message = "Contact address cannot be empty")
        String contactAddress,
        UserRole role

) {
}
