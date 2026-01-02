package com.application.discussion.project.domain.valueobjects.users;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

/**
 * メールアドレスを表す値オブジェクト
 * メールアドレスの形式検証とドメイン制限を行う
 */
public class Email {
    private final String email;
    private final static Integer MAX_LENGTH = 255;
    private final static String EMAIL_BASIC_REGEX = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    private final static String EMAIL_DOMAIN_REGEX = ".*\\.(jp|com)$";

    private static final Pattern EMAIL_BASIC_PATTERN = Pattern.compile(EMAIL_BASIC_REGEX);

    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile(EMAIL_DOMAIN_REGEX);

    private static final Logger logger = LoggerFactory.getLogger(Email.class);

    /**
     * デフォルトコンストラクタ（使用禁止）
     */
    private Email() {
        this.email = "";
    }
    
    /**
     * メールアドレス値オブジェクトのプライベートコンストラクタ
     * 
     * @param email メールアドレス文字列
     */
    private Email(String email, Boolean isSkipValidation) {
        logger.info("Creating Email value object with email: {}", email);
        if(!isSkipValidation) {
            logger.info("Validating email address during Email value object creation {}",isSkipValidation);
            validate(email);
        }
        this.email = email;
        logger.info("Email value object created successfully with email: {}", this.email);
    }

    /**
     * メールアドレスの妥当性を検証する
     * 
     * @param email 検証対象のメールアドレス
     * @throws DomainLayerErrorException メールアドレスが不正な場合
     */
    private void validate(String email){
        logger.info("Validating email address: {}", email);
        if(StringUtils.isBlank(email)) {
            logger.error("Email address is blank");
            throw new DomainLayerErrorException(
                "メールアドレスの形式が正しくありません", 
                HttpStatus.BAD_REQUEST, 
                HttpStatusCode.valueOf(400)
            );
        }
        if(email.length() > MAX_LENGTH) {
            logger.error("Email address exceeds maximum length of {}: {}", MAX_LENGTH, email.length());
            throw new DomainLayerErrorException(
                "メールアドレスの形式が正しくありません", 
                HttpStatus.BAD_REQUEST, 
                HttpStatusCode.valueOf(400)
            );
        }

        

        if(!EMAIL_BASIC_PATTERN.matcher(email).matches() || !EMAIL_DOMAIN_PATTERN.matcher(email).matches()) {
            logger.error("Email address format is invalid: {}", email);
            throw new DomainLayerErrorException(
                "メールアドレスの形式が正しくありません", 
                HttpStatus.BAD_REQUEST, 
                HttpStatusCode.valueOf(400)
            );
        }
    }

    /**
     * メールアドレス値オブジェクトを生成する
     * 
     * @param email メールアドレス文字列
     * @return Email値オブジェクト
     */
    public static Email of(String email) {
        logger.info("Creating Email value object using of() with email: {}", email);
        return new Email(email, Boolean.FALSE);
    }

    /**
     * メールアドレス値オブジェクトを再構築する
     * 
     * @param email メールアドレス文字列
     * @return Email値オブジェクト
     */
    public static Email reBuild(String email) {
        logger.info("Rebuilding Email value object with email: {}", email);
        return new Email(email, Boolean.TRUE);
    }

    /**
     * メールアドレスの文字列表現を返す
     * 
     * @return メールアドレス文字列
     */
    public String value(){
        return this.email;
    }

    /**
     * メールアドレスが最大長以下かどうかを判定する
     * 
     * @return 最大長以下の場合true
     */
    public boolean isBelowMaxLength(){
        return this.email.length() <= MAX_LENGTH;
    }

    /**
     * メールアドレスの文字列表現を返す
     * 
     * @return メールアドレス文字列
     */
    @Override
    public String toString() {
        return this.email;
    }
}
