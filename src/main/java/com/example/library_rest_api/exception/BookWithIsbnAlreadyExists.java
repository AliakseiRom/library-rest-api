package com.example.library_rest_api.exception;

public class BookWithIsbnAlreadyExists extends RuntimeException {
    public BookWithIsbnAlreadyExists(String message) {
        super(message);
    }
}
