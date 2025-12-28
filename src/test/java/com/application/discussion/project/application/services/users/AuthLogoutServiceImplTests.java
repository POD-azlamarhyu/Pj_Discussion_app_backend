package com.application.discussion.project.application.services.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthLogoutServiceImpl テストクラス")
public class AuthLogoutServiceImplTests {

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_COOKIE_NAME = "jwt";
    private static final String TEST_COOKIE_PATH = "/";
    private static final String EXPECTED_SUCCESS_MESSAGE = "ログアウトに成功しました";
    private static final String EXPECTED_ERROR_MESSAGE = "ログアウト処理中に認証情報が見つかりません。";
    private static final String UNKNOWN_USER = "不明なユーザ";

    @Mock
    private JWTUtils mockJwtUtils;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private SecurityContext mockSecurityContext;
    
    @Mock
    private JWTAuthUserDetails mockUserDetails;

    @InjectMocks
    private AuthLogoutServiceImpl authLogoutService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;
    private ResponseCookie mockClearCookie;

    @BeforeEach
    void setUp() {
        mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        
        mockClearCookie = ResponseCookie.from(TEST_COOKIE_NAME, "")
                .path(TEST_COOKIE_PATH)
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .build();
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    @DisplayName("認証済みユーザーが正常にログアウトできること")
    void serviceSuccessfullyLogsOutAuthenticatedUser() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(mockJwtUtils.getClearJwtCookie()).thenReturn(mockClearCookie);

        LogoutResponseDTO actualResponse = authLogoutService.service();

        assertNotNull(actualResponse);
        assertNotNull(actualResponse.getLogoutResponse());
        assertEquals(EXPECTED_SUCCESS_MESSAGE, actualResponse.getLogoutResponse().getMessage());
        assertTrue(actualResponse.getLogoutResponse().getSuccess());
        assertNotNull(actualResponse.getJwtCookie());
        assertEquals(TEST_COOKIE_NAME, actualResponse.getJwtCookie().getName());
        assertEquals("", actualResponse.getJwtCookie().getValue());
        assertEquals(0L, actualResponse.getJwtCookie().getMaxAge().getSeconds());
        
        verify(mockJwtUtils, times(1)).getClearJwtCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
    }

    @Test
    @DisplayName("認証情報がnullの場合に例外がスローされること")
    void serviceThrowsExceptionWhenAuthenticationIsNull() {
        when(mockSecurityContext.getAuthentication()).thenReturn(null);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> authLogoutService.service()
        );

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        
        verify(mockJwtUtils, never()).getClearJwtCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, never());
    }

    @Test
    @DisplayName("未認証ユーザーの場合に例外がスローされること")
    void serviceThrowsExceptionWhenUserIsNotAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> authLogoutService.service()
        );

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        
        verify(mockJwtUtils, never()).getClearJwtCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, never());
    }

    @Test
    @DisplayName("SecurityContextがクリアされること")
    void serviceSuccessfullyClearsSecurityContext() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockJwtUtils.getClearJwtCookie()).thenReturn(mockClearCookie);

        authLogoutService.service();

        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
    }

    @Test
    @DisplayName("JWTUtilsのgetClearJwtCookieが正しく呼び出されること")
    void serviceCallsGetClearJwtCookieCorrectly() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockJwtUtils.getClearJwtCookie()).thenReturn(mockClearCookie);

        authLogoutService.service();

        verify(mockJwtUtils, times(1)).getClearJwtCookie();
    }

    @Test
    @DisplayName("返されるLogoutResponseDTOのtoStringメソッドが正常に動作すること")
    void serviceReturnsValidLogoutResponseDTOWithToString() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockJwtUtils.getClearJwtCookie()).thenReturn(mockClearCookie);

        LogoutResponseDTO actualResponse = authLogoutService.service();
        String actualToString = actualResponse.toString();

        assertNotNull(actualToString);
        assertTrue(actualToString.contains("LogoutResponseDTO"));
        assertTrue(actualToString.contains("logoutResponse"));
        assertTrue(actualToString.contains("jwtCookie"));
    }

    @Test
    @DisplayName("認証情報からユーザー名が正しく取得されること")
    void serviceExtractsUsernameCorrectlyFromAuthentication() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockJwtUtils.getClearJwtCookie()).thenReturn(mockClearCookie);

        authLogoutService.service();

        verify(mockUserDetails, times(1)).getUsername();
    }

    @Test
    @DisplayName("複数回ログアウト処理を実行しても正常に動作すること")
    void serviceWorksCorrectlyWhenCalledMultipleTimes() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockJwtUtils.getClearJwtCookie()).thenReturn(mockClearCookie);

        LogoutResponseDTO firstResponse = authLogoutService.service();
        LogoutResponseDTO secondResponse = authLogoutService.service();

        assertNotNull(firstResponse);
        assertNotNull(secondResponse);
        assertEquals(firstResponse.getLogoutResponse().getMessage(), secondResponse.getLogoutResponse().getMessage());
        verify(mockJwtUtils, times(2)).getClearJwtCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, times(2));
    }

    @Test
    @DisplayName("PrincipalがJWTAuthUserDetails型でない場合に例外がスローされること")
    void serviceThrowsExceptionWhenPrincipalIsNotJWTAuthUserDetails() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn("anonymousUser");
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> authLogoutService.service()
        );

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        
        verify(mockJwtUtils, never()).getClearJwtCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, never());
    }

    @Test
    @DisplayName("Principalがnullの場合に例外がスローされること")
    void serviceThrowsExceptionWhenPrincipalIsNull() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(null);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> authLogoutService.service()
        );

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        
        verify(mockJwtUtils, never()).getClearJwtCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, never());
    }
}
