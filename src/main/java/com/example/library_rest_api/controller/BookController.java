package com.example.library_rest_api.controller;

import com.example.library_rest_api.exception.CommonException;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/book")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Library", description = "API for managing the library's books and rental operations")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID from the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the book"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) throws CommonException {
        BookResponseDto response = bookService.getBookById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all books", description = "Retrieve all books available in the library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all books")
    })
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> response = bookService.getAllBooks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get book by ISBN", description = "Retrieve a book from the library by its ISBN number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the book by ISBN"),
            @ApiResponse(responseCode = "404", description = "Book not found by ISBN")
    })
    public ResponseEntity<BookResponseDto> getBookByIsbn(@PathVariable String isbn) throws CommonException {
        BookResponseDto response = bookService.getBookByIsbn(isbn);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/rent/{id}")
    @Operation(summary = "Rent a book by ID", description = "Rent a specific book by its ID. Marks the book as unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully rented"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Book is not available for rent")
    })
    public ResponseEntity<BookResponseDto> getRentBookById(@PathVariable Long id) throws CommonException {
        BookResponseDto response = bookService.getRentBookById(id);
        libraryService.rentBook(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/return/{id}")
    @Operation(summary = "Return a rented book", description = "Return a previously rented book by its ID, marking it as available again.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully returned"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponseDto> returnBookById(@PathVariable Long id) throws CommonException, ExecutionException, InterruptedException {
        BookResponseDto response = bookService.getReturnBookById(id);
        libraryService.returnBook(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new book", description = "Add a new book to the library collection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created")
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

    @PutMapping("/update/{id}")
    @Operation(summary = "Update book details", description = "Update the details of an existing book by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "404", description = "Book not found")
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

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a book by ID", description = "Remove a book from the library collection by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponseDto> deleteBookById(@PathVariable Long id) throws CommonException {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/all")
    @Operation(summary = "Delete all books", description = "Remove all books from the library collection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All books successfully deleted")
    })
    public ResponseEntity<BookResponseDto> deleteAllBooks() {
        bookService.deleteAllBooks();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
