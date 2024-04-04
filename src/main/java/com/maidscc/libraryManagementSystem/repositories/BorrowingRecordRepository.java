package com.maidscc.libraryManagementSystem.repositories;

import com.maidscc.libraryManagementSystem.entities.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    BorrowingRecord findByBookIdAndUserId(Long bookId, Long userId);
}
