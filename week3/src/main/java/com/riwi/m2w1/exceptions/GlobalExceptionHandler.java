package com.riwi.m2w1.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones.
 *
 * Captura excepciones y devuelve respuestas JSON con el código HTTP correcto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Valida errores de @Valid → HTTP 400 Bad Request.
     * Devuelve un mapa con el campo y el mensaje de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        response.put("message", "Datos inválidos. Revisa los campos.");
        response.put("errors", fieldErrors);
        return response;
    }

    /**
     * Captura ResourceNotFoundException → HTTP 404 Not Found.
     * Escenario 2: ID inexistente devuelve 404 con mensaje claro.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return error;
    }

    /**
     * Captura cualquier otro RuntimeException → HTTP 500.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Error interno: " + ex.getMessage());
        return error;
    }
}
