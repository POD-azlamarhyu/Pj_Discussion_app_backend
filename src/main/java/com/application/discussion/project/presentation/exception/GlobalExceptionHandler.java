package com.application.discussion.project.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.application.discussion.project.application.dtos.exception.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // You can define exception handling methods here
    // For example, to handle specific exceptions globally

    // @ExceptionHandler(SomeSpecificException.class)
    // public ResponseEntity<ErrorResponse> handleSomeSpecificException(SomeSpecificException ex) {
    //     ErrorResponse errorResponse = new ErrorResponse("Error message", ex.getMessage());
    //     return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    // }

    // Add more exception handling methods as needed
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
        Exception ex,
        WebRequest request
    ){
        ErrorDetails errorDetails = new ErrorDetails(
            "An error occurred",
            ex.getMessage()
        );
        return new ResponseEntity<>(
            errorDetails,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
        IllegalArgumentException ex,
        WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
            "Invalid argument",
            ex.getMessage()
        );
        return new ResponseEntity<>(
            errorDetails,
            HttpStatus.BAD_REQUEST
        );
    }
}
