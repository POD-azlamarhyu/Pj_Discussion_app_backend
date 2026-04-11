package com.application.discussion.project.application.services.users;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTUtils;
import com.application.discussion.project.domain.entities.users.RefreshToken;
import com.application.discussion.project.domain.repositories.users.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthLoginServiceのテスト")
public class AuthLoginServiceImplTests {

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_PASSWORD = "pAssword12345";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_ROLE = "ROLE_USER";
    private static final String TEST_ACCESS_TOKEN = "access-token";
    private static final String TEST_REFRESH_TOKEN = "refresh-token";
    private static final String TEST_REFRESH_TOKEN_HASH = "refresh-token-hash";

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private JWTUtils mockJwtUtils;

    @Mock
    private RefreshTokenRepository mockRefreshTokenRepository;

    @Mock
    private Authentication mockAuthentication;

    @InjectMocks
    private AuthLoginService authLoginService;

    @Test
    @DisplayName("ログイン成功時にアクセストークンとリフレッシュトークンCookieを返す")
    public void serviceReturnsAccessTokenInBodyAndRefreshCookieWhenLoginSucceeds() {
        LoginRequest targetRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
        JWTAuthUserDetails mockUserDetails = new JWTAuthUserDetails(
            TEST_USER_ID,
            TEST_USERNAME,
            TEST_EMAIL,
            TEST_PASSWORD,
            "login-id",
            false,
            true,
            List.of(new SimpleGrantedAuthority(TEST_ROLE))
        );
        ResponseCookie expectedRefreshCookie = ResponseCookie.from("refreshToken", TEST_REFRESH_TOKEN)
            .httpOnly(true)
            .path("/v1/auth")
            .maxAge(3600)
            .build();

        when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockJwtUtils.generateToken(mockUserDetails)).thenReturn(TEST_ACCESS_TOKEN);
        when(mockJwtUtils.generateRefreshToken(mockUserDetails)).thenReturn(TEST_REFRESH_TOKEN);
        when(mockJwtUtils.hashToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_REFRESH_TOKEN_HASH);
        when(mockJwtUtils.getRefreshTokenExpirationMs()).thenReturn(3600000L);
        when(mockJwtUtils.generateRefreshTokenCookie(TEST_REFRESH_TOKEN)).thenReturn(expectedRefreshCookie);
        when(mockRefreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<LoginResponse> actualResponse = authLoginService.service(targetRequest);
        LoginResponse actualBody = actualResponse.getBody();

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getHeaders().get(HttpHeaders.SET_COOKIE))
            .containsExactly(expectedRefreshCookie.toString());
        assertThat(actualBody).isNotNull();
        if (actualBody == null) {
            return;
        }
        assertThat(actualBody.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(actualBody.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(actualBody.getRoles()).containsExactly(TEST_ROLE);
        assertThat(actualBody.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
        verify(mockRefreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("認証時にAuthenticationExceptionが発生した場合は例外をスローする")
    public void serviceThrowsExceptionWhenAuthenticationManagerThrowsException() {
        LoginRequest targetRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

        when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new AuthenticationException("Invalid credentials") {});

        assertThatThrownBy(() -> authLoginService.service(targetRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("認証エラーが発生しました")
            .extracting("status")
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        verify(mockRefreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("認証結果がnullの場合は例外をスローする")
    public void serviceThrowsExceptionWhenAuthenticationResultIsNull() {
        LoginRequest targetRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

        when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(null);

        assertThatThrownBy(() -> authLoginService.service(targetRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("認証に失敗しました")
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("認証済みフラグがfalseの場合は例外をスローする")
    public void serviceThrowsExceptionWhenAuthenticationIsNotAuthenticated() {
        LoginRequest targetRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

        when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);

        assertThatThrownBy(() -> authLoginService.service(targetRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessage("認証に失敗しました")
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
