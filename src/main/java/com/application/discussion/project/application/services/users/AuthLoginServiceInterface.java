package com.application.discussion.project.application.services.users;

import org.springframework.http.ResponseEntity;

import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;

public interface AuthLoginServiceInterface {
    ResponseEntity<LoginResponse> service(
        final LoginRequest loginRequest
    );
}
