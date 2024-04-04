package com.maidscc.libraryManagementSystem.controllers;

import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/user/{userId}")
    public ResponseEntity<ApiResponse<String>> borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
        return borrowingService.borrowBook(bookId, userId);
    }

    @PostMapping("/{borrowingRecordId}/approve")
    public ResponseEntity<ApiResponse<String>> approveBorrowRequest(@PathVariable Long borrowingRecordId) {
        borrowingService.approveBorrowRequest(borrowingRecordId);
        return ResponseEntity.ok(new ApiResponse<>("Borrowing request approved successfully", HttpStatus.OK));
    }

    @PostMapping("/{borrowingRecordId}/reject")
    public ResponseEntity<ApiResponse<String>> rejectBorrowRequest(@PathVariable Long borrowingRecordId) {
        borrowingService.rejectBorrowRequest(borrowingRecordId);
        return ResponseEntity.ok(new ApiResponse<>("Borrowing request rejected successfully", HttpStatus.OK));
    }

    @PutMapping("/return/{bookId}/user/{userId}")
    public ResponseEntity<ApiResponse<String>> recordReturn(@PathVariable Long bookId, @PathVariable Long userId) {
        return borrowingService.recordReturn(bookId, userId);
    }
}

