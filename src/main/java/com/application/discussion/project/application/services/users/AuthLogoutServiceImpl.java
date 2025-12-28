package com.application.discussion.project.application.services.users;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LogoutResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTUtils;

/**
 * ログアウト処理を行うアプリケーションサービスの実装クラス
 */
@Service
public class AuthLogoutServiceImpl implements AuthLogoutService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthLogoutServiceImpl.class);
    private static final String LOGOUT_SUCCESS_MESSAGE = "ログアウトに成功しました";
    private static final String AUTHENTICATION_NOT_FOUND_MESSAGE = "ログアウト処理中に認証情報が見つかりません。";

    @Autowired
    private JWTUtils jwtUtils;

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
        
        if (Objects.isNull(authentication)) {
            logger.error("認証情報が存在しない状態でログアウトを試みました");
            throw new ApplicationLayerException(
                AUTHENTICATION_NOT_FOUND_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }
        if (!authentication.isAuthenticated()) {
            logger.error("未認証のユーザーがログアウトを試みました");
            throw new ApplicationLayerException(
                AUTHENTICATION_NOT_FOUND_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }
        
        Object principal = authentication.getPrincipal();
        
        if (Objects.isNull(principal) || !(principal instanceof JWTAuthUserDetails)) {
            logger.warn("無効なPrincipal型でログアウトを試みました: {}", 
                principal != null ? principal.getClass().getName() : "null");
            throw new ApplicationLayerException(
                AUTHENTICATION_NOT_FOUND_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }
        
        JWTAuthUserDetails userDetails = (JWTAuthUserDetails) principal;
        String username = userDetails.getUsername();
        
        logger.info("ログアウト処理開始: ユーザー={}", username);
        
        SecurityContextHolder.clearContext();
        logger.info("セキュリティコンテキストをクリアしました: ユーザー={}", username);
        
        ResponseCookie jwtCookie = jwtUtils.getClearJwtCookie();
        LogoutResponse logoutResponse = LogoutResponse.of(LOGOUT_SUCCESS_MESSAGE, true);
        
        logger.info("ログアウト処理完了: ユーザー={}", username);
        
        return LogoutResponseDTO.of(logoutResponse, jwtCookie);
    }
}
