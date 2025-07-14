package com.application.discussion.project.domain.valueobjects.users;

public class LoginId {
    private final String loginId;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 8;
    private final static String pattern = "^[a-zA-Z0-9._-]{8,255}$";

    private LoginId(String loginId) {
        this.validate(loginId);
        this.loginId = loginId;
    }
    private void validate(String loginId) {
        if (loginId == null || loginId.isEmpty()) {
            throw new IllegalArgumentException("Login ID cannot be null or empty");
        }
        if (loginId.length() > maxLength || loginId.length() < minLength) {
            throw new IllegalArgumentException("Login ID must be between " + minLength + " and " + maxLength + " characters");
        }
        if (!loginId.matches(pattern)) {
            throw new IllegalArgumentException("Login ID can only contain alphanumeric characters, dots, underscores, and hyphens");
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
