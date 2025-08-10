package com.application.discussion.project.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class RequestNotValidException extends RuntimeException {
    private String type;

    public RequestNotValidException(
        String message, 
        String type
    ){
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
