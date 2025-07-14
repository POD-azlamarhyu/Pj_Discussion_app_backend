package com.application.discussion.project.application.services.users;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTUtilsInterface;

import com.application.discussion.project.domain.valueobjects.users.EmailOrLoginId;
import com.application.discussion.project.domain.valueobjects.users.Password;

@Service
public class AuthLoginService implements AuthLoginServiceInterface {

    // This class is currently empty, but it can be implemented later
    // to handle the login service logic.
    private static final Logger logger = LoggerFactory.getLogger(AuthLoginService.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtilsInterface jwtUtils;
    
    @Override
    public ResponseEntity<?> service(LoginRequest loginRequest) {
        logger.debug("AuthLoginService called with request: {}", loginRequest);
        final EmailOrLoginId emailOrLoginId = EmailOrLoginId.of(loginRequest.getEmailOrLoginId());
        final Password password = Password.of(loginRequest.getPassword());
        Authentication authentication = null;

        try{
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailOrLoginId, password)
            );
        }catch(AuthenticationException e){
            logger.error("Authentication failed for user: {}", emailOrLoginId, e);
        } 
        if (authentication == null || !authentication.isAuthenticated()) throw new RuntimeException("Authentication failed for user: " + emailOrLoginId);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final JWTAuthUserDetails userDetails = (JWTAuthUserDetails) authentication.getPrincipal();
        ResponseCookie responseCookie = jwtUtils.generateJwtCookie(userDetails);

        logger.info("Generated JWT token for user: {} with email: {}", userDetails.getUsername(), userDetails.getEmail());
        
        LoginResponse loginResponse = LoginResponse.builder()
            .userId(userDetails.getUserId())
            .token(responseCookie.toString())
            .username(userDetails.getUsername())
            .roles(userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()))
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(loginResponse);
    }

}
