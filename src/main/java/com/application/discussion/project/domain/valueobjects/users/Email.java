package com.application.discussion.project.domain.valueobjects.users;

import java.util.regex.Pattern;

public class Email {
    private final String email;
    private final static Integer maxLength = 255;
    
    private Email(String email){
        this.validate(email);
        this.email = email;
    }
    private void validate(String email){
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if(email.length() > maxLength) {
            throw new IllegalArgumentException("Email cannot exceed " + maxLength + " characters");
        }

        Pattern emailBasicPattern = Pattern.compile("/^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/");

        Pattern emailBannedDomainPattern = Pattern.compile(".*\\.(jp|com)$");

        if(!emailBasicPattern.matcher(email).matches() || !emailBannedDomainPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static Email of(String email) {
        return new Email(email);
    }

    public String value(){
        return this.email;
    }

    public boolean isBelowMaxLength(){
        return this.email.length() <= maxLength;
    }
}
