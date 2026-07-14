package com.espe.drivex.exception;

/**
 * Lanzada cuando se intenta crear un registro duplicado o se viola
 * una regla de negocio (409 Conflict).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
