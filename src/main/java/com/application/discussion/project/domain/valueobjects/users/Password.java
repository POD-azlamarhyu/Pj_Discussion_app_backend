package com.application.discussion.project.domain.valueobjects.users;


import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class Password {
    private final String password;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 10;
    private final static String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{10,255}$";

    /**
     * パスワード値オブジェクトを生成する
     * @param password
     */
    private Password(String password) {
        this.validate(password);
        this.password = password;
    }

    /**
     * パスワードの妥当性を検証する
     * 
     * @param password 検証対象のパスワード
     * @throws DomainLayerErrorException パスワードが不正な場合
     */
    private void validate(String password) {
        if (password == null || password.isEmpty()) {
            throw new DomainLayerErrorException("パスワードは必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (password.length() > maxLength || password.length() < minLength) {
            throw new DomainLayerErrorException("パスワードは" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (!password.matches(pattern)) {
            throw new DomainLayerErrorException("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
    /**
     * パスワード値オブジェクトを生成するファクトリメソッド
     * 
     * @param password パスワードの文字列
     * @return Password パスワードの値オブジェクト
     */
    public static Password of(String password) {
        return new Password(password);
    }
    /**
     * パスワードの文字列を取得する
     * 
     * @return String パスワードの文字列
     */
    public String value() {
        return this.password;
    }
    /**
     * パスワードが最大長以下かを検証する
     * 
     * @return boolean 最大長以下であればtrue、そうでなければfalse
     */
    public boolean isBelowMaxLength() {
        return this.password.length() <= maxLength;
    }

    /**
     * パスワードが最小長以上かを検証する
     * 
     * @return boolean 最小長以上であればtrue、そうでなければfalse
     */
    public boolean isAboveMinLength() {
        return this.password.length() >= minLength;
    }
}
