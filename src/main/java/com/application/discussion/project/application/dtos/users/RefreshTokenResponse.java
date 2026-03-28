package com.application.discussion.project.application.dtos.users;

/**
 * トークンリフレッシュレスポンスDTO
 */
public class RefreshTokenResponse {

    private String accessToken;

    public RefreshTokenResponse() {
    }

    private RefreshTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * ファクトリメソッド
     *
     * @param accessToken 新しいアクセストークン
     * @return RefreshTokenResponseオブジェクト
     */
    public static RefreshTokenResponse of(String accessToken) {
        return new RefreshTokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenResponse{accessToken='****'}";
    }
}
