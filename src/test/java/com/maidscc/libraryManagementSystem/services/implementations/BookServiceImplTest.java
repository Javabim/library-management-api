package com.maidscc.libraryManagementSystem.services.implementations;

import com.maidscc.libraryManagementSystem.dtos.BookInfo;
import com.maidscc.libraryManagementSystem.entities.Book;
import com.maidscc.libraryManagementSystem.entities.User;
import com.maidscc.libraryManagementSystem.enums.UserRole;
import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import com.maidscc.libraryManagementSystem.repositories.BookRepository;
import com.maidscc.libraryManagementSystem.repositories.UserRepository;
import com.maidscc.libraryManagementSystem.utils.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private final BookRepository bookRepository = mock(BookRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserUtil userUtilMock = mock(UserUtil.class);
    private final BookServiceImpl bookService = new BookServiceImpl(bookRepository, userRepository);

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn("librarian@example.com");

        MockitoAnnotations.openMocks(this);

        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testAddBookWithValidISBN() {
        Book book = new Book();
        book.setIsbn("978-3-16-148410-0");
        book.setTitle("Test Book");

        when(userUtilMock.getLoggedInUser()).thenReturn("test@user.com");

        User user = mock(User.class);
        UserRole userRole = UserRole.LIBRARIAN;
        when(user.getRole()).thenReturn(userRole);
        when(userRepository.findByEmailAddress("test@user.com")).thenReturn(Optional.of(user));

        when(bookRepository.findByIsbnOrTitle("978-3-16-148410-0", "Test Book")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        ResponseEntity<ApiResponse<Book>> response = bookService.addBook(book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book created successfully", response.getBody().getMessage());
    }



    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(new Book(),
                new Book()));

        ResponseEntity<ApiResponse<List<BookInfo>>> response = bookService.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Books retrieved successfully", response.getBody().getMessage());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void testGetBookById() {
        Long id = 1L;
        String title = "Title";
        String author = "Author";
        int publicationYear = 2000;
        boolean available = true;

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        book.setAvailable(available);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        ResponseEntity<ApiResponse<BookInfo>> response = bookService.getBookById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book retrieved successfully", response.getBody().getMessage());

        assertNotNull(response.getBody().getData());
        assertEquals(title, response.getBody().getData().title());
        assertEquals(author, response.getBody().getData().author());
        assertEquals(publicationYear, response.getBody().getData().publicationYear());
        assertEquals(available, response.getBody().getData().available());
    }



    @Test
    void testUpdateBook() {
        Long id = 1L;
        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setQuantity(5);

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("New Author");
        updatedBook.setPublicationYear(2022);
        updatedBook.setQuantity(10);

        when(userUtilMock.getLoggedInUser()).thenReturn("test@user.com");

        User user = mock(User.class);
        UserRole userRole = UserRole.LIBRARIAN;
        when(user.getRole()).thenReturn(userRole);
        when(userRepository.findByEmailAddress("test@user.com")).thenReturn(Optional.of(user));

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));

        ResponseEntity<ApiResponse<Book>> response = bookService.updateBook(id, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book updated successfully", response.getBody().getMessage());
        assertEquals("Updated Book", existingBook.getTitle());
        assertEquals("New Author", existingBook.getAuthor());
        assertEquals(2022, existingBook.getPublicationYear());
        assertEquals(10, existingBook.getQuantity());
        assertEquals(true, existingBook.isAvailable());
    }


    @Test
    void testDeleteBook() {
        Long id = 1L;

        when(userUtilMock.getLoggedInUser()).thenReturn("test@user.com");

        User user = mock(User.class);
        UserRole userRole = UserRole.LIBRARIAN;
        when(user.getRole()).thenReturn(userRole);
        when(userRepository.findByEmailAddress("test@user.com")).thenReturn(Optional.of(user));

        when(bookRepository.existsById(id)).thenReturn(true);

        ResponseEntity<ApiResponse<String>> response = bookService.deleteBook(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book deleted successfully", response.getBody().getMessage());
    }




}

