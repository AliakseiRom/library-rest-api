package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.AccessDeniedException;
import com.example.library_rest_api.exception.BookNotAvailableException;
import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRentalInfo;
import com.example.library_rest_api.repository.BookRentalInfoRepository;
import com.example.library_rest_api.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class LibraryService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookRentalInfoRepository bookRentalInfoRepository;

    private static final Integer BOOK_RENT_PERIOD = 7;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<?> rentBook(Long bookId) throws EntityNotExistException {
        if (!hasUserRole()) {
            throw new AccessDeniedException("You do not have permission to update this book.");
        }

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
        return null;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<?> returnBook(Long bookId) throws EntityNotExistException {
        if (!hasUserRole()) {
            throw new AccessDeniedException("You do not have permission to update this book.");
        }

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotExistException("book with id " + bookId + " not exist");
        }
        Book bookEntity = book.get();
        bookEntity.setAvailable(true);

        Optional<BookRentalInfo> activeRental = bookRentalInfoRepository.findFirstByBookId(bookId);
        if (activeRental.isEmpty()) {
            throw new EntityNotExistException("No active rental found for book with id " + bookId);
        }
        bookRentalInfoRepository.delete(activeRental.get());
        return null;
    }

    public List<BookRentalInfo> getBookRentalInfo(Long bookId) {
        if (!hasUserRole()) {
            throw new AccessDeniedException("You do not have permission to update this book.");
        }

        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotExistException("Book with id " + bookId + " does not exist");
        }

        return bookRentalInfoRepository.findByBookId(bookId);
    }

    private boolean hasUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
    }
}
