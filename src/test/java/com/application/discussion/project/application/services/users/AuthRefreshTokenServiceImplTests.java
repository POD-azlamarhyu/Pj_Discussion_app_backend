package com.application.discussion.project.application.services.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.AuthRefreshTokenServiceResult;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTAuthUserDetailsService;
import com.application.discussion.project.application.services.security.JWTUtils;
import com.application.discussion.project.domain.entities.users.RefreshToken;
import com.application.discussion.project.domain.repositories.users.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthRefreshTokenServiceImplのテスト")
public class AuthRefreshTokenServiceImplTests {

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final UUID TEST_TOKEN_ID = UUID.randomUUID();
    private static final String TEST_REFRESH_TOKEN = "refresh-token-value";
    private static final String TEST_OLD_TOKEN_HASH = "old-token-hash";
    private static final String TEST_NEW_TOKEN_HASH = "new-token-hash";
    private static final String TEST_LOGIN_ID = "test-login-id";
    private static final String TEST_NEW_ACCESS_TOKEN = "new-access-token";
    private static final String TEST_NEW_REFRESH_TOKEN = "new-refresh-token";

    @Mock
    private JWTUtils mockJwtUtils;

    @Mock
    private JWTAuthUserDetailsService mockJwtAuthUserDetailsService;

    @Mock
    private RefreshTokenRepository mockRefreshTokenRepository;

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private AuthRefreshTokenServiceImpl authRefreshTokenService;

    @Test
    @DisplayName("有効なリフレッシュトークンで新しいアクセストークンとCookieを返す")
    public void serviceReturnsNewAccessTokenAndRefreshCookie() {
        RefreshToken mockStoredToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_OLD_TOKEN_HASH,
            LocalDateTime.now().plusMinutes(30),
            false,
            false,
            LocalDateTime.now().minusMinutes(1)
        );
        JWTAuthUserDetails mockUserDetails = new JWTAuthUserDetails(
            TEST_USER_ID,
            "test-user",
            "test@example.com",
            "dummy-password",
            TEST_LOGIN_ID,
            false,
            true,
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        ResponseCookie expectedCookie = ResponseCookie.from("refreshToken", TEST_NEW_REFRESH_TOKEN)
            .httpOnly(true)
            .path("/v1/auth")
            .maxAge(3600)
            .build();

        when(mockJwtUtils.getRefreshTokenFromCookies(mockRequest)).thenReturn(TEST_REFRESH_TOKEN);
        when(mockJwtUtils.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(mockJwtUtils.hashToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_OLD_TOKEN_HASH);
        when(mockRefreshTokenRepository.findByTokenHash(TEST_OLD_TOKEN_HASH)).thenReturn(Optional.of(mockStoredToken));
        when(mockJwtUtils.getEmailOrLoginIdFromRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_LOGIN_ID);
        when(mockJwtAuthUserDetailsService.loadUserByUsername(TEST_LOGIN_ID)).thenReturn(mockUserDetails);
        when(mockJwtUtils.generateToken(mockUserDetails)).thenReturn(TEST_NEW_ACCESS_TOKEN);
        when(mockJwtUtils.generateRefreshToken(mockUserDetails)).thenReturn(TEST_NEW_REFRESH_TOKEN);
        when(mockJwtUtils.hashToken(TEST_NEW_REFRESH_TOKEN)).thenReturn(TEST_NEW_TOKEN_HASH);
        when(mockJwtUtils.getRefreshTokenExpirationMs()).thenReturn(3600000L);
        when(mockJwtUtils.generateRefreshTokenCookie(TEST_NEW_REFRESH_TOKEN)).thenReturn(expectedCookie);

        AuthRefreshTokenServiceResult actualResult = authRefreshTokenService.service(mockRequest);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getRefreshTokenResponse()).isNotNull();
        assertThat(actualResult.getRefreshTokenResponse().getAccessToken()).isEqualTo(TEST_NEW_ACCESS_TOKEN);
        assertThat(actualResult.getRefreshTokenCookie()).isEqualTo(expectedCookie);
        verify(mockRefreshTokenRepository).markAsUsed(TEST_TOKEN_ID);
        verify(mockRefreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Cookieにリフレッシュトークンがない場合は例外をスローする")
    public void serviceThrowsExceptionWhenCookieIsMissing() {
        when(mockJwtUtils.getRefreshTokenFromCookies(mockRequest)).thenReturn(null);

        assertThatThrownBy(() -> authRefreshTokenService.service(mockRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("リフレッシュトークンが見つかりません")
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository, never()).findByTokenHash(any());
    }

    @Test
    @DisplayName("DBにリフレッシュトークンが存在しない場合は例外をスローする")
    public void serviceThrowsExceptionWhenTokenNotFoundInDatabase() {
        when(mockJwtUtils.getRefreshTokenFromCookies(mockRequest)).thenReturn(TEST_REFRESH_TOKEN);
        when(mockJwtUtils.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(mockJwtUtils.hashToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_OLD_TOKEN_HASH);
        when(mockRefreshTokenRepository.findByTokenHash(TEST_OLD_TOKEN_HASH)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authRefreshTokenService.service(mockRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("リフレッシュトークンが無効です")
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository, never()).markAsUsed(any());
    }

    @Test
    @DisplayName("使用済みトークンの再利用時は全トークン失効して例外をスローする")
    public void serviceRevokesAllTokensWhenReusedTokenDetected() {
        RefreshToken usedToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_OLD_TOKEN_HASH,
            LocalDateTime.now().plusMinutes(10),
            true,
            false,
            LocalDateTime.now().minusMinutes(1)
        );

        when(mockJwtUtils.getRefreshTokenFromCookies(mockRequest)).thenReturn(TEST_REFRESH_TOKEN);
        when(mockJwtUtils.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(mockJwtUtils.hashToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_OLD_TOKEN_HASH);
        when(mockRefreshTokenRepository.findByTokenHash(TEST_OLD_TOKEN_HASH)).thenReturn(Optional.of(usedToken));

        assertThatThrownBy(() -> authRefreshTokenService.service(mockRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("リフレッシュトークンが無効です")
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository).revokeAllByUserId(TEST_USER_ID);
        verify(mockRefreshTokenRepository, never()).markAsUsed(any());
    }

    @Test
    @DisplayName("期限切れまたは失効トークンの場合は例外をスローする")
    public void serviceThrowsExceptionWhenStoredTokenIsInvalid() {
        RefreshToken expiredToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_OLD_TOKEN_HASH,
            LocalDateTime.now().minusMinutes(1),
            false,
            false,
            LocalDateTime.now().minusHours(1)
        );

        when(mockJwtUtils.getRefreshTokenFromCookies(mockRequest)).thenReturn(TEST_REFRESH_TOKEN);
        when(mockJwtUtils.validateRefreshToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(mockJwtUtils.hashToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_OLD_TOKEN_HASH);
        when(mockRefreshTokenRepository.findByTokenHash(TEST_OLD_TOKEN_HASH)).thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> authRefreshTokenService.service(mockRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("リフレッシュトークンが無効です")
            .extracting("status")
            .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(mockRefreshTokenRepository, never()).markAsUsed(any());
        verify(mockRefreshTokenRepository, never()).save(any(RefreshToken.class));
    }
}
