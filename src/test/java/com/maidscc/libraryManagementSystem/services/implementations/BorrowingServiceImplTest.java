package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.entities.BorrowingRecord;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.exception.MaidSccLibraryException;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.BookRepository;
import com.maidscc.libraryManagementSystem.repositories.BorrowingRecordRepository;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.utils.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BorrowingServiceImplTest {

    private final BookRepository bookRepository = mock(BookRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final BorrowingRecordRepository borrowingRecordRepository = mock(BorrowingRecordRepository.class);
    private final BorrowingServiceImpl borrowingService = new BorrowingServiceImpl(userRepository, borrowingRecordRepository, bookRepository);

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn("test@user.com");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook_ByPatron_ShouldReturnSuccessResponse() {
        String userEmail = "patron@test.com";
        User user = new User();
        user.setRole(UserRole.PATRON);
        when(userRepository.findByEmailAddress(userEmail)).thenReturn(Optional.of(user));
        when(UserUtil.getLoggedInUser()).thenReturn(userEmail);

        Long bookId = 1L;
        Book book = new Book();
        book.setAvailable(true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<ApiResponse<String>> response = borrowingService.borrowBook(bookId, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Borrowing request initiated successfully", response.getBody().getMessage());
    }

    @Test
    void testBorrowBook_UserNotFound_ShouldThrowException() {
        String userEmail = "nonexistent@test.com";
        when(userRepository.findByEmailAddress(userEmail)).thenReturn(Optional.empty());
        when(UserUtil.getLoggedInUser()).thenReturn(userEmail);

        assertThrows(MaidSccLibraryException.class, () -> borrowingService.borrowBook(1L, 2L));
    }

    @Test
    void testBorrowBook_BookNotFound_ShouldThrowException() {
        String userEmail = "patron@test.com";
        User user = new User();
        user.setRole(UserRole.PATRON);
        when(userRepository.findByEmailAddress(userEmail)).thenReturn(Optional.of(user));
        when(UserUtil.getLoggedInUser()).thenReturn(userEmail);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(MaidSccLibraryException.class, () -> borrowingService.borrowBook(1L, 2L));
    }

    @Test
    void testBorrowBook_BookNotAvailable_ShouldThrowException() {
        String userEmail = "patron@test.com";
        User user = new User();
        user.setRole(UserRole.PATRON);
        when(userRepository.findByEmailAddress(userEmail)).thenReturn(Optional.of(user));
        when(UserUtil.getLoggedInUser()).thenReturn(userEmail);

        Long bookId = 1L;
        Book book = new Book();
        book.setAvailable(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(MaidSccLibraryException.class, () -> borrowingService.borrowBook(bookId, 2L));
    }

    @Test
    void testBorrowBook_UserAlreadyBorrowedBook_ShouldThrowException() {
        String userEmail = "patron@test.com";
        User user = new User();
        user.setRole(UserRole.PATRON);
        when(userRepository.findByEmailAddress(userEmail)).thenReturn(Optional.of(user));
        when(UserUtil.getLoggedInUser()).thenReturn(userEmail);

        Long bookId = 1L;
        Book book = new Book();
        book.setAvailable(true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(borrowingRecordRepository.findByBookIdAndUserId(bookId, 2L)).thenReturn(new BorrowingRecord());

        assertThrows(MaidSccLibraryException.class, () -> borrowingService.borrowBook(bookId, 2L));
    }


    @Test
    void testBorrowBook_WhenBookIsNotAvailable_ShouldThrowException() {
        Book book = new Book();
        book.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(MaidSccLibraryException.class, () -> borrowingService.borrowBook(1L, 2L));
    }


    @Test
    void testRecordReturn_WhenRecordNotFound_ShouldThrowException() {
        when(borrowingRecordRepository.findByBookIdAndUserId(1L, 2L)).thenReturn(null);

        assertThrows(MaidSccLibraryException.class, () -> borrowingService.recordReturn(1L, 2L));
    }

}
