package com.maidscc.libraryManagementSystem.dtos;

public record LoginResponse(
        String firstName,
        String lastName,
        String token
) {
}
