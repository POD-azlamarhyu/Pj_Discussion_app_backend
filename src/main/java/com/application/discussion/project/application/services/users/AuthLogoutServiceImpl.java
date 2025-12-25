package com.application.discussion.project.application.services.users;

import com.application.discussion.project.application.dtos.users.LogoutResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.security.JWTUtils;

/**
 * ログアウト処理を行うアプリケーションサービスの実装クラス
 */
@Service
public class AuthLogoutServiceImpl implements AuthLogoutService {

    @Autowired
    private JWTUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthLogoutServiceImpl.class);

    /**
     * ログアウト処理を実行する
     * 
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @return ログアウト結果を含むレスポンス
     */
    @Override
    public LogoutResponseDTO service() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        String username = Optional.ofNullable(authentication)
                .map(Authentication::getName)
                .orElse("不明なユーザ");
        
        logger.info("ログアウト処理を開始します - ユーザ: {}", username);

        final ResponseCookie jwtCookie = jwtUtils.getClearJwtCookie();
        SecurityContextHolder.clearContext();

        logger.info("ログアウト処理が完了しました - ユーザ: {}", username);
        final LogoutResponse logoutResponse =  LogoutResponse.builder()
            .message("ログアウトに成功しました")
            .success(true)
            .build();

        final LogoutResponseDTO logoutResponseDTO = LogoutResponseDTO.of(logoutResponse, jwtCookie);

        return logoutResponseDTO;
    }
}
