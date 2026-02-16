package com.application.discussion.project.presentation.controllers;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.users.AuthLoginServiceInterface;
import com.application.discussion.project.application.services.users.AuthLogoutService;
import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;
import com.application.discussion.project.presentation.validations.AuthLoginRequestValidation;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestAPIControllerTest
@DisplayName("AuthController - MockMVC統合テスト")
public class AuthControllerMockMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthLoginServiceInterface authLoginServiceInterface;

    @MockitoBean
    private AuthLogoutService authLogoutService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_LOGIN_ID = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "testuser";
    private static final List<String> TEST_ROLES = List.of("ROLE_USER");
    private static final String TEST_LOGOUT_MESSAGE = "ログアウトに成功しました";
    private static final Boolean TEST_LOGOUT_SUCCESS = true;
    private static final String LOGIN_ENDPOINT = "/v1/auth/login";
    private static final String LOGOUT_ENDPOINT = "/v1/auth/logout";

    private LoginRequest mockLoginRequest;
    private LoginResponse mockLoginResponse;
    private LogoutResponse mockLogoutResponse;
    private LogoutResponseDTO mockLogoutResponseDTO;
    private ResponseCookie mockJwtCookie;

    @BeforeEach
    void setUp() {
        mockLoginRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
        mockLoginResponse = new LoginResponse(TEST_USER_ID, TEST_USERNAME, TEST_ROLES);
        mockLogoutResponse = LogoutResponse.of(TEST_LOGOUT_MESSAGE, TEST_LOGOUT_SUCCESS);
        
        mockJwtCookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .build();
        
        mockLogoutResponseDTO = LogoutResponseDTO.of(mockLogoutResponse, mockJwtCookie);
    }

    @Test
    @DisplayName("正常系: メールアドレスとパスワードでログインが成功すること")
    void loginWithEmailShouldReturnSuccess() throws Exception {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.ok(mockLoginResponse);
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .then(invocation -> null);
            
            when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(TEST_USER_ID.toString()))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
        }
    }

    @Test
    @DisplayName("正常系: ログインIDとパスワードでログインが成功すること")
    void loginWithLoginIdShouldReturnSuccess() throws Exception {
        LoginRequest loginIdRequest = new LoginRequest(TEST_LOGIN_ID, TEST_PASSWORD);
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.ok(mockLoginResponse);
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .then(invocation -> null);
            
            when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginIdRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
        }
    }

    @Test
    @DisplayName("異常系: メールアドレス未入力で400エラーが返却されること")
    void loginWithEmptyEmailShouldReturnBadRequest() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", TEST_PASSWORD);
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .thenThrow(new PresentationLayerErrorException(
                    "メールアドレスまたはログインIDは必須です",
                    HttpStatus.BAD_REQUEST,
                    HttpStatusCode.valueOf(400)
                ));

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("異常系: パスワード未入力で400エラーが返却されること")
    void loginWithEmptyPasswordShouldReturnBadRequest() throws Exception {
        LoginRequest invalidRequest = new LoginRequest(TEST_EMAIL, "");
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .thenThrow(new PresentationLayerErrorException("パスワードは必須です", HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400)));

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("異常系: 認証情報が誤っている場合401エラーが返却されること")
    void loginWithInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .then(invocation -> null);
            
            when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockLoginRequest)))
                .andExpect(status().isUnauthorized());
        }
    }

    @Test
    @DisplayName("異常系: ユーザーが存在しない場合404エラーが返却されること")
    void loginWithNonExistentUserShouldReturnNotFound() throws Exception {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .then(invocation -> null);
            
            when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockLoginRequest)))
                .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("異常系: アカウントロック時に403エラーが返却されること")
    void loginWithLockedAccountShouldReturnForbidden() throws Exception {
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        
        try (MockedStatic<AuthLoginRequestValidation> mockedValidation = Mockito.mockStatic(AuthLoginRequestValidation.class)) {
            mockedValidation.when(() -> AuthLoginRequestValidation.validate(any(LoginRequest.class)))
                .then(invocation -> null);
            
            when(authLoginServiceInterface.service(any(LoginRequest.class))).thenReturn(expectedResponse);

            mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockLoginRequest)))
                .andExpect(status().isForbidden());
        }
    }

    @Test
    @WithMockUser
    @DisplayName("正常系: ログアウトが成功し200ステータスとCookie削除ヘッダーが返却されること")
    void logoutSuccessfullyShouldReturnOkWithClearCookie() throws Exception {
        when(authLogoutService.service()).thenReturn(mockLogoutResponseDTO);

        mockMvc.perform(post(LOGOUT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value(TEST_LOGOUT_MESSAGE))
            .andExpect(jsonPath("$.success").value(TEST_LOGOUT_SUCCESS))
            .andExpect(header().exists("Set-Cookie"))
            .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("jwt=")))
            .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("Max-Age=0")));
    }

    @Test
    @WithMockUser
    @DisplayName("正常系: ログアウト時にJWT Cookieが正しく削除されること")
    void logoutShouldClearJwtCookieCorrectly() throws Exception {
        when(authLogoutService.service()).thenReturn(mockLogoutResponseDTO);

        mockMvc.perform(post(LOGOUT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("HttpOnly")))
            .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("Path=/")));
    }

    @Test
    @WithMockUser
    @DisplayName("異常系: 未認証状態でログアウト時に401エラーが返却されること")
    void logoutWithoutAuthenticationShouldReturnUnauthorized() throws Exception {
        when(authLogoutService.service())
            .thenThrow(new ApplicationLayerException(
                "ログアウト処理中に認証情報が見つかりません。",
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            ));

        mockMvc.perform(post(LOGOUT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("異常系: ログアウト権限がない場合に403エラーが返却されること")
    void logoutWithoutPermissionShouldReturnForbidden() throws Exception {
        when(authLogoutService.service())
            .thenThrow(new ApplicationLayerException(
                "ログアウト権限がありません。",
                HttpStatus.FORBIDDEN,
                HttpStatusCode.valueOf(403)
            ));

        mockMvc.perform(post(LOGOUT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("異常系: 不正なリクエスト形式で400エラーが返却されること")
    void loginWithInvalidJsonFormatShouldReturnBadRequest() throws Exception {
        String invalidJson = "{\"emailOrLoginId\":\"test@example.com\",\"password\":}";

        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("異常系: Content-Typeが指定されていない場合に415エラーが返却されること")
    void loginWithoutContentTypeShouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(post(LOGIN_ENDPOINT)
                .content(objectMapper.writeValueAsString(mockLoginRequest)))
            .andExpect(status().isUnsupportedMediaType());
    }
}
