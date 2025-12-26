package com.application.discussion.project.application.services.users;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LogoutResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;

import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
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
        if (Optional.ofNullable(authentication).isEmpty()){
            logger.warn("ログアウト処理中に認証情報が見つかりません。");
            throw new ApplicationLayerException("ログアウト処理中に認証情報が見つかりません。",HttpStatus.UNAUTHORIZED,HttpStatusCode.valueOf(401));
        }
        
        JWTAuthUserDetails user = (JWTAuthUserDetails) authentication.getPrincipal();
        
        logger.info("ログアウト処理を開始します - ユーザ: {}", user.toString());

        final ResponseCookie jwtCookie = jwtUtils.getClearJwtCookie();
        SecurityContextHolder.clearContext();

        logger.info("ログアウト処理が完了しました - ユーザ: {}", user.toString());
        final LogoutResponse logoutResponse =  LogoutResponse.of("ログアウトに成功しました", true);

        final LogoutResponseDTO logoutResponseDTO = LogoutResponseDTO.of(logoutResponse, jwtCookie);

        return logoutResponseDTO;
    }
}
