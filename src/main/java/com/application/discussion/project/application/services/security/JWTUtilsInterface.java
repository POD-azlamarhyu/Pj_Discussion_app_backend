package com.application.discussion.project.application.services.security;


import java.security.Key;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletRequest;


public interface JWTUtilsInterface {
    String generateToken(JWTAuthUserDetails userDetails);
    ResponseCookie generateJwtCookie(JWTAuthUserDetails userDetails);
    String getEmailOrLoginId(String token);
    Boolean validateJwtToken(String token);
    String getUserIdFromToken(String token);
    String getJwtFromCookies(HttpServletRequest request);
    String getJwtFromHeader(HttpServletRequest request);
    Key getKey();
    String generateRefreshToken(JWTAuthUserDetails userDetails);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    Boolean validateRefreshToken(String token);
    String getUserIdFromRefreshToken(String token);
    String getEmailOrLoginIdFromRefreshToken(String token);
    Key getRefreshTokenKey();
    ResponseCookie getClearRefreshTokenCookie();
    String hashToken(String token);
    long getRefreshTokenExpirationMs();
}
