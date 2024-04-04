package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.entities.BorrowingRecord;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.BorrowingStatus;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.exception.MaidSccLibraryException;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.BookRepository;
import com.maidscc.libraryManagementSystem.repositories.BorrowingRecordRepository;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.services.BorrowingService;
import com.maidscc.libraryManagementSystem.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {
    private final UserRepository userRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<String>> borrowBook(Long bookId, Long userId) {
        String userEmail = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(userEmail)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new MaidSccLibraryException("Book not found"));

        if (!book.isAvailable()) {
            throw new MaidSccLibraryException("Book is not available for borrowing");
        }

        BorrowingRecord existingBorrowingRecord = borrowingRecordRepository.findByBookIdAndUserId(bookId, userId);
        if (existingBorrowingRecord != null) {
            throw new MaidSccLibraryException("You have already borrowed this book");
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setBorrowingDate(LocalDate.now());
        borrowingRecord.setReturnDate(LocalDate.now().plusDays(14));
        borrowingRecord.setReturned(false);

        if (user.getRole().equals(UserRole.LIBRARIAN)) {
            approveBorrowRequestForLibrarian(borrowingRecord, userId);
            return ResponseEntity.ok(new ApiResponse<>("Borrowing request approved successfully", HttpStatus.OK));
        } else if (user.getRole().equals(UserRole.PATRON)) {
            borrowingRecord.setStatus(BorrowingStatus.PENDING);
            borrowingRecord.setUser(user);
            borrowingRecordRepository.save(borrowingRecord);
            return ResponseEntity.ok(new ApiResponse<>("Borrowing request initiated successfully", HttpStatus.OK));
        }
        throw new MaidSccLibraryException("User role not supported for borrowing");

    }



    private void approveBorrowRequestForLibrarian(BorrowingRecord borrowingRecord, Long userId) {
        borrowingRecord.setStatus(BorrowingStatus.APPROVED);

        User userToBorrow = userRepository.findById(userId)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        borrowingRecord.setUser(userToBorrow);
        borrowingRecordRepository.save(borrowingRecord);

        Book book = borrowingRecord.getBook();
        book.setQuantity(book.getQuantity() - 1);
        book.setAvailable(book.getQuantity() > 0);
        bookRepository.save(book);

    }


    @Override
    public ResponseEntity<ApiResponse<String>> approveBorrowRequest(Long borrowingRecordId) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if (!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to approve borrowing requests");
        }

        BorrowingRecord borrowingRecord = borrowingRecordRepository.findById(borrowingRecordId)
                .orElseThrow(() -> new MaidSccLibraryException("Borrowing record not found"));

        if (borrowingRecord.getStatus() != BorrowingStatus.PENDING) {
            throw new MaidSccLibraryException("Borrowing request has already been processed");
        }

        borrowingRecord.setStatus(BorrowingStatus.APPROVED);
        borrowingRecordRepository.save(borrowingRecord);

        Book book = borrowingRecord.getBook();

        book.setQuantity(book.getQuantity() - 1);
        book.setAvailable(book.getQuantity() > 0);
        bookRepository.save(book);

        return ResponseEntity.ok(new ApiResponse<>("Borrowing request approved successfully", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> rejectBorrowRequest(Long borrowingRecordId) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if (!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to approve borrowing requests");
        }

        BorrowingRecord borrowingRecord = borrowingRecordRepository.findById(borrowingRecordId)
                .orElseThrow(() -> new MaidSccLibraryException("Borrowing record not found"));

        if (borrowingRecord.getStatus() == BorrowingStatus.PENDING) {
            borrowingRecord.setStatus(BorrowingStatus.REJECTED);
            borrowingRecordRepository.save(borrowingRecord);
            return ResponseEntity.ok(new ApiResponse<>("Borrowing request rejected successfully", HttpStatus.OK));
        } else {
            throw new MaidSccLibraryException("Borrowing request cannot be rejected as it is not pending");
        }
    }

    @Override
    public ResponseEntity<ApiResponse<String>> recordReturn(Long bookId, Long userId) {
        String userEmail = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(userEmail)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if (!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to record book returns");
        }

        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndUserId(bookId, userId);
        if (borrowingRecord == null) {
            throw new MaidSccLibraryException("Borrowing record not found for the given bookId or patronId");
        }

        if (borrowingRecord.isReturned()) {
            throw new MaidSccLibraryException("The book has already been returned by the user");
        }

        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecord.setReturned(true);

        borrowingRecordRepository.save(borrowingRecord);

        Book book = borrowingRecord.getBook();
        if (book == null) {
            throw new MaidSccLibraryException("Book associated with the borrowing record not found");
        }

        int currentQuantity = book.getQuantity();
        book.setQuantity(currentQuantity + 1);
        book.setAvailable(true);

        bookRepository.save(book);

        return ResponseEntity.ok(new ApiResponse<>("Book return recorded successfully", HttpStatus.OK));
    }


}
