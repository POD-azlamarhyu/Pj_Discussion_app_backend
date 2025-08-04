package com.application.discussion.project.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class ResourceNotFoundException extends RuntimeException {

    private String type;

    public ResourceNotFoundException(String message, String type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
