package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.BookNotAvailableException;
import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.mapper.BookMapper;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRentalInfo;
import com.example.library_rest_api.repository.BookRentalInfoRepository;
import com.example.library_rest_api.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibraryService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookRentalInfoRepository bookRentalInfoRepository;

    private static final Integer BOOK_RENT_PERIOD = 7;
    @Autowired
    private BookMapper bookMapper;

    @Async("threadPoolTaskExecutor")
    public void rentBook(Long bookId) throws EntityNotExistException {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotExistException("book with id " + bookId + " not exist");
        }
        Book bookEntity = book.get();

        if (!bookEntity.getAvailable()){
            throw new BookNotAvailableException("book with id " + bookId + " not available");
        }
        bookEntity.setAvailable(false);

        LocalDate rentAt = LocalDate.now();
        LocalDate returnAt = rentAt.plusDays(BOOK_RENT_PERIOD);

        BookRentalInfo rentalInfo = new BookRentalInfo();
        rentalInfo.setBookId(bookId);
        rentalInfo.setRentAt(rentAt);
        rentalInfo.setReturnAt(returnAt);

        bookRentalInfoRepository.save(rentalInfo);
    }

    @Async("threadPoolTaskExecutor")
    public void returnBook(Long bookId) throws EntityNotExistException {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotExistException("book with id " + bookId + " not exist");
        }
        Book bookEntity = book.get();
        bookEntity.setAvailable(true);

        Optional<BookRentalInfo> activeRental = bookRentalInfoRepository.findFirstByBookIdAndReturnAtAfter(bookId, LocalDate.now());
        if (activeRental.isEmpty()) {
            throw new EntityNotExistException("No active rental found for book with id " + bookId);
        }
        bookRentalInfoRepository.delete(activeRental.get());
    }

    public List<BookRentalInfo> getBookRentalInfo(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotExistException("Book with id " + bookId + " does not exist");
        }

        return bookRentalInfoRepository.findByBookId(bookId);
    }
}
