package com.application.discussion.project.application.services.users;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.application.discussion.project.domain.repositories.users.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthLogoutServiceImplのテスト")
public class AuthLogoutServiceImplTests {

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "testuser";
    private static final String EXPECTED_SUCCESS_MESSAGE = "ログアウトに成功しました";
    private static final String EXPECTED_ERROR_MESSAGE = "ログアウト処理中に認証情報が見つかりません。";

    @Mock
    private JWTUtils mockJwtUtils;

    @Mock
    private RefreshTokenRepository mockRefreshTokenRepository;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private SecurityContext mockSecurityContext;

    @Mock
    private JWTAuthUserDetails mockUserDetails;

    @InjectMocks
    private AuthLogoutServiceImpl authLogoutService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    public void setUp() {
        mockedSecurityContextHolder = org.mockito.Mockito.mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    public void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    @DisplayName("認証済みユーザーは正常にログアウトできる")
    public void serviceReturnsLogoutResponseAndClearsRefreshCookieWhenAuthenticated() {
        ResponseCookie expectedClearRefreshCookie = ResponseCookie.from("refreshToken", "")
            .path("/v1/auth")
            .maxAge(0)
            .httpOnly(true)
            .build();

        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(TEST_USERNAME);
        when(mockUserDetails.getUserId()).thenReturn(TEST_USER_ID);
        when(mockJwtUtils.getClearRefreshTokenCookie()).thenReturn(expectedClearRefreshCookie);

        LogoutResponseDTO actualResponse = authLogoutService.service();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getLogoutResponse().getMessage()).isEqualTo(EXPECTED_SUCCESS_MESSAGE);
        assertThat(actualResponse.getLogoutResponse().getSuccess()).isTrue();
        assertThat(actualResponse.getJwtCookie()).isEqualTo(expectedClearRefreshCookie);
        verify(mockRefreshTokenRepository, times(1)).revokeAllByUserId(TEST_USER_ID);
        verify(mockJwtUtils, times(1)).getClearRefreshTokenCookie();
        mockedSecurityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
    }

    @Test
    @DisplayName("認証情報がnullの場合は例外をスローする")
    public void serviceThrowsExceptionWhenAuthenticationIsNull() {
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(null);

        assertThatThrownBy(() -> authLogoutService.service())
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage(EXPECTED_ERROR_MESSAGE)
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository, never()).revokeAllByUserId(org.mockito.ArgumentMatchers.any());
        verify(mockJwtUtils, never()).getClearRefreshTokenCookie();
    }

    @Test
    @DisplayName("未認証の場合は例外をスローする")
    public void serviceThrowsExceptionWhenAuthenticationIsNotAuthenticated() {
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);

        assertThatThrownBy(() -> authLogoutService.service())
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage(EXPECTED_ERROR_MESSAGE)
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository, never()).revokeAllByUserId(org.mockito.ArgumentMatchers.any());
        verify(mockJwtUtils, never()).getClearRefreshTokenCookie();
    }

    @Test
    @DisplayName("PrincipalがJWTAuthUserDetails型でない場合は例外をスローする")
    public void serviceThrowsExceptionWhenPrincipalTypeIsInvalid() {
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn("anonymousUser");

        assertThatThrownBy(() -> authLogoutService.service())
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage(EXPECTED_ERROR_MESSAGE)
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository, never()).revokeAllByUserId(org.mockito.ArgumentMatchers.any());
        verify(mockJwtUtils, never()).getClearRefreshTokenCookie();
    }
}
