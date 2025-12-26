package com.application.discussion.project.presentation.controllers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.users.AuthLoginServiceInterface;
import com.application.discussion.project.application.services.users.AuthLogoutService;
import com.application.discussion.project.presentation.validations.AuthLoginRequestValidation;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestAPIControllerTest
@DisplayName("AuthController - 認証機能のテスト")
public class AuthControllerTests {

    @Mock
    private AuthLoginServiceInterface authLoginServiceInterface;

    @Mock
    private AuthLogoutService authLogoutService;

    @InjectMocks
    private AuthController authController;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_ACCESS_TOKEN = "access_token_sample";
    private static final String TEST_REFRESH_TOKEN = "refresh_token_sample";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "testuser";
    private static final List<String> TEST_ROLES = List.of("ROLE_USER");
    private static final String TEST_LOGOUT_MESSAGE = "ログアウトに成功しました";
    private static final Boolean TEST_LOGOUT_SUCCESS = true;
    private static final String TEST_ERROR_MESSAGE = "ログアウト処理中にエラーが発生しました";
    private static final String LOGIN_ENDPOINT = "/v1/auth/login";
    private static final String LOGOUT_ENDPOINT = "/v1/auth/logout";

    private LoginRequest mockLoginRequest;
    private LoginResponse mockLoginResponse;
    private LogoutResponse mockLogoutResponse;
    private LogoutResponseDTO mockLogoutResponseDTO;
    private ResponseCookie mockJwtCookie;

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
        
        mockLogoutResponse = LogoutResponse.of(TEST_LOGOUT_MESSAGE,TEST_LOGOUT_SUCCESS);
        
        mockJwtCookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();
        
        mockLogoutResponseDTO = LogoutResponseDTO.of(mockLogoutResponse, mockJwtCookie);
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

    @Test
    @DisplayName("正常系: ログアウトが成功し200ステータスとCookie削除ヘッダーが返却されること")
    void logoutSuccessfullyShouldReturnOkWithClearCookie() {
        when(authLogoutService.service()).thenReturn(mockLogoutResponseDTO);

        ResponseEntity<LogoutResponse> actualResponse = authController.logout();

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody().getMessage()).isEqualTo(TEST_LOGOUT_MESSAGE);
        assertThat(actualResponse.getBody().getSuccess()).isEqualTo(TEST_LOGOUT_SUCCESS);
        assertThat(actualResponse.getHeaders().get("Set-Cookie")).isNotNull();
        assertThat(actualResponse.getHeaders().get("Set-Cookie").get(0)).contains("jwt=");
        assertThat(actualResponse.getHeaders().get("Set-Cookie").get(0)).contains("Max-Age=0");
        
        verify(authLogoutService).service();
    }

    @Test
    @DisplayName("正常系: ログアウト時にJWT Cookieが正しく削除されること")
    void logoutShouldClearJwtCookie() {
        when(authLogoutService.service()).thenReturn(mockLogoutResponseDTO);

        ResponseEntity<LogoutResponse> actualResponse = authController.logout();

        assertThat(actualResponse.getHeaders().getFirst("Set-Cookie")).isEqualTo(mockJwtCookie.toString());
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isNotNull();
        
        verify(authLogoutService).service();
    }

    @Test
    @DisplayName("異常系: ログアウトサービスが例外をスローした場合に例外が伝播すること")
    void logoutWhenServiceThrowsExceptionShouldPropagate() {
        when(authLogoutService.service()).thenThrow(new ApplicationLayerException("ログアウト処理中に認証情報が見つかりません。",HttpStatus.UNAUTHORIZED,HttpStatusCode.valueOf(401)));

        assertThatThrownBy(() -> authController.logout())
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("ログアウト処理中に認証情報が見つかりません。");
        
        verify(authLogoutService).service();
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("MockMVC - 正常系: ログインAPIに有効な認証情報でアクセスする")
    void mockMvcLoginWithValidCredentials() throws Exception {
        when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(ResponseEntity.ok(mockLoginResponse));

        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID.toString()))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    @DisplayName("MockMVC - 異常系: ログインAPIに無効な認証情報でアクセスする")
    void mockMvcLoginWithInvalidCredentials() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "");

        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("MockMVC - 異常系: 存在しないユーザーでログインAPIにアクセスする")
    void mockMvcLoginWithNonExistentUser() throws Exception {
        when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockLoginRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("MockMVC - 異常系: ログアウトAPIでエラーが発生する")
    void mockMvcLogoutWithError() throws Exception {
        when(authLogoutService.service()).thenThrow(new ApplicationLayerException("ログアウト処理中に認証情報が見つかりません。",HttpStatus.UNAUTHORIZED,HttpStatusCode.valueOf(401)));

        mockMvc.perform(post(LOGOUT_ENDPOINT))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("ログアウト処理中に認証情報が見つかりません。"));
    }
}
