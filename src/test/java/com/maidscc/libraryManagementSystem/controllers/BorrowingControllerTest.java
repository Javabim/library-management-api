package com.maidscc.libraryManagementSystem.controllers;

import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.BorrowingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BorrowingControllerTest {

    @Mock
    private BorrowingService borrowingService;

    @InjectMocks
    private BorrowingController borrowingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testBorrowBook() {
        Long bookId = 1L;
        Long patronId = 1L;
        ResponseEntity<ApiResponse<String>> expectedResponse = new ResponseEntity<>(new ApiResponse<>("Book borrowed successfully", HttpStatus.OK), HttpStatus.OK);
        when(borrowingService.borrowBook(bookId, patronId)).thenReturn(expectedResponse);

        ResponseEntity<ApiResponse<String>> response = borrowingController.borrowBook(bookId, patronId);

        assertEquals(expectedResponse, response);
        verify(borrowingService, times(1)).borrowBook(bookId, patronId);
    }

    @Test
    public void testRejectBorrowRequest() {
        when(borrowingService.rejectBorrowRequest(anyLong()))
                .thenReturn(new ResponseEntity<>(new ApiResponse<>("Borrowing request rejected successfully", HttpStatus.OK).getStatus()));

        ResponseEntity<ApiResponse<String>> responseEntity = borrowingController.rejectBorrowRequest(123L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Borrowing request rejected successfully", responseEntity.getBody().getMessage());
    }

    @Test
    public void testApproveBorrowRequest() {
        when(borrowingService.approveBorrowRequest(anyLong()))
                .thenReturn(new ResponseEntity<>(new ApiResponse<>("Borrowing request approved successfully", HttpStatus.OK).getStatus()));

        ResponseEntity<ApiResponse<String>> responseEntity = borrowingController.approveBorrowRequest(123L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Borrowing request approved successfully", responseEntity.getBody().getMessage());
    }

    @Test
    void testRecordReturn() {
        Long bookId = 1L;
        Long patronId = 1L;
        ResponseEntity<ApiResponse<String>> expectedResponse = new ResponseEntity<>(new ApiResponse<>("Book return recorded successfully", HttpStatus.OK), HttpStatus.OK);
        when(borrowingService.recordReturn(bookId, patronId)).thenReturn(expectedResponse);

        ResponseEntity<ApiResponse<String>> response = borrowingController.recordReturn(bookId, patronId);

        assertEquals(expectedResponse, response);
        verify(borrowingService, times(1)).recordReturn(bookId, patronId);
    }
}
