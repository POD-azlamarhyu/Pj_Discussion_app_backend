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
}
