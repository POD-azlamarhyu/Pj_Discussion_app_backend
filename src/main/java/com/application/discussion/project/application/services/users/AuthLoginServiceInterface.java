package com.application.discussion.project.application.services.users;

import org.springframework.http.ResponseEntity;

import com.application.discussion.project.application.dtos.users.LoginRequest;

public interface AuthLoginServiceInterface {
    ResponseEntity<?> service(
        LoginRequest loginRequest
    );
}
