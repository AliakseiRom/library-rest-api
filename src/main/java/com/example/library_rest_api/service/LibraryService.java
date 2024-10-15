package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRentalInfo;
import com.example.library_rest_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    BookRepository bookRepository;

    private static final Integer BOOK_RENT_PERIOD = 7;

    private static Map<Long, BookRentalInfo> bookRentalMap = new HashMap<>();

    @Async("threadPoolTaskExecutor")
    public void rentBook(Long bookId) throws EntityNotExistException {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotExistException("Book with id " + bookId + " does not exist");
        }

        LocalDate rentAt = LocalDate.now();
        LocalDate returnAt = rentAt.plusDays(BOOK_RENT_PERIOD);
        bookRentalMap.put(bookId, BookRentalInfo
                .builder()
                .setRentAt(rentAt)
                .setReturnAt(returnAt)
                .build());
    }

    @Async("threadPoolTaskExecutor")
    public void returnBook(Long bookId) throws EntityNotExistException {
        bookRentalMap.remove(bookId);
    }

    public BookRentalInfo getBookRentalInfo(Long bookId) {
        return bookRentalMap.get(bookId);
    }
}
