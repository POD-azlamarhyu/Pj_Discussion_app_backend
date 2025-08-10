package com.application.discussion.project.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private String type;

    public BadRequestException(
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
