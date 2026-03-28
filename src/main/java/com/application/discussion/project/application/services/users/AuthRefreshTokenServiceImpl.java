package com.application.discussion.project.application.services.users;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.AuthRefreshTokenServiceResult;
import com.application.discussion.project.application.dtos.users.RefreshTokenResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTAuthUserDetailsService;
import com.application.discussion.project.application.services.security.JWTUtils;
import com.application.discussion.project.domain.entities.users.RefreshToken;
import com.application.discussion.project.domain.repositories.users.RefreshTokenRepository;

import jakarta.servlet.http.HttpServletRequest;

/**
 * リフレッシュトークンによるアクセストークン再発行サービスの実装クラス
 */
@Service
public class AuthRefreshTokenServiceImpl implements AuthRefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AuthRefreshTokenServiceImpl.class);
    private static final String INVALID_REFRESH_TOKEN_MESSAGE = "リフレッシュトークンが無効です";
    private static final String MISSING_REFRESH_TOKEN_MESSAGE = "リフレッシュトークンが見つかりません";

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private JWTAuthUserDetailsService jwtAuthUserDetailsService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public AuthRefreshTokenServiceResult service(final HttpServletRequest request) {
        logger.info("リフレッシュトークン処理を開始します");

        String refreshTokenStr = jwtUtils.getRefreshTokenFromCookies(request);

        if (!StringUtils.hasText(refreshTokenStr)) {
            logger.error("リフレッシュトークンがCookieに見つかりません");
            throw new ApplicationLayerException(
                MISSING_REFRESH_TOKEN_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }

        jwtUtils.validateRefreshToken(refreshTokenStr);

        String tokenHash = jwtUtils.hashToken(refreshTokenStr);
        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(tokenHash)
            .orElseThrow(() -> {
                logger.error("リフレッシュトークンがDBに見つかりません");
                return new ApplicationLayerException(
                    INVALID_REFRESH_TOKEN_MESSAGE,
                    HttpStatus.UNAUTHORIZED,
                    HttpStatusCode.valueOf(401)
                );
            });

        if (storedToken.isAlreadyUsed()) {
            logger.warn("使用済みリフレッシュトークンの再利用を検出しました。userId: {}", storedToken.getUserId());
            refreshTokenRepository.revokeAllByUserId(storedToken.getUserId());
            throw new ApplicationLayerException(
                INVALID_REFRESH_TOKEN_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }

        if (!storedToken.isValid()) {
            logger.error("リフレッシュトークンが無効または失効しています。tokenId: {}", storedToken.getId());
            throw new ApplicationLayerException(
                INVALID_REFRESH_TOKEN_MESSAGE,
                HttpStatus.UNAUTHORIZED,
                HttpStatusCode.valueOf(401)
            );
        }

        refreshTokenRepository.markAsUsed(storedToken.getId());

        String emailOrLoginId = jwtUtils.getEmailOrLoginIdFromRefreshToken(refreshTokenStr);
        UserDetails userDetails = jwtAuthUserDetailsService.loadUserByUsername(emailOrLoginId);
        JWTAuthUserDetails jwtUserDetails = (JWTAuthUserDetails) userDetails;

        String newAccessToken = jwtUtils.generateToken(jwtUserDetails);

        String newRefreshTokenStr = jwtUtils.generateRefreshToken(jwtUserDetails);
        String newTokenHash = jwtUtils.hashToken(newRefreshTokenStr);

        LocalDateTime newExpiresAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(System.currentTimeMillis() + jwtUtils.getRefreshTokenExpirationMs()),
            ZoneId.systemDefault()
        );
        RefreshToken newRefreshToken = RefreshToken.create(
            jwtUserDetails.getUserId(),
            newTokenHash,
            newExpiresAt
        );
        refreshTokenRepository.save(newRefreshToken);

        ResponseCookie newRefreshCookie = jwtUtils.generateRefreshTokenCookie(newRefreshTokenStr);

        logger.info("リフレッシュトークン処理が完了しました。userId: {}", jwtUserDetails.getUserId());

        RefreshTokenResponse response = RefreshTokenResponse.of(newAccessToken);
        return AuthRefreshTokenServiceResult.of(response, newRefreshCookie);
    }
}
