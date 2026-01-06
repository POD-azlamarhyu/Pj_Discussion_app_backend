package com.application.discussion.project.presentation.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.SignUpRequest;
import com.application.discussion.project.application.dtos.users.SignUpResponse;
import com.application.discussion.project.application.services.users.UserRegistrationService;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestAPIControllerTest
@DisplayName("UserController テスト")
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRegistrationService mockUserRegistrationService;

    private static final String SIGNUP_ENDPOINT = "/v1/users/signup";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_LOGIN_ID = "testuser123";
    private static final String VALID_PASSWORD = "Password123";
    private static final String VALID_USERNAME = "TestUser";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String INVALID_PASSWORD = "pass123";

    private SignUpRequest validRequest;
    private SignUpResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = new SignUpRequest();
        validRequest.setEmail(VALID_EMAIL);
        validRequest.setPassword(VALID_PASSWORD);
        validRequest.setUsername(VALID_USERNAME);

        expectedResponse = new SignUpResponse();
        expectedResponse.setEmail(VALID_EMAIL);
        expectedResponse.setUsername(VALID_USERNAME);
    }

    @Test
    @DisplayName("正常にユーザ登録が完了し201ステータスが返されること")
    void signupWithValidRequestReturnsCreatedStatus() throws Exception {
        when(mockUserRegistrationService.service(any(SignUpRequest.class))).thenReturn(expectedResponse);

        MvcResult actualResult = mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(VALID_EMAIL))
                .andExpect(jsonPath("$.username").value(VALID_USERNAME))
                .andReturn();

        assertThat(actualResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(mockUserRegistrationService, times(1)).service(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("メールアドレスがnullの場合400エラーが返されること")
    void signupWithNullEmailReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(null);
        invalidRequest.setPassword(VALID_PASSWORD);
        invalidRequest.setUsername(VALID_USERNAME);

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("メールアドレスは必須項目です"));
    }

    @Test
    @DisplayName("メールアドレスが空文字の場合400エラーが返されること")
    void signupWithEmptyEmailReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail("");
        invalidRequest.setPassword(VALID_PASSWORD);
        invalidRequest.setUsername(VALID_USERNAME);

        doThrow(new DomainLayerErrorException("メールアドレスは必須項目です", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("メールアドレスは必須項目です"));
    }

    @Test
    @DisplayName("ユーザーネームがnullの場合400エラーが返されること")
    void signupWithNullUserNameReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(VALID_EMAIL);
        invalidRequest.setPassword(VALID_PASSWORD);
        invalidRequest.setUsername(null);

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ユーザーネームは必須項目です"));
    }

    @Test
    @DisplayName("ユーザーネームが空文字の場合400エラーが返されること")
    void signupWithEmptyUserNameReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(VALID_EMAIL);
        invalidRequest.setPassword(VALID_PASSWORD);
        invalidRequest.setUsername("");

        doThrow(new DomainLayerErrorException("ユーザー名は必須項目です", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));
        
        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ユーザー名は必須項目です"));
    }

    @Test
    @DisplayName("パスワードがnullの場合400エラーが返されること")
    void signupWithNullPasswordReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(VALID_EMAIL);
        invalidRequest.setPassword(null);
        invalidRequest.setUsername(VALID_USERNAME);

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("パスワードが空文字の場合400エラーが返されること")
    void signupWithEmptyPasswordReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(VALID_EMAIL);
        invalidRequest.setPassword("");
        invalidRequest.setUsername(VALID_USERNAME);

        doThrow(new DomainLayerErrorException("パスワードは必須項目です", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("メールアドレスが不正な形式の場合400エラーが返されること")
    void signupWithInvalidEmailFormatReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(INVALID_EMAIL);
        invalidRequest.setPassword(VALID_PASSWORD);
        invalidRequest.setUsername(VALID_USERNAME);

        doThrow(new DomainLayerErrorException("メールアドレスの形式が正しくありません", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("パスワードが不正な形式の場合400エラーが返されること")
    void signupWithInvalidPasswordFormatReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(VALID_EMAIL);
        invalidRequest.setPassword(INVALID_PASSWORD);
        invalidRequest.setUsername(VALID_USERNAME);

        doThrow(new DomainLayerErrorException("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります"));
    }

    @Test
    @DisplayName("パスワードが最小長未満の場合400エラーが返されること")
    void signupWithTooShortPasswordReturnsBadRequest() throws Exception {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail(VALID_EMAIL);
        invalidRequest.setPassword("Pass123");
        invalidRequest.setUsername(VALID_USERNAME);

        doThrow(new DomainLayerErrorException("パスワードは10文字以上255文字以下である必要があります", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("パスワードは10文字以上255文字以下である必要があります"));
    }

    @Test
    @DisplayName("既に登録されているメールアドレスの場合409エラーが返されること")
    void signupWithDuplicateEmailReturnsConflict() throws Exception {
        doThrow(new DomainLayerErrorException("このメールアドレスは既に登録されています", HttpStatus.CONFLICT, HttpStatus.valueOf(409)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath( "$.message").value("このメールアドレスは既に登録されています"));
    }


    @Test
    @DisplayName("サーバー内部エラーが発生した場合500エラーが返されること")
    void signupWithInternalServerErrorReturnsInternalServerError() throws Exception {
        doThrow(new DomainLayerErrorException("内部サーバーエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.valueOf(500)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("内部サーバーエラーが発生しました"));
                
    }

    @Test
    @DisplayName("予期しない例外が発生した場合500エラーが返されること")
    void signupWithUnexpectedExceptionReturnsInternalServerError() throws Exception {
        doThrow(new ApplicationLayerException("予期しないエラー", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.valueOf(500)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("予期しないエラー"));
    }

    @Test
    @DisplayName("予期しない例外が発生した場合500エラーが返されること")
    void signupWithPasswordHashedMissReturnsInternalServerError() throws Exception {
        doThrow(new ApplicationLayerException("不明なエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.valueOf(500)))
                .when(mockUserRegistrationService).service(any(SignUpRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("不明なエラーが発生しました"));
    }

    @Test
    @DisplayName("リクエストボディが不正なJSON形式の場合400エラーが返されること")
    void signupWithInvalidJsonReturnsBadRequest() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Content-Typeが指定されていない場合415エラーが返されること")
    void signupWithoutContentTypeReturnsUnsupportedMediaType() throws Exception {
        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("UserRegistrationServiceが正しく呼び出されること")
    void signupCallsUserRegistrationServiceCorrectly() throws Exception {
        when(mockUserRegistrationService.service(any(SignUpRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

        verify(mockUserRegistrationService, times(1)).service(any(SignUpRequest.class));
    }
}
