package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.BookNotAvailableException;
import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.mapper.BookMapper;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRentalInfo;
import com.example.library_rest_api.repository.BookRentalInfoRepository;
import com.example.library_rest_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookRentalInfoRepository bookRentalInfoRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private LibraryService libraryService;

    private Book testBook;
    private BookRentalInfo testRentalInfo;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setAvailable(true);

        testRentalInfo = new BookRentalInfo();
        testRentalInfo.setBookId(1L);
        testRentalInfo.setRentAt(LocalDate.now());
        testRentalInfo.setReturnAt(LocalDate.now().plusDays(7));
    }

    @Test
    void rentBook_shouldRentBookSuccessfully() throws EntityNotExistException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        libraryService.rentBook(1L);

        assertFalse(testBook.getAvailable());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRentalInfoRepository, times(1)).save(any(BookRentalInfo.class));
    }

    @Test
    void rentBook_shouldThrowExceptionIfBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotExistException exception = assertThrows(EntityNotExistException.class, () -> libraryService.rentBook(1L));

        assertEquals("book with id 1 not exist", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void rentBook_shouldThrowExceptionIfBookNotAvailable() {
        testBook.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        BookNotAvailableException exception = assertThrows(BookNotAvailableException.class, () -> libraryService.rentBook(1L));

        assertEquals("book with id 1 not available", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void returnBook_shouldReturnBookSuccessfully() throws EntityNotExistException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRentalInfoRepository.findFirstByBookIdAndReturnAtAfter(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.of(testRentalInfo));

        libraryService.returnBook(1L);

        assertTrue(testBook.getAvailable());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRentalInfoRepository, times(1)).findFirstByBookIdAndReturnAtAfter(eq(1L), any(LocalDate.class));
        verify(bookRentalInfoRepository, times(1)).delete(testRentalInfo);
    }

    @Test
    void returnBook_shouldThrowExceptionIfBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotExistException exception = assertThrows(EntityNotExistException.class, () -> libraryService.returnBook(1L));

        assertEquals("book with id 1 not exist", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void returnBook_shouldThrowExceptionIfNoActiveRentalFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRentalInfoRepository.findFirstByBookIdAndReturnAtAfter(eq(1L), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        EntityNotExistException exception = assertThrows(EntityNotExistException.class, () -> libraryService.returnBook(1L));

        assertEquals("No active rental found for book with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRentalInfoRepository, times(1)).findFirstByBookIdAndReturnAtAfter(eq(1L), any(LocalDate.class));
    }

    @Test
    void getBookRentalInfo_shouldReturnRentalInfoSuccessfully() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRentalInfoRepository.findByBookId(1L)).thenReturn(List.of(testRentalInfo));

        List<BookRentalInfo> rentalInfos = libraryService.getBookRentalInfo(1L);

        assertNotNull(rentalInfos);
        assertEquals(1, rentalInfos.size());
        assertEquals(testRentalInfo, rentalInfos.get(0));
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRentalInfoRepository, times(1)).findByBookId(1L);
    }

    @Test
    void getBookRentalInfo_shouldThrowExceptionIfBookDoesNotExist() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        EntityNotExistException exception = assertThrows(EntityNotExistException.class, () -> libraryService.getBookRentalInfo(1L));

        assertEquals("Book with id 1 does not exist", exception.getMessage());
        verify(bookRepository, times(1)).existsById(1L);
    }
}
