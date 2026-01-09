package com.application.discussion.project.presentation.security;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;

import com.application.discussion.project.application.dtos.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthEntryPoint.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException authenticationException
    ) throws IOException, ServletException {

        logger.error("Unauthorized error: {}", authenticationException.getMessage());

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = ErrorResponse.of(
            "認証に失敗しました。",
            HttpStatusCode.valueOf(401).value(),
            HttpStatus.UNAUTHORIZED.name()
        );

        objectMapper.writeValue(httpServletResponse.getOutputStream(), errorResponse);
    }
}
