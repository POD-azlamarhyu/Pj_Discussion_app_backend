package com.application.discussion.project.application.dtos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {
    private String type;

    public InternalServerErrorException(
        String message, 
        String type
    ) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
