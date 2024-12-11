package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.CommonException;
import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.mapper.BookMapper;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRequestDto;
import com.example.library_rest_api.model.BookResponseDto;
import com.example.library_rest_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    private Book book;
    private BookRequestDto bookRequestDto;
    private BookResponseDto bookResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "1234567890", "Test Book", "Author", "Description", "Fiction", true);
        bookResponseDto = new BookResponseDto(1L, "1234567890", "Test Book", "Author", "Description", "Fiction", true);
        bookRequestDto = new BookRequestDto("1234567890", "Test Book", "Author", "Description", "Fiction", true);
    }

    @Test
    void getBookById_ShouldReturnBookResponseDto_WhenBookExists() throws CommonException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(bookResponseDto);

        BookResponseDto result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(bookResponseDto, result);
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowEntityNotExistException_WhenBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotExistException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("book with id 1 not exist", exception.getMessage());
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toResponse(book)).thenReturn(bookResponseDto);

        List<BookResponseDto> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals(bookResponseDto, result.get(0));
        verify(bookRepository).findAll();
    }

    @Test
    void createBook_ShouldReturnBookResponseDto_WhenBookIsCreated() {
        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(bookResponseDto);

        BookResponseDto result = bookService.createBook(bookRequestDto);

        assertNotNull(result);
        assertEquals(bookResponseDto, result);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBookResponseDto_WhenBookExists() throws CommonException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(bookResponseDto);

        BookResponseDto result = bookService.updateBook(bookRequestDto, 1L);

        assertNotNull(result);
        assertEquals(bookResponseDto, result);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_ShouldThrowEntityNotExistException_WhenBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotExistException.class, () -> {
            bookService.updateBook(bookRequestDto, 1L);
        });

        assertEquals("book with id 1 not exist", exception.getMessage());
    }

    @Test
    void deleteBook_ShouldDeleteBook_WhenBookExists() throws CommonException {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldThrowEntityNotExistException_WhenBookDoesNotExist() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotExistException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("book with id 1 not exist", exception.getMessage());
    }

    @Test
    void deleteAllBooks_ShouldDeleteAllBooks() {
        bookService.deleteAllBooks();

        verify(bookRepository).deleteAll();
    }
}