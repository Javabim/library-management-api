package com.maidscc.libraryManagementSystem.repositories;

import com.maidscc.libraryManagementSystem.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbnOrTitle(String isbn, String title);
}
