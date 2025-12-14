package com.application.discussion.project.presentation.controllers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.services.users.AuthLoginServiceInterface;
import com.application.discussion.project.presentation.validations.AuthLoginRequestValidation;

@RestAPIControllerTest
@DisplayName("AuthController - ログイン機能のテスト")
public class AuthControllerTests {

    @Mock
    private AuthLoginServiceInterface authLoginServiceInterface;

    @InjectMocks
    private AuthController authController;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_ACCESS_TOKEN = "access_token_sample";
    private static final String TEST_REFRESH_TOKEN = "refresh_token_sample";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "testuser";
    private static final List<String> TEST_ROLES = List.of("ROLE_USER");


    private LoginRequest mockLoginRequest;
    private LoginResponse mockLoginResponse;

    @BeforeEach
    void setUp() {
        mockLoginRequest = new LoginRequest(
            TEST_EMAIL,
            TEST_PASSWORD
        );
        mockLoginResponse = new LoginResponse(
            TEST_USER_ID,
            TEST_USERNAME,
            TEST_ROLES
        );
    }

    @Test
    @DisplayName("正常系: 有効な認証情報でログインが成功すること")
    void loginWithValidCredentialsShouldReturnSuccess() {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.ok(mockLoginResponse);
        when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                    .then(invocation -> null);

            ResponseEntity<LoginResponse> actualResponse = authController.login(mockLoginRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(actualResponse.getBody()).isNotNull();
            

            mockedValidation.verify(() -> AuthLoginRequestValidation.validate(mockLoginRequest));
            verify(authLoginServiceInterface).service(mockLoginRequest);
        }
    }

    @Test
    @DisplayName("異常系: バリデーションエラーで例外がスローされること")
    void loginWithInvalidRequestShouldThrowException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmailOrLoginId("");
        invalidRequest.setPassword("");

        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                    .thenThrow(new IllegalArgumentException("Validation failed"));

            assertThatThrownBy(() -> authController.login(invalidRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Validation failed");

            mockedValidation.verify(() -> AuthLoginRequestValidation.validate(invalidRequest));
        }
    }

    @Test
    @DisplayName("異常系: 認証失敗時に401ステータスが返却されること")
    void loginWithInvalidCredentialsShouldReturnUnauthorized() {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                    .then(invocation -> null);

            ResponseEntity<LoginResponse> actualResponse = authController.login(mockLoginRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(actualResponse.getBody()).isNull();

            verify(authLoginServiceInterface).service(mockLoginRequest);
        }
    }

    @Test
    @DisplayName("異常系: ユーザーが存在しない場合に404ステータスが返却されること")
    void loginWithNonExistentUserShouldReturnNotFound() {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                    .then(invocation -> null);

            ResponseEntity<LoginResponse> actualResponse = authController.login(mockLoginRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(actualResponse.getBody()).isNull();

            verify(authLoginServiceInterface).service(mockLoginRequest);
        }
    }

    @Test
    @DisplayName("異常系: アカウントがロックされている場合に403ステータスが返却されること")
    void loginWithLockedAccountShouldReturnForbidden() {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                    .then(invocation -> null);

            ResponseEntity<LoginResponse> actualResponse = authController.login(mockLoginRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(actualResponse.getBody()).isNull();

            verify(authLoginServiceInterface).service(mockLoginRequest);
        }
    }
}
