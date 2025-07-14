package com.application.discussion.project.application.dtos.users;

import java.util.List;
import java.util.UUID;

import lombok.Builder;



public class LoginResponse {
    private UUID userId;
    private String token;
    private String username;
    private List<String> roles;

    public LoginResponse() {
    }
    public LoginResponse(UUID userId, String token, String username, List<String> roles) {
        this.userId = userId;
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID userId;
        private String token;
        private String username;
        private List<String> roles;

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(userId, token, username, roles);
        }
    }
}
