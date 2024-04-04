package com.maidscc.libraryManagementSystem.entities;

import com.maidscc.libraryManagementSystem.enums.BorrowingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "borrow_user_fkey")
    )
    private User user;

    @Column(nullable = false)
    private LocalDate borrowingDate;

    @Column(nullable = false)
    private LocalDate returnDate;

    @Column(nullable = false)
    private boolean returned;

    @Enumerated(EnumType.STRING)
    private BorrowingStatus status = BorrowingStatus.PENDING;


}

