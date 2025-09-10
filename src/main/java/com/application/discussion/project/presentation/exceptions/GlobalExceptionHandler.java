package com.application.discussion.project.presentation.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.application.discussion.project.application.dtos.exceptions.ErrorResponse;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.domain.exceptions.BadRequestException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //TODO: Exceptionで例外をキャッチした場合はすべてINTERNAL_SERVER_ERRORクラスにするため，コメントアウトとする．今後削除するかをIssueで検討する．
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ErrorResponse> handleAllException(
    //         Exception ex,
    //         WebRequest webRequest) {
    //     logger.error("Error Handler an unexpected error occurred: {}", ex.getMessage(), ex);
    //     ErrorResponse errorResponse = ErrorResponse.of(
    //         "An unexpected error occurred",
    //         HttpStatus.INTERNAL_SERVER_ERROR.value(),
    //         "INTERNAL_SERVER_ERROR"
    //     );
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    // }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest webRequest) {
        logger.error("Error Handler resource not found: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            ex.getType()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex,
            WebRequest webRequest) {
        logger.error("Error Handler bad request: {}", ex);
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getType()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RequestNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotValidException(
        RequestNotValidException ex,
        WebRequest webRequest
    ) {
        logger.error("Error handler request not valid: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getType()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        logger.error("Error Handler validation error occurred: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        logger.error("Error Handler validation errors: {}", validationErrors);

        ErrorResponse errorResponse = ErrorResponse.of(
            "入力データに問題があります: " + validationErrors.toString(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {
        logger.warn("Error Handler invalid JSON format: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            "リクエストボディのJSON形式が不正です",
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_JSON"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorExceptions(
            InternalServerErrorException ex,
            WebRequest webRequest) {
        logger.error("Error Handler internal Server Error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getType()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
