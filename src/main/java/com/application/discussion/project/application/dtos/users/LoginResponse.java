package com.application.discussion.project.application.dtos.users;

import java.util.List;
import java.util.UUID;

public class LoginResponse {
    private UUID userId;
    private String username;
    private List<String> roles;
    private String accessToken;

    /**
     * デフォルトコンストラクタ
     */
    public LoginResponse() {
    }

    /**
     * コンストラクタ
     * @param userId ユーザーID
     * @param username ユーザー名
     * @param roles ユーザーロールのリスト
     * @param accessToken アクセストークン
     */
    public LoginResponse(UUID userId, String username, List<String> roles, String accessToken) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.accessToken = accessToken;
    }

    /**
     * ユーザーIDを取得する
     * @return ユーザーID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定する
     * @param userId ユーザーID
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * ユーザー名を取得する
     * @return ユーザー名
     */
    public String getUsername() {
        return username;
    }

    /**
     * ユーザー名を設定する
     * @param username ユーザー名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * ユーザーロールのリストを取得する
     * @return ユーザーロールのリスト
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * ユーザーロールのリストを設定する
     * @param roles ユーザーロールのリスト
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * アクセストークンを取得する
     * @return アクセストークン
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * アクセストークンを設定する
     * @param accessToken アクセストークン
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * ビルダーパターンのためのビルダーメソッド
     * @return Builderインスタンス
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * LoginResponseのビルダークラス
     */
    public static class Builder {
        private UUID userId;
        private String username;
        private List<String> roles;
        private String accessToken;

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(userId, username, roles, accessToken);
        }
    }
}
