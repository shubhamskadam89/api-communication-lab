package com.example.apilab.repository.exception;

public class DuplicateRepositoryException extends RuntimeException {
    public DuplicateRepositoryException(String message) {
        super(message);
    }
}
