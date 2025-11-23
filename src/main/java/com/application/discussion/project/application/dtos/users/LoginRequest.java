package com.application.discussion.project.application.dtos.users;


public class LoginRequest {
    private String emailOrLoginId;
    private String password;

    /**
     * デフォルトコンストラクタ
     */
    public LoginRequest() {
    }

    /**
     * コンストラクタ
     * @param emailOrLoginId メールアドレスまたはログインID
     * @param password パスワード
     */
    public LoginRequest(String emailOrLoginId, String password) {
        this.emailOrLoginId = emailOrLoginId;
        this.password = password;
    }

    /**
     * メールアドレスまたはログインIDを取得する
     * @return メールアドレスまたはログインID
     */
    public String getEmailOrLoginId() {
        return emailOrLoginId;
    }

    /**
     * メールアドレスまたはログインIDを設定する
     * @param emailOrLoginId メールアドレスまたはログインID
     */
    public void setEmailOrLoginId(String emailOrLoginId) {
        this.emailOrLoginId = emailOrLoginId;
    }

    /**
     * パスワードを取得する
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定する
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;  
    }
}
