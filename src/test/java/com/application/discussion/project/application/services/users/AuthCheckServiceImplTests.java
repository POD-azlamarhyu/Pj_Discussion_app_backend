package com.application.discussion.project.application.services.users;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.AuthCheckResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthCheckServiceImpl テストクラス")
public class AuthCheckServiceImplTests {

    private static final String TEST_USERNAME = "testuser";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String EXPECTED_AUTHENTICATED_MESSAGE = "認証済みです";
    private static final String EXPECTED_NOT_AUTHENTICATED_MESSAGE = "認証されていません";

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private SecurityContext mockSecurityContext;

    @Mock
    private JWTAuthUserDetails mockUserDetails;

    @InjectMocks
    private AuthCheckServiceImpl authCheckService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    void setUp() {
        mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    @DisplayName("認証済みユーザーで正常にAuthCheckResponseが返却されること")
    public void serviceReturnsAuthCheckResponseWhenAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(mockUserDetails.getUserId()).thenReturn(TEST_USER_ID);

        AuthCheckResponse actualResponse = authCheckService.service();

        assertNotNull(actualResponse);
        assertEquals(TEST_USERNAME, actualResponse.getUsername());
        assertTrue(actualResponse.getIsAuthenticated());
        assertEquals(EXPECTED_AUTHENTICATED_MESSAGE, actualResponse.getMessage());
    }

    @Test
    @DisplayName("認証情報がnullの場合に例外がスローされること")
    public void serviceThrowsExceptionWhenAuthenticationIsNull() {
        when(mockSecurityContext.getAuthentication()).thenReturn(null);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException actualException = assertThrows(
            ApplicationLayerException.class,
            () -> authCheckService.service()
        );

        assertEquals(EXPECTED_NOT_AUTHENTICATED_MESSAGE, actualException.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatus());
    }

    @Test
    @DisplayName("未認証の場合に例外がスローされること")
    public void serviceThrowsExceptionWhenUserIsNotAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException actualException = assertThrows(
            ApplicationLayerException.class,
            () -> authCheckService.service()
        );

        assertEquals(EXPECTED_NOT_AUTHENTICATED_MESSAGE, actualException.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatus());
    }

    @Test
    @DisplayName("PrincipalがJWTAuthUserDetails型でない場合に例外がスローされること")
    public void serviceThrowsExceptionWhenPrincipalIsNotJWTAuthUserDetails() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn("anonymousUser");
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException actualException = assertThrows(
            ApplicationLayerException.class,
            () -> authCheckService.service()
        );

        assertEquals(EXPECTED_NOT_AUTHENTICATED_MESSAGE, actualException.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatus());
    }

    @Test
    @DisplayName("Principalがnullの場合に例外がスローされること")
    public void serviceThrowsExceptionWhenPrincipalIsNull() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(null);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        ApplicationLayerException actualException = assertThrows(
            ApplicationLayerException.class,
            () -> authCheckService.service()
        );

        assertEquals(EXPECTED_NOT_AUTHENTICATED_MESSAGE, actualException.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatus());
    }

    @Test
    @DisplayName("ユーザー名がJWTAuthUserDetailsから正しく取得されること")
    public void serviceExtractsUsernameCorrectlyFromUserDetails() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(mockUserDetails.getUserId()).thenReturn(TEST_USER_ID);

        authCheckService.service();

        verify(mockUserDetails, times(2)).getUsername();
    }

    @Test
    @DisplayName("認証成功時のメッセージが正しいこと")
    public void serviceReturnsCorrectMessageWhenAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(mockUserDetails.getUserId()).thenReturn(TEST_USER_ID);

        AuthCheckResponse actualResponse = authCheckService.service();

        assertEquals(EXPECTED_AUTHENTICATED_MESSAGE, actualResponse.getMessage());
    }

    @Test
    @DisplayName("認証成功時にisAuthenticatedがtrueであること")
    public void serviceReturnsIsAuthenticatedTrueWhenAuthenticated() {
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(mockUserDetails.getUserId()).thenReturn(TEST_USER_ID);

        AuthCheckResponse actualResponse = authCheckService.service();

        assertTrue(actualResponse.getIsAuthenticated());
    }
}
