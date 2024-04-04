package com.maidscc.libraryManagementSystem.controllers;

import com.maidscc.libraryManagementSystem.dtos.BookInfo;
import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping("/allBooks")
    public ResponseEntity<ApiResponse<List<BookInfo>>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookInfo>> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookService.updateBook(id, updatedBook);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }
}

