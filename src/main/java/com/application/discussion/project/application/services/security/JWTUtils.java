package com.application.discussion.project.application.services.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JWTUtils implements JWTUtilsInterface {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${springboot.app.authentication.jwt.token.expiration}")
    private long jwtTokenExpirationMs;
    @Value("${springboot.app.authentication.jwt.token.secret}")
    private String jwtTokenSecret;
    @Value("${springboot.app.cookies.name}")
    private String jwtCookieName;

    @Override
    public String getJwtFromHeader(HttpServletRequest httpServletRequest) {
        // Implementation for extracting JWT from the Authorization header
        logger.info("Extracting JWT from Authorization header");
        
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest httpServletRequest) {
        // Implementation for extracting JWT from cookies
        logger.info("Extracting JWT from cookies");
        
        
        return null;
    }

    @Override
    public String generateToken(JWTAuthUserDetails userDetails) {
        // Implementation for generating JWT token
        logger.info("Generating JWT token for user: {} {}", userDetails.getUsername(), userDetails.getEmail());
        
        return Jwts.builder()
                .subject(userDetails.getLoginId() != null ? userDetails.getLoginId() : userDetails.getEmail())
                .id(userDetails.getUserId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtTokenExpirationMs))
                .signWith(getKey())
                .claim("username",userDetails.getUsername())
                .claim("loginId", userDetails.getLoginId())
                .compact();
    }
    
    @Override
    public String getEmailOrLoginId(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public String getUserIdFromToken(String token) {
        // Implementation for extracting user ID from JWT token
        logger.info("Extracting user ID from JWT token");
        
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }
    @Override
    public Boolean validateJwtToken(String token) {
        // Implementation for validating JWT token
        logger.info("Validating JWT token");
        
        try {
            Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e){
            logger.error("Invalid JWT token: ", e.getMessage());
            throw new MalformedJwtException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error("JWT token is expired: ", e.getMessage());
            throw new ExpiredJwtException(null, null, "JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            throw new IllegalArgumentException("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            logger.error("JWT token validation failed: {}", e.getMessage());
            throw new RuntimeException("JWT token validation failed: " + e.getMessage());
        }
        return false;
    }

    @Override
    public ResponseCookie generateJwtCookie(JWTAuthUserDetails userDetails) {
        // Implementation for generating JWT cookie
        logger.info("Generating JWT cookie for user: {} {}", userDetails.getUsername(), userDetails.getEmail());
        
        String jwt = generateToken(userDetails);
        return ResponseCookie.from(jwtCookieName, jwt)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api")
                .maxAge(jwtTokenExpirationMs / 1000)
                .build();
    }
    
    @Override
    public  Key getKey(){
        // Implementation for retrieving the signing key
        logger.info("Retrieving signing key for JWT token");
        // This should return the actual Key object used for signing the JWT
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtTokenSecret));
    }
}
