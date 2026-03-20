package com.application.discussion.project.application.dtos.users;

/**
 * 認証状態確認レスポンスDTO
 */
public class AuthCheckResponse {

    private String username;
    private Boolean isAuthenticated;
    private String message;

    public AuthCheckResponse() {
    }

    private AuthCheckResponse(String username, Boolean isAuthenticated, String message) {
        this.username = username;
        this.isAuthenticated = isAuthenticated;
        this.message = message;
    }

    public static AuthCheckResponse of(String username, Boolean isAuthenticated, String message) {
        return new AuthCheckResponse(username, isAuthenticated, message);
    }

    public String getUsername() {
        return username;
    }

    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AuthCheckResponse{username='" + username + "', isAuthenticated=" + isAuthenticated + ", message='" + message + "'}";
    }
}
