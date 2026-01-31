package com.myProject.demo.exception;

import com.myProject.demo.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
       String message = ex.getBindingResult()
               .getFieldErrors()
               .stream()
               .findFirst()
               .map(err -> err.getField() + " " + err.getDefaultMessage())
               .orElse("Validation Failed");

       ApiError error = new ApiError(
               400,
               message,
               request.getRequestURI()
       );

       return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                404,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(
            ConflictException ex,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                409,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(409).body(error);
    }
}
