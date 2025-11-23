package com.application.discussion.project.application.dtos.users;


public class LoginRequest {
    private String emailOrLoginId;
    private String password;

    public LoginRequest() {
    }
    public LoginRequest(String emailOrLoginId, String password) {
        this.emailOrLoginId = emailOrLoginId;
        this.password = password;
    }
    public String getEmailOrLoginId() {
        return emailOrLoginId;
    }
    public void setEmailOrLoginId(String emailOrLoginId) {
        this.emailOrLoginId = emailOrLoginId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;  
    }
}
