package com.application.discussion.project.application.dtos.users;

import org.springframework.http.ResponseCookie;

/**
 * リフレッシュトークンサービスの結果を保持するDTO
 */
public class AuthRefreshTokenServiceResult {

    private final RefreshTokenResponse refreshTokenResponse;
    private final ResponseCookie refreshTokenCookie;

    private AuthRefreshTokenServiceResult(
        RefreshTokenResponse refreshTokenResponse,
        ResponseCookie refreshTokenCookie
    ) {
        this.refreshTokenResponse = refreshTokenResponse;
        this.refreshTokenCookie = refreshTokenCookie;
    }

    /**
     * ファクトリメソッド
     *
     * @param refreshTokenResponse レスポンスDTO
     * @param refreshTokenCookie 新しいリフレッシュトークンCookie
     * @return AuthRefreshTokenServiceResultオブジェクト
     */
    public static AuthRefreshTokenServiceResult of(
        RefreshTokenResponse refreshTokenResponse,
        ResponseCookie refreshTokenCookie
    ) {
        return new AuthRefreshTokenServiceResult(refreshTokenResponse, refreshTokenCookie);
    }

    public RefreshTokenResponse getRefreshTokenResponse() {
        return refreshTokenResponse;
    }

    public ResponseCookie getRefreshTokenCookie() {
        return refreshTokenCookie;
    }
}
