package com.application.discussion.project.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.application.discussion.project.application.dtos.exceptions.ErrorResponse;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorExceptions(
        InternalServerErrorException ex
    ){
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getType()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(
        Exception ex
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(
            "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex,
        WebRequest webRequest
    ){
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            ex.getType()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
