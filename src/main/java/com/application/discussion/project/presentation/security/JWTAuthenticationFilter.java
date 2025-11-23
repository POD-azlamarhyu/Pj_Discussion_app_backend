package com.application.discussion.project.presentation.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.application.discussion.project.application.services.security.JWTAuthUserDetailsService;
import com.application.discussion.project.application.services.security.JWTUtilsInterface;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/api/v1/login","POST");

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    @Autowired
    private JWTUtilsInterface jwtUtils;

    @Autowired
    private JWTAuthUserDetailsService jwtAuthUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.debug("JWTAuthenticationFiler called for URI : {}",httpServletRequest.getRequestURI());

        if(!requestMatcher.matches(httpServletRequest)) return;
        String token = jwtUtils.getJwtFromHeader(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtils.validateJwtToken(token)){
            logger.warn("JWT token is invalid or missing");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String emailOrLoginId = jwtUtils.getEmailOrLoginId(token);
        String userId = jwtUtils.getUserIdFromToken(token);
        logger.info("JWT token is valid for user: {} with ID: {}", emailOrLoginId, userId);

        UserDetails userDetails = jwtAuthUserDetailsService.loadUserByUsername(emailOrLoginId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
