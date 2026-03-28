package com.application.discussion.project.application.services.users;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTUtils;
import com.application.discussion.project.domain.entities.users.RefreshToken;
import com.application.discussion.project.domain.repositories.users.RefreshTokenRepository;


@Service
public class AuthLoginService implements AuthLoginServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(AuthLoginService.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    /**
     * ユーザーのログインを処理するサービスメソッド
     * 
     * @param loginRequest ログインリクエストDTO（メールアドレスまたはログインID、パスワードを含む）
     * @return レスポンスエンティティ（JWTトークンとユーザー情報を含む）
     * @throws ApplicationLayerException 認証失敗時にスローされる例外
     */
    @Override
    @Transactional
    public ResponseEntity<LoginResponse> service(final LoginRequest loginRequest) {
        logger.debug("AuthLoginService called with request: {}", loginRequest.toString());
        
        final String emailOrLoginId = loginRequest.getEmailOrLoginId();
        
        final String password = loginRequest.getPassword();
        
        Authentication authentication = null;

        try{
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailOrLoginId, password)
            );
        }catch(AuthenticationException e){
            logger.error("Authentication failed for user: {}", emailOrLoginId, e);
            throw new ApplicationLayerException("認証エラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500));
        } 
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Authentication returned null or unauthenticated for user: {}", emailOrLoginId);
            throw new ApplicationLayerException("認証に失敗しました", HttpStatus.BAD_REQUEST,HttpStatusCode.valueOf(400));
        }

        logger.info("User {} authenticated successfully", emailOrLoginId);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final JWTAuthUserDetails userDetails = (JWTAuthUserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtils.generateToken(userDetails);

        final String refreshTokenStr = jwtUtils.generateRefreshToken(userDetails);
        final String tokenHash = jwtUtils.hashToken(refreshTokenStr);
        final LocalDateTime expiresAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(System.currentTimeMillis() + jwtUtils.getRefreshTokenExpirationMs()),
            ZoneId.systemDefault()
        );
        final RefreshToken refreshToken = RefreshToken.create(userDetails.getUserId(), tokenHash, expiresAt);
        refreshTokenRepository.save(refreshToken);

        final ResponseCookie refreshCookie = jwtUtils.generateRefreshTokenCookie(refreshTokenStr);

        logger.info("Generated access token and refresh token for user: {} with email: {}", userDetails.getUsername(), userDetails.getEmail());
        
        final LoginResponse loginResponse = LoginResponse.builder()
            .userId(userDetails.getUserId())
            .username(userDetails.getUsername())
            .roles(userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()))
            .accessToken(accessToken)
            .build();

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(loginResponse);
    }

}
