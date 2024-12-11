package com.example.library_rest_api.controller;

import com.example.library_rest_api.exception.CommonException;
import com.example.library_rest_api.model.BookRentalInfo;
import com.example.library_rest_api.model.BookRequestDto;
import com.example.library_rest_api.model.BookResponseDto;
import com.example.library_rest_api.service.BookService;
import com.example.library_rest_api.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/books")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Library", description = "API for managing the library's books and rental operations")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID from the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the book"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) throws CommonException {
        BookResponseDto response = bookService.getBookById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get all books", description = "Retrieve all books available in the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all books"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> response = bookService.getAllBooks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get book by ISBN", description = "Retrieve a book from the library by its ISBN number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the book by ISBN"),
            @ApiResponse(responseCode = "404", description = "Book not found by ISBN"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<BookResponseDto> getBookByIsbn(@PathVariable String isbn) throws CommonException {
        BookResponseDto response = bookService.getBookByIsbn(isbn);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rent/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Rent a book by ID", description = "Rent a specific book by its ID. Marks the book as unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully rented"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Book is not available for rent"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<String> getRentBookById(@PathVariable Long id) throws CommonException {
        libraryService.rentBook(id);
        return new ResponseEntity<>("Book rented successfully", HttpStatus.OK);
    }

    @GetMapping("/return/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Return a rented book", description = "Return a previously rented book by its ID, marking it as available again.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully returned"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<String> returnBookById(@PathVariable Long id) throws CommonException, ExecutionException, InterruptedException {
        libraryService.returnBook(id);
        return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
    }

    @GetMapping("/rental_info/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
            summary = "Get book rental information", description = "Retrieves a list of rental information for a specific book identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rental information"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
    })
    public ResponseEntity<List<BookRentalInfo>> getRentalInfo(@PathVariable Long id) throws CommonException {
        List<BookRentalInfo> bookRentalInfo = libraryService.getBookRentalInfo(id);
        return new ResponseEntity<>(bookRentalInfo, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book", description = "Add a new book to the library collection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<BookResponseDto> createBook(@RequestBody
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                                                              @ExampleObject(
                                                                      name = "ContractRemark",
                                                                      value = """
                                                                              {
                                                                                "isbn": "test_isbn",
                                                                                "title": "test_test",
                                                                                "author": "test_author",
                                                                                "description": "test_description",
                                                                                "genre": "test_genre",
                                                                                "available": true
                                                                              }"""
                                                              )
                                                      }))
                                                      BookRequestDto book) {
        BookResponseDto response = bookService.createBook(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book details", description = "Update the details of an existing book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<BookResponseDto> updateBook(@RequestBody
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                                                              @ExampleObject(
                                                                      name = "ContractRemark",
                                                                      value = """
                                                                              {
                                                                                "isbn": "test_isbn1",
                                                                                "title": "test_test1",
                                                                                "author": "test_author1",
                                                                                "description": "test_description1",
                                                                                "genre": "test_genre1",
                                                                                "available": false
                                                                              }"""
                                                              )
                                                      }))
                                                      BookRequestDto book, @PathVariable Long id) throws CommonException {
        BookResponseDto response = bookService.updateBook(book, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book by ID", description = "Remove a book from the library collection by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) throws CommonException {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete all books", description = "Remove all books from the library collection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All books successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    public ResponseEntity<Void> deleteAllBooks() {
        bookService.deleteAllBooks();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
