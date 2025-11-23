package com.application.discussion.project.domain.valueobjects.users;

public class UserName {
    private final String userName;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 1;

    private UserName(String userName) {
        this.validate(userName);
        this.userName = userName;
    }
    private void validate(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (userName.length() > maxLength || userName.length() < minLength) {
            throw new IllegalArgumentException("User name must be between " + minLength + " and " + maxLength + " characters");
        }
    }
    public static UserName of(String userName) {
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
