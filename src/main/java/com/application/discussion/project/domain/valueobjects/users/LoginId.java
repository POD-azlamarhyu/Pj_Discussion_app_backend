package com.application.discussion.project.domain.valueobjects.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.apache.commons.lang3.StringUtils;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

public class LoginId {
    private final String loginId;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 8;
    private final static String pattern = "^[a-zA-Z0-9._-]{8,255}$";

    private LoginId(final String loginId) {
        this.validate(loginId);
        this.loginId = loginId;
    }
    private void validate(final String loginId) {
        if (StringUtils.isBlank(loginId)) {
            throw new DomainLayerErrorException("ログインIDは必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (loginId.length() > maxLength || loginId.length() < minLength) {
            throw new DomainLayerErrorException("ログインIDは" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (!loginId.matches(pattern)) {
            throw new DomainLayerErrorException("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
    public static LoginId of(String loginId) {
        return new LoginId(loginId);
    }
    public String value() {
        return this.loginId;
    }
    public boolean isBelowMaxLength() {
        return this.loginId.length() <= maxLength;
    }
    public boolean isAboveMinLength() {
        return this.loginId.length() >= minLength;
    }

}
