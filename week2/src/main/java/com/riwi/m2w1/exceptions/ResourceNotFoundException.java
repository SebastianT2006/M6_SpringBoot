package com.riwi.m2w1.exceptions;

/**
 * Se lanza cuando un recurso (Event o Venue) no se encuentra por ID.
 * El GlobalExceptionHandler la captura y devuelve HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
