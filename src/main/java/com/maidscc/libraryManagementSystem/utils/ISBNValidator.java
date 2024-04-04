package com.maidscc.libraryManagementSystem.utils;

public class ISBNValidator {

    public static boolean isValidISBN(String isbn) {
        // Remove hyphens (if any)
        isbn = isbn.replaceAll("-", "");

        // Check length
        if (isbn.length() != 13) {
            return false;
        }

        // Check format
        if (!isbn.matches("[0-9]+")) {
            return false;
        }

        // Calculate checksum
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checksum = 10 - (sum % 10);
        if (checksum == 10) {
            checksum = 0;
        }

        // Compare with the last digit
        return checksum == Character.getNumericValue(isbn.charAt(12));
    }

    public static void main(String[] args) {
        String isbn = "978-3-16-148410-0"; // Example ISBN
        if (isValidISBN(isbn)) {
            System.out.println("ISBN is valid");
        } else {
            System.out.println("ISBN is not valid");
        }
    }
}

