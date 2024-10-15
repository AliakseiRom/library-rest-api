package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.CommonException;
import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRentalInfo;
import com.example.library_rest_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "1234567890", "Test Book", "Author", "Description", "Fiction", true);
    }

    @Test
    void rentBook_ShouldStoreRentalInfo_WhenBookExists() throws CommonException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        libraryService.rentBook(1L);

        BookRentalInfo rentalInfo = libraryService.getBookRentalInfo(1L);
        assertNotNull(rentalInfo);
        assertEquals(LocalDate.now(), rentalInfo.rentAt());
        assertEquals(LocalDate.now().plusDays(7), rentalInfo.returnAt());
    }

    @Test
    void rentBook_ShouldThrowException_WhenBookDoesNotExist() throws CommonException {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotExistException.class, () -> {
            libraryService.rentBook(1L);
        });

        assertEquals("Book with id 1 does not exist", exception.getMessage());
    }

    @Test
    void returnBook_ShouldRemoveRentalInfo_WhenBookExists() throws EntityNotExistException {
        Book book = new Book(1L, "1234567890", "Test Book", "Author", "Description", "Fiction", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        libraryService.rentBook(1L);
        assertNotNull(libraryService.getBookRentalInfo(1L));

        libraryService.returnBook(1L);

        assertNull(libraryService.getBookRentalInfo(1L));
    }

    @Test
    void returnBook_ShouldDoNothing_WhenBookDoesNotExist() {
        assertDoesNotThrow(() -> {
            libraryService.returnBook(1L);
        });
    }
}
