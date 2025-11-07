// src/main/java/com/example/mortgage/api/error/GlobalExceptionHandler.java
package com.example.mortgage.api.error;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;
import jakarta.servlet.http.HttpServletRequest; // import

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onBeanValidation(MethodArgumentNotValidException ex) {
        var msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst().orElse("Invalid request");
        return ResponseEntity.badRequest().body(new ApiError("VALIDATION_ERROR", msg, traceId()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> onNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("NOT_FOUND", ex.getMessage(), traceId()));
    }

    // Note: accept HttpServletRequest so we can inspect the request path
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> onAny(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();

        // If it's the OpenAPI endpoint, rethrow so springdoc can return diagnostic JSON
        if (path != null && path.startsWith("/v3/api-docs")) {
            throw new RuntimeException(ex);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_ERROR", "Something went wrong", traceId()));
    }

    private String traceId() { return java.util.UUID.randomUUID().toString(); }
}
