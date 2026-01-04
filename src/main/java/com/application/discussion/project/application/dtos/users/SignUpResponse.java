package com.application.discussion.project.application.dtos.users;

import java.time.LocalDateTime;

/**
 * サインアップレスポンスDTO
 */
public class SignUpResponse {

    private String userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private String isActive;

    /**
     * デフォルトコンストラクタ
     */
    public SignUpResponse() {}

    /**
     * コンストラクタ
     * @param userId ユーザーID
     * @param username ユーザー名
     * @param email メールアドレス
     * @param createdAt 作成日時
     * @param isActive アカウントステータス
     */
    private SignUpResponse(String userId,String username, String email, LocalDateTime createdAt, String isActive) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public static SignUpResponse of(String userId,String username, String email, LocalDateTime createdAt, String isActive) {
        return new SignUpResponse(userId,username, email, createdAt, isActive);
    }

    /**
     * ユーザーIDを取得する
     * @return ユーザーID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定する
     * @param userId ユーザーID
     */
    public void setUserId(String userId) {
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
     * メールアドレスを取得する
     * @return メールアドレス
     */
    public String getEmail() {
        return email;
    }

    /**
     * メールアドレスを設定する
     * @param email メールアドレス
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 作成日時を取得する
     * @return 作成日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 作成日時を設定する
     * @param createdAt 作成日時
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * アカウントステータスを取得する
     * @return アカウントステータス
     */
    public String getisActive() {
        return isActive;
    }

    /**
     * アカウントステータスを設定する
     * @param isActive アカウントステータス
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
