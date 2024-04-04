package com.maidscc.libraryManagementSystem.controllers;

import com.maidscc.libraryManagementSystem.dtos.BookInfo;
import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Test
    void testAddBook() {
        BookService bookService = mock(BookService.class);
        BookController bookController = new BookController(bookService);
        Book book = new Book();
        ResponseEntity<ApiResponse<Book>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(book, "Book added successfully"), HttpStatus.CREATED);

        when(bookService.addBook(book)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<Book>> response = bookController.addBook(book);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetAllBooks() {
        BookService bookService = mock(BookService.class);
        BookController bookController = new BookController(bookService);
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        ResponseEntity<ApiResponse<List<Book>>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(books, "All books retrieved successfully"), HttpStatus.OK);

        when(bookService.getAllBooks()).thenReturn((ResponseEntity) expectedResponse);
        ResponseEntity<ApiResponse<List<BookInfo>>> response = bookController.getAllBooks();

        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetBookById() {
        BookService bookService = mock(BookService.class);
        BookController bookController = new BookController(bookService);
        Long id = 1L;
        BookInfo bookInfo = new BookInfo("Title", "Author", 2022, true);
        ResponseEntity<ApiResponse<BookInfo>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(bookInfo, "Book retrieved successfully"), HttpStatus.OK);

        when(bookService.getBookById(id)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<BookInfo>> response = bookController.getBookById(id);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testUpdateBook() {
        BookService bookService = mock(BookService.class);
        BookController bookController = new BookController(bookService);
        Long id = 1L;
        Book updatedBook = new Book();
        updatedBook.setId(id);
        ResponseEntity<ApiResponse<Book>> expectedResponse = new ResponseEntity<>(new ApiResponse<>(updatedBook, "Book updated successfully"), HttpStatus.OK);

        when(bookService.updateBook(id, updatedBook)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<Book>> response = bookController.updateBook(id, updatedBook);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testDeleteBook() {
        BookService bookService = mock(BookService.class);
        BookController bookController = new BookController(bookService);
        Long id = 1L;
        ResponseEntity<ApiResponse<String>> expectedResponse = new ResponseEntity<>(new ApiResponse<>("Book deleted successfully", HttpStatus.OK).getStatus());

        when(bookService.deleteBook(id)).thenReturn(expectedResponse);
        ResponseEntity<ApiResponse<String>> response = bookController.deleteBook(id);

        assertEquals(expectedResponse, response);
    }
}
