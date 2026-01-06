package com.application.discussion.project.application.dtos.users;

public class SignUpRequest {

    private String email;
    private String password;
    private String username;

    /**
     * デフォルトコンストラクタ
     */
    public SignUpRequest() {}

    /**
     * コンストラクタ
     * @param email メールアドレス
     * @param password パスワード
     * @param username ユーザー名
     */
    public SignUpRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
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
}
