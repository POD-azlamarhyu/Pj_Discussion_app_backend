package com.application.discussion.project.application.dtos.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

@DisplayName("AuthRefreshTokenServiceResultのテスト")
public class AuthRefreshTokenServiceResultTests {

    private static final String TEST_ACCESS_TOKEN = "new-access-token";
    private static final String TEST_COOKIE_NAME = "refreshToken";
    private static final String TEST_COOKIE_VALUE = "new-refresh-token";

    @Test
    @DisplayName("ofメソッドでレスポンスDTOとCookieを保持できる")
    public void ofReturnsResultWithCorrectFields() {
        RefreshTokenResponse expectedResponse = RefreshTokenResponse.of(TEST_ACCESS_TOKEN);
        ResponseCookie expectedCookie = ResponseCookie.from(TEST_COOKIE_NAME, TEST_COOKIE_VALUE)
            .httpOnly(true)
            .path("/v1/auth")
            .maxAge(3600)
            .build();

        AuthRefreshTokenServiceResult actualResult = AuthRefreshTokenServiceResult.of(expectedResponse, expectedCookie);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getRefreshTokenResponse()).isEqualTo(expectedResponse);
        assertThat(actualResult.getRefreshTokenCookie()).isEqualTo(expectedCookie);
    }
}
