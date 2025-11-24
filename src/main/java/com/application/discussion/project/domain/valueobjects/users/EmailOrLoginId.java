package com.application.discussion.project.domain.valueobjects.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

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

    private EmailOrLoginId(final String emailOrLoginId) {
        if (StringUtils.isBlank(emailOrLoginId)) {
            throw new DomainLayerErrorException("メールアドレスまたはログインIDは必須項目です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        this.validateEmail(emailOrLoginId);
        // this.validateLoginId(emailOrLoginId);
        this.emailOrLoginId = emailOrLoginId;
    }

    public static EmailOrLoginId of(final String emailOrLoginId) {
        return new EmailOrLoginId(emailOrLoginId);
    }

    public String value() {
        return this.emailOrLoginId;
    }

    private void validateEmail(final String emailOrLoginId) {
        if (emailOrLoginId.length() > maxLength) {
            throw new DomainLayerErrorException("メールアドレスは最大" + maxLength + "文字までです",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
        if (!emailPatternCompiled.matcher(emailOrLoginId).matches() || !emailBannedDomainPatternCompiled.matcher(emailOrLoginId).matches()) {
            throw new DomainLayerErrorException("無効なメールアドレス形式です",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }

    private void validateLoginId(final String emailOrLoginId) {
        if (!loginIdPatternCompiled.matcher(emailOrLoginId).matches()) {
            throw new DomainLayerErrorException("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }

        if (emailOrLoginId.length() < minLength || emailOrLoginId.length() > maxLength) {
            throw new DomainLayerErrorException("ログインIDは" + minLength + "文字以上" + maxLength + "文字以下である必要があります",HttpStatus.BAD_REQUEST , HttpStatusCode.valueOf(400));
        }
    }
}
