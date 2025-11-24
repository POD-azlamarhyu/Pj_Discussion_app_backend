package com.application.discussion.project.presentation.validations;

import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AuthLoginRequestValidation {

    private static final Logger logger = LoggerFactory.getLogger(AuthLoginRequestValidation.class);

    public static void validate(final LoginRequest loginRequest) {
        List<String> errors = new ArrayList<>();
        logger.info("Starting validation for LoginRequest");
        Optional.ofNullable(loginRequest)
            .ifPresentOrElse(
                request -> {
                    validateEmailOrLoginId(request.getEmailOrLoginId())
                        .ifPresent(errors::add);
                    validatePassword(request.getPassword())
                        .ifPresent(errors::add);
                },
                () -> errors.add("ログインリクエストがnullです")
            );

        Optional.of(errors)
            .filter(errorList -> !errorList.isEmpty())
            .ifPresent(errorList -> {
                throw new PresentationLayerErrorException(String.join(", ", errorList), HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400));
            });
    }

    private static Optional<String> validateEmailOrLoginId(final String emailOrLoginId) {
        logger.info("Validating emailOrLoginId: {}", emailOrLoginId);
        return Optional.ofNullable(emailOrLoginId)
            .filter(value -> !value.trim().isEmpty())
            .map(value -> Optional.<String>empty())
            .orElse(Optional.of("メールアドレスまたはログインIDは必須です"));
    }

    private static Optional<String> validatePassword(final String password) {
        logger.info("Validating password: {}", password != null ? "********" : null);
        return Optional.ofNullable(password)
            .filter(value -> !value.trim().isEmpty())
            .map(value -> Optional.<String>empty())
            .orElse(Optional.of("パスワードは必須です"));
    }
}
