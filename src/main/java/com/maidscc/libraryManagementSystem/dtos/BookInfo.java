package com.maidscc.libraryManagementSystem.dtos;

public record BookInfo(
        String title,
        String author,
        int publicationYear,
        boolean available
) {
}
