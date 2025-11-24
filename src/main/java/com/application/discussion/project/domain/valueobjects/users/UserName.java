package com.application.discussion.project.domain.valueobjects.users;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class UserName {
    private final String userName;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 1;

    private UserName(final String userName) {
        this.validate(userName);
        this.userName = userName;
    }
    private void validate(final String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new DomainLayerErrorException("ユーザー名は必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (userName.length() > maxLength || userName.length() < minLength) {
            throw new DomainLayerErrorException("ユーザー名は" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
    public static UserName of(final String userName) {
        return new UserName(userName);
    }
    public String value() {
        return this.userName;
    }
    public boolean isBelowMaxLength() {
        return this.userName.length() <= maxLength;
    }
    public boolean isAboveMinLength() {
        return this.userName.length() >= minLength;
    }
}
