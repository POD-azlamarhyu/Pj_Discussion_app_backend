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

    private static final Logger logger = LoggerFactory.getLogger(Email.class);
    
    private Email(String email){
        logger.info("Creating Email value object with email: {}", email);
        this.validate(email);
        this.email = email;
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

        Pattern emailBasicPattern = Pattern.compile("/^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/");

        Pattern emailBannedDomainPattern = Pattern.compile(".*\\.(jp|com)$");

        if(!emailBasicPattern.matcher(email).matches() || !emailBannedDomainPattern.matcher(email).matches()) {
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
        return new Email(email);
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
}
