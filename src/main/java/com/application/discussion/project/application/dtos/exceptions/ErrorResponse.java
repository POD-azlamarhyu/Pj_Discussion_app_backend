package com.application.discussion.project.application.dtos.exceptions;

import lombok.Builder;

@Builder
public class ErrorResponse {
    private final String message;
    private final Integer statusCode;
    private final String type;
    
    private ErrorResponse(final String message,final Integer statusCode,final String type) {
        this.message = message;
        this.statusCode = statusCode;
        this.type = type;
    }

    public static ErrorResponse of(
        final String message,
        final Integer statusCode,
        final String type
    ) {
        return new ErrorResponse(message, statusCode, type);
    }

    public String getMessage(){
        return this.message;
    }

    public Integer getStatusCode(){
        return this.statusCode;
    }

    public String getType(){
        return this.type;
    }
}
