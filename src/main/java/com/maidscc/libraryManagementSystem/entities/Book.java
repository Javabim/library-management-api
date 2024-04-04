package com.maidscc.libraryManagementSystem.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private int publicationYear;

    @Column(nullable = false, unique = true)
    private String isbn;


    @Column(nullable = false)
    private int quantity;

    @Column(columnDefinition = "boolean default true")
    private boolean available;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BorrowingRecord> borrowingRecords;
}

