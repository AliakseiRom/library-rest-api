package com.example.library_rest_api.service;

import com.example.library_rest_api.exception.BookNotAvailableException;
import com.example.library_rest_api.exception.CommonException;
import com.example.library_rest_api.exception.EntityNotExistException;
import com.example.library_rest_api.mapper.BookMapper;
import com.example.library_rest_api.model.Book;
import com.example.library_rest_api.model.BookRequestDto;
import com.example.library_rest_api.model.BookResponseDto;
import com.example.library_rest_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookMapper bookMapper;

    public BookResponseDto getBookById(Long id) throws CommonException {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotExistException("book with id " + id + " not exist");
        }
        Book bookEntity = book.get();
        return bookMapper.toResponse(bookEntity);
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> bookMapper.toResponse(book))
                .toList();
    }

    public BookResponseDto getBookByIsbn(String isbn) throws CommonException {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new EntityNotExistException("book with isbn " + isbn + " not exist");
        }
        BookResponseDto bookResponseDto = bookMapper.toResponse(book);
        return bookResponseDto;
    }

    public BookResponseDto getRentBookById(Long bookId) throws CommonException {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotExistException("book with id " + bookId + " not exist");
        }
        Book bookEntity = book.get();

        if (!bookEntity.getAvailable()){
            throw new BookNotAvailableException("book with id " + bookId + " not available");
        }
        bookEntity.setAvailable(false);
        BookResponseDto bookResponseDto = bookMapper.toResponse(bookRepository.save(bookEntity));
        return bookResponseDto;
    }

    public BookResponseDto getReturnBookById(Long bookId) throws CommonException {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotExistException("book with id " + bookId + " not exist");
        }
        Book bookEntity = book.get();
        bookEntity.setAvailable(true);
        BookResponseDto response = bookMapper.toResponse(bookRepository.save(bookEntity));
        return response;
    }

    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        Book book = bookMapper.toEntity(bookRequestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    public BookResponseDto updateBook(BookRequestDto book, Long id) throws CommonException {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()) {
            Book bookToUpdate = bookOptional.get();
            bookToUpdate.setIsbn(book.isbn());
            bookToUpdate.setTitle(book.title());
            bookToUpdate.setAuthor(book.author());
            bookToUpdate.setDescription(book.description());
            bookToUpdate.setGenre(book.genre());
            bookToUpdate.setAvailable(book.available());
            Book updatedBook = bookRepository.save(bookToUpdate);
            return bookMapper.toResponse(updatedBook);
        }
        throw new EntityNotExistException("book with id " + id + " not exist");
    }

    public void deleteBook(Long id) throws CommonException {
        if(bookRepository.existsById(id))  {
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotExistException("book with id " + id + " not exist");
        }
    }

    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }
}
