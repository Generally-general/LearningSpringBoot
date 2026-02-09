package com.myProject.demo.exception;

import com.myProject.demo.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> catchAll(Exception ex) {
        ApiResponse<Void> response = new ApiResponse<>(false, "Something went wrong", null);
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
       String message = ex.getBindingResult()
               .getFieldErrors()
               .stream()
               .findFirst()
               .map(err -> err.getField() + " " + err.getDefaultMessage())
               .orElse("Validation Failed");

       log.warn("Validation Failed: {}", message);
       ApiResponse<Void> response = new ApiResponse<>(false, message, null);

       return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            ResourceNotFoundException ex
    ) {
        log.warn("Resource not found: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), null);

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(
            ConflictException ex
    ) {
        log.warn("Conflict occurred: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), null);

        return ResponseEntity.status(409).body(response);
    }
}
