package com.maidscc.libraryManagementSystem.services;


import com.maidscc.libraryManagementSystem.dtos.BookInfo;
import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {
    ResponseEntity<ApiResponse<Book>> addBook(Book book);

    ResponseEntity<ApiResponse<List<BookInfo>>> getAllBooks();

    ResponseEntity<ApiResponse<BookInfo>> getBookById(Long id);

    ResponseEntity<ApiResponse<Book>> updateBook(Long id, Book updatedBook);

    ResponseEntity<ApiResponse<String>> deleteBook(Long id);

}
