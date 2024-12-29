package com.example.library_rest_api.exception;

public class AccessDeniedException extends CommonException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
