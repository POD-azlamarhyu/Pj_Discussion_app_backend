package com.application.discussion.project.domain.valueobjects.users;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Password {
    private final String password;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 10;
    private final static String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{10,255}$";
    private final static String HASHED_PASSWORD = "*******************";
    private final static String BCRYPT_PATTERN = "^\\$2[ayb]\\$.{56}$";

    private static final Logger logger = LoggerFactory.getLogger(Password.class);

    /**
     * デフォルトコンストラクタ（使用禁止）
     */
    private Password() {
        this.password = "";
    }

    /**
     * パスワード値オブジェクトを生成する
     * @param password
     */
    private Password(String password, Boolean isSkipValidation) {
        logger.info("Creating Password value object with password {}: ",password != null ? "********" : null);
        if (!isSkipValidation) {
            logger.info("Validating password during Password object creation {}",isSkipValidation);
            validate(password);
        }
        this.password = password;
        logger.info("Password value object created successfully {}",password != null ? "********" : null);
    }

    /**
     * パスワードの妥当性を検証する
     * 
     * @param password 検証対象のパスワード
     * @throws DomainLayerErrorException パスワードが不正な場合
     */
    private void validate(String password) {
        logger.info("Validating password");
        if (password == null || password.isEmpty()) {
            logger.error("Password validation failed: password is null or empty");
            throw new DomainLayerErrorException("パスワードは必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (password.length() > maxLength || password.length() < minLength) {
            logger.error("Password validation failed: password length is out of bounds");
            throw new DomainLayerErrorException("パスワードは" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (!password.matches(pattern)) {
            logger.error("Password validation failed: password does not match required pattern");
            throw new DomainLayerErrorException("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
    /**
     * パスワード値オブジェクトを生成するファクトリメソッド
     * 
     * @param password パスワードの文字列
     * @return Password パスワードの値オブジェクト
     */
    public static Password of(final String password) {
        logger.info("Factory method called to create Password value object");
        return new Password(password, Boolean.FALSE);
    }

    /**
     * バリデーションをスキップしてパスワード値オブジェクトを再構築するファクトリメソッド
     * 
     * @param password パスワードの文字列
     * @return Password パスワードの値オブジェクト
     */
    public static Password reBuild(final String password) {
        logger.info("Rebuilding Password value object without validation");
        return new Password(password, Boolean.TRUE);
    }

    /**
     * ハッシュ化されたパスワード値オブジェクトを再構築するファクトリメソッド
     * 
     * @param hashedPassword ハッシュ化されたパスワードの文字列
     * @return Password パスワードの値オブジェクト
     */
    public static Password reBuildHashed(final String hashedPassword) {
        logger.info("Rebuilding Password value object with hashed password");
        return new Password(hashedPassword, Boolean.TRUE);
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

    /**
     * パスワードがハッシュ化されているかを判定する
     * BCryptのパターン、またはマスク文字列に一致する場合はハッシュ化されていると判定する
     * 
     * @param password 検証対象のパスワード文字列
     * @return boolean ハッシュ化されている場合はtrue、そうでなければfalse
     */
    public Boolean isHashed(String rawPassword, PasswordEncoder passwordEncoder) {
        logger.info("Checking if password is hashed");
        
        if (StringUtils.isBlank(this.password) || StringUtils.isBlank(rawPassword)) {
            logger.info("Password is null or empty, returning false");
            return false;
        }

        logger.debug("Comparing raw password with hashed password using PasswordEncoder {}, {}",rawPassword, this.password);

        Boolean result = passwordEncoder.matches(rawPassword, this.password);
        
        logger.info("Password hashed check result: {}", result);
        
        return result;
    }
}
