package com.application.discussion.project.application.services.users;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.AuthCheckResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;

/**
 * 認証状態確認のアプリケーションサービス実装
 */
@Service
public class AuthCheckServiceImpl implements AuthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(AuthCheckServiceImpl.class);
    private static final String AUTHENTICATED_MESSAGE = "認証済みです";
    private static final String NOT_AUTHENTICATED_MESSAGE = "認証されていません";

    @Override
    public AuthCheckResponse service() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            logger.warn("認証情報が存在しないか、未認証です");
            throw new ApplicationLayerException(
                NOT_AUTHENTICATED_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof JWTAuthUserDetails)) {
            logger.warn("PrincipalがJWTAuthUserDetailsではありません: {}",
                principal != null ? principal.getClass().getName() : "null");
            throw new ApplicationLayerException(
                NOT_AUTHENTICATED_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }

        JWTAuthUserDetails userDetails = (JWTAuthUserDetails) principal;
        logger.info("認証状態確認成功: userId={}, username={}", userDetails.getUserId(), userDetails.getUsername());

        return AuthCheckResponse.of(
            userDetails.getUsername(),
            true,
            AUTHENTICATED_MESSAGE
        );
    }
}
