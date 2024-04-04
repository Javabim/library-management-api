package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.dtos.BookInfo;
import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.exception.MaidSccLibraryException;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.BookRepository;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.services.BookService;
import com.maidscc.libraryManagementSystem.utils.ISBNValidator;
import com.maidscc.libraryManagementSystem.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<ApiResponse<Book>> addBook(Book book) {

        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if (!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to add a book");
        }

        if (!ISBNValidator.isValidISBN(book.getIsbn())) {
            throw new MaidSccLibraryException("Invalid ISBN format");
        }

        Optional<Book> existingBook = bookRepository.findByIsbnOrTitle(book.getIsbn(), book.getTitle());
        if (existingBook.isPresent()) {
            throw new MaidSccLibraryException("A book with the same ISBN or title already exists");
        }


        Book savedBook = bookRepository.save(book);

        savedBook.setAvailable(true);
        bookRepository.save(savedBook);

        return ResponseEntity.ok(new ApiResponse<>("Book created successfully", HttpStatus.OK));
    }


    public ResponseEntity<ApiResponse<List<BookInfo>>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookInfo> bookInfos = books.stream().map(this::mapToBookInfo).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>("Books retrieved successfully", HttpStatus.OK, bookInfos));
    }

    public ResponseEntity<ApiResponse<BookInfo>> getBookById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new MaidSccLibraryException("The book you searched for does not exist");
        }
        BookInfo bookInfo = mapToBookInfo(bookOptional.get());
        return ResponseEntity.ok(new ApiResponse<>("Book retrieved successfully", HttpStatus.OK, bookInfo));
    }

    @Override
    public ResponseEntity<ApiResponse<Book>> updateBook(Long id, Book updatedBook) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if(!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to update a book");
        }

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new MaidSccLibraryException("Book not found with id: " + id));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setQuantity(updatedBook.getQuantity());

        existingBook.setAvailable(existingBook.getQuantity() > 0);

        bookRepository.save(existingBook);

        return ResponseEntity.ok(new ApiResponse<>("Book updated successfully", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteBook(Long id) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MaidSccLibraryException("User not found"));

        if(!user.getRole().equals(UserRole.LIBRARIAN)) {
            throw new MaidSccLibraryException("You are not authorized to delete a book");
        }

        if (!bookRepository.existsById(id)) {
            throw new MaidSccLibraryException("Book does not exist");
        }

        bookRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Book deleted successfully", HttpStatus.OK));
    }

    private BookInfo mapToBookInfo(Book book) {
        return new BookInfo(
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.isAvailable()
        );


    }

}
