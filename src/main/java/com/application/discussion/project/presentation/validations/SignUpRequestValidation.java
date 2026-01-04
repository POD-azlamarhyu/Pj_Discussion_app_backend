package com.application.discussion.project.presentation.validations;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.application.dtos.users.SignUpRequest;
import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;

public class SignUpRequestValidation {

    private static final Logger logger = LoggerFactory.getLogger(SignUpRequestValidation.class);

    /**
     * SignUpRequestの各フィールドの妥当性を検証する
     * 
     * @param signUpRequest 検証対象のSignUpRequest
     * @throws PresentationLayerErrorException 検証エラーが発生した場合
     */
    public static void validate(
        final SignUpRequest signUpRequest
    ) {
        logger.info("Starting validation for SignUpRequest");

        validateEmail(signUpRequest.getEmail());
        validatePassword(signUpRequest.getPassword());
        validateUsername(signUpRequest.getUsername());

        logger.info("SignUpRequest validation passed");
    }

    /**
     * メールアドレスのNullチェックとString型チェックを行う
     * 
     * @param email 検証対象のメールアドレス
     * @throws PresentationLayerErrorException メールアドレスがnullまたはString型でない場合
     */
    private static void validateEmail(Object email) {
        logger.info("Validating email address");
        
        if (Objects.isNull(email)) {
            logger.error("Email validation failed: email is null");
            throw new PresentationLayerErrorException(
                "メールアドレスは必須項目です",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        
        if (!(email instanceof String)) {
            logger.error("Email validation failed: email is not a String type");
            throw new PresentationLayerErrorException(
                "メールアドレスは文字列である必要があります",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        
        logger.info("Email validation passed");
    }

    /**
     * パスワードのNullチェックとString型チェックを行う
     * 
     * @param password 検証対象のパスワード
     * @throws PresentationLayerErrorException パスワードがnullまたはString型でない場合
     */
    private static void validatePassword(Object password) {
        logger.info("Validating password");
        
        if (Objects.isNull(password)) {
            logger.error("Password validation failed: password is null");
            throw new PresentationLayerErrorException(
                "パスワードは必須項目です",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        
        if (!(password instanceof String)) {
            logger.error("Password validation failed: password is not a String type");
            throw new PresentationLayerErrorException(
                "パスワードは文字列である必要があります",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        
        logger.info("Password validation passed");
    }

    /**
     * ユーザーネームのNullチェックとString型チェックを行う
     * 
     * @param username 検証対象のユーザーネーム
     * @throws PresentationLayerErrorException ユーザーネームがnullまたはString型でない場合
     */
    private static void validateUsername(Object username) {
        logger.info("Validating username");
        
        if (Objects.isNull(username)) {
            logger.error("Username validation failed: username is null");
            throw new PresentationLayerErrorException(
                "ユーザーネームは必須項目です",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        
        if (!(username instanceof String)) {
            logger.error("Username validation failed: username is not a String type");
            throw new PresentationLayerErrorException(
                "ユーザーネームは文字列である必要があります",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        
        logger.info("Username validation passed");
    }
}
