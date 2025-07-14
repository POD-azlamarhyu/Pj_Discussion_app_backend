package com.application.discussion.project.domain.valueobjects.users;

public class EmailOrLoginId {

    private final String emailOrLoginId;
    private final static String loginIdPattern = "^[a-zA-Z0-9._-]{8,255}$";
    private final static Integer maxLength = 255;
    private final static Integer minLength = 10;
    private final static String emailPattern = "/^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/";
    private final static String emailBannedDomainPattern = ".*\\.(jp|com)$";

    private EmailOrLoginId(String emailOrLoginId) {
        if (emailOrLoginId == null || emailOrLoginId.isEmpty()) {
            throw new IllegalArgumentException("Email or Login ID cannot be null or empty");
        }
        this.validateEmail(emailOrLoginId);
        this.validateLoginId(emailOrLoginId);
        this.emailOrLoginId = emailOrLoginId;
    }

    public static EmailOrLoginId of(String emailOrLoginId) {
        return new EmailOrLoginId(emailOrLoginId);
    }

    public String value() {
        return this.emailOrLoginId;
    }

    private void validateEmail(String emailOrLoginId) {
        if (emailOrLoginId.length() > maxLength) {
            throw new IllegalArgumentException("Email or Login ID cannot exceed " + maxLength + " characters");
        }
        if (!emailOrLoginId.matches(emailPattern) || !emailOrLoginId.matches(emailBannedDomainPattern)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateLoginId(String emailOrLoginId) {
        if (!emailOrLoginId.matches(loginIdPattern)) {
            throw new IllegalArgumentException("Login ID can only contain alphanumeric characters, dots, underscores, and hyphens");
        }

        if (emailOrLoginId.length() < minLength || emailOrLoginId.length() > maxLength) {
            throw new IllegalArgumentException("Login ID must be between " + minLength + " and " + maxLength + " characters");
        }
    }
}
