package com.example.library_rest_api.repository;

import com.example.library_rest_api.model.BookRentalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRentalInfoRepository extends JpaRepository<BookRentalInfo, Long> {
    List<BookRentalInfo> findByBookId(Long bookId);

    Optional<BookRentalInfo> findFirstByBookIdAndReturnAtAfter(Long bookId, LocalDate now);
}