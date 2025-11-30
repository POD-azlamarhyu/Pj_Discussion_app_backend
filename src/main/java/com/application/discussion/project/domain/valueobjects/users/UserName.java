package com.application.discussion.project.domain.valueobjects.users;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class UserName {
    private final String userName;
    private final static Integer maxLength = 255;
    private final static Integer minLength = 1;

    private static final Logger logger = LoggerFactory.getLogger(UserName.class);

    /**
     * ユーザー名値オブジェクトを生成する
     * @param userName
     */
    private UserName(final String userName) {
        logger.info("Creating UserName with value: {}", userName);
        this.validate(userName);
        this.userName = userName;
    }

    /**
     * ユーザー名の妥当性を検証する
     * 
     * @param userName 検証対象のユーザー名
     * @throws DomainLayerErrorException ユーザー名が不正な場合
     */
    private void validate(final String userName) {
        logger.info("Validating UserName: {}", userName);
        if (userName == null || userName.isEmpty()) {
            logger.error("Validation failed: UserName is null or empty");
            throw new DomainLayerErrorException("ユーザー名は必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (userName.length() > maxLength || userName.length() < minLength) {
            logger.error("Validation failed: UserName length is out of bounds ({}-{} characters)", minLength, maxLength);
            throw new DomainLayerErrorException("ユーザー名は" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }

    /**
     * ユーザー名値オブジェクトを生成するファクトリメソッド
     * 
     * @param userName ユーザー名の文字列
     * @return UserName ユーザー名の値オブジェクト
     */
    public static UserName of(final String userName) {
        logger.info("Factory method called to create UserName with value: {}", userName);
        return new UserName(userName);
    }

    /**
     * ユーザー名の文字列を取得する
     * 
     * @return String ユーザー名の文字列
     */
    public String value() {
        return this.userName;
    }

    /**
     * ユーザー名が最大長以下かを検証する
     * 
     * @return boolean 最大長以下であればtrue、そうでなければfalse
     */
    public boolean isBelowMaxLength() {
        return this.userName.length() <= maxLength;
    }
    
    /**
     * ユーザー名が最小長以上かを検証する
     * 
     * @return boolean 最小長以上であればtrue、そうでなければfalse
     */
    public boolean isAboveMinLength() {
        return this.userName.length() >= minLength;
    }
}
