package com.application.discussion.project.domain.valueobjects.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

public class EmailOrLoginId {

    private final String emailOrLoginId;
    private final static String loginIdPattern = "^[a-zA-Z0-9.!#*+$%&_-]{8,255}$";
    private final static Integer maxLength = 255;
    private final static Integer minLength = 10;
    private final static String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    private final static String emailBannedDomainPattern = ".*\\.(jp|com)$";
    private final static Pattern loginIdPatternCompiled = Pattern.compile(loginIdPattern);
    private final static Pattern emailPatternCompiled = Pattern.compile(emailPattern);
    private final static Pattern emailBannedDomainPatternCompiled = Pattern.compile(emailBannedDomainPattern);

    private static final Logger logger = LoggerFactory.getLogger(EmailOrLoginId.class);

    /**
     * メールアドレスまたはログインID値オブジェクトを生成する
     * @param emailOrLoginId
     */
    private EmailOrLoginId(final String emailOrLoginId) {
        logger.info("Creating EmailOrLoginId with value: {}", emailOrLoginId);
        if (StringUtils.isBlank(emailOrLoginId)) {
            logger.error("emailOrLoginId is blank");
            throw new DomainLayerErrorException("メールアドレスまたはログインIDは必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        this.validateEmail(emailOrLoginId);
        // this.validateLoginId(emailOrLoginId);
        this.emailOrLoginId = emailOrLoginId;
    }

    /**
     * メールアドレスまたはログインID値オブジェクトを生成するファクトリメソッド
     * 
     * @param emailOrLoginId メールアドレスまたはログインIDの文字列
     * @return EmailOrLoginId メールアドレスまたはログインIDの値オブジェクト
     */
    public static EmailOrLoginId of(final String emailOrLoginId) {
        logger.info("Factory method called to create EmailOrLoginId with value: {}", emailOrLoginId);
        return new EmailOrLoginId(emailOrLoginId);
    }

    /**
     * メールアドレスまたはログインIDの文字列を取得する
     * 
     * @return String メールアドレスまたはログインIDの文字列
     */
    public String value() {
        return this.emailOrLoginId;
    }

    /**
     * メールアドレスの妥当性を検証する
     * 
     * @param emailOrLoginId 検証対象のメールアドレス
     * @throws DomainLayerErrorException メールアドレスが不正な場合
     */
    private void validateEmail(final String emailOrLoginId) {
        logger.info("Validating email format for: {}", emailOrLoginId);
        if (emailOrLoginId.length() > maxLength) {
            logger.error("Email length exceeds maximum of {} characters", maxLength);
            throw new DomainLayerErrorException("メールアドレスは最大" + maxLength + "文字までです",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (!emailPatternCompiled.matcher(emailOrLoginId).matches() || !emailBannedDomainPatternCompiled.matcher(emailOrLoginId).matches()) {
            logger.error("Email format is invalid for: {}", emailOrLoginId);
            throw new DomainLayerErrorException("無効なメールアドレス形式です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }

    /**
     * ログインIDの妥当性を検証する
     * 
     * @param emailOrLoginId 検証対象のログインID
     * @throws DomainLayerErrorException ログインIDが不正な場合
     */
    private void validateLoginId(final String emailOrLoginId) {
        if (!loginIdPatternCompiled.matcher(emailOrLoginId).matches()) {
            throw new DomainLayerErrorException("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }

        if (emailOrLoginId.length() < minLength || emailOrLoginId.length() > maxLength) {
            throw new DomainLayerErrorException("ログインIDは" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
}
