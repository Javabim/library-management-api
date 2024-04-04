package com.maidscc.libraryManagementSystem.services;

import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface BorrowingService {
    ResponseEntity<ApiResponse<String>> borrowBook(Long bookId, Long userId);

    ResponseEntity<ApiResponse<String>> recordReturn(Long bookId, Long userId);

    ResponseEntity<ApiResponse<String>> approveBorrowRequest(Long borrowingRecordId);

    ResponseEntity<ApiResponse<String>> rejectBorrowRequest(Long borrowingRecordId);
}
