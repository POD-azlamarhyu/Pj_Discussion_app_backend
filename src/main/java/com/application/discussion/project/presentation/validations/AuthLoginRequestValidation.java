package com.application.discussion.project.presentation.validations;

import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AuthLoginRequestValidation {

    public static void validate(final LoginRequest loginRequest) {
        List<String> errors = new ArrayList<>();

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
        return Optional.ofNullable(emailOrLoginId)
            .filter(value -> !value.trim().isEmpty())
            .map(value -> Optional.<String>empty())
            .orElse(Optional.of("メールアドレスまたはログインIDは必須です"));
    }

    private static Optional<String> validatePassword(final String password) {
        return Optional.ofNullable(password)
            .filter(value -> !value.trim().isEmpty())
            .map(value -> Optional.<String>empty())
            .orElse(Optional.of("パスワードは必須です"));
    }
}
