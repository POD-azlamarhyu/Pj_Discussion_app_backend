package com.application.discussion.project.domain.valueobjects.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.apache.commons.lang3.StringUtils;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

public class LoginId {
    private final String loginId;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 8;
    private final static String pattern = "^[a-zA-Z0-9._-]{8,255}$";

    /**
     * ログインID値オブジェクトを生成する
     * @param loginId
     */
    private LoginId(final String loginId) {
        this.validate(loginId);
        this.loginId = loginId;
    }
    /**
     * ログインIDの妥当性を検証する
     * 
     * @param loginId 検証対象のログインID
     * @throws DomainLayerErrorException ログインIDが不正な場合
     */
    private void validate(final String loginId) {
        if (StringUtils.isBlank(loginId)) {
            throw new DomainLayerErrorException("ログインIDは必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (loginId.length() > maxLength || loginId.length() < minLength) {
            throw new DomainLayerErrorException("ログインIDは" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (!loginId.matches(pattern)) {
            throw new DomainLayerErrorException("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
    /**
     * ログインID値オブジェクトを生成するファクトリメソッド
     * 
     * @param loginId ログインIDの文字列
     * @return LoginId ログインIDの値オブジェクト
     */
    public static LoginId of(String loginId) {
        return new LoginId(loginId);
    }
    /**
     * ログインIDの文字列を取得する
     * 
     * @return String ログインIDの文字列
     */
    public String value() {
        return this.loginId;
    }
    /**
     * ログインIDが最大長以下かを検証する
     * 
     * @return boolean 最大長以下であればtrue、そうでなければfalse
     */
    public boolean isBelowMaxLength() {
        return this.loginId.length() <= maxLength;
    }
    /**
     * ログインIDが最小長以上かを検証する
     * 
     * @return boolean 最小長以上であればtrue、そうでなければfalse
     */
    public boolean isAboveMinLength() {
        return this.loginId.length() >= minLength;
    }

}
