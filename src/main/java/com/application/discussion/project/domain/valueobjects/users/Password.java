package com.application.discussion.project.domain.valueobjects.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Password {
    private final String password;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 10;
    private final static String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{10,255}$";

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Password(String password) {
        this.validate(password);
        this.password = passwordEncoder.encode(password);
    }

    private void validate(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() > maxLength || password.length() < minLength) {
            throw new IllegalArgumentException("Password must be between " + minLength + " and " + maxLength + " characters");
        }
        if (!password.matches(pattern)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
        }
    }

    public static Password of(String password) {
        return new Password(password);
    }

    public String value() {
        return this.password;
    }

    public boolean isBelowMaxLength() {
        return this.password.length() <= maxLength;
    }

    public boolean isAboveMinLength() {
        return this.password.length() >= minLength;
    }
}
