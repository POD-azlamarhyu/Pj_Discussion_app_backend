package com.application.discussion.project.presentation.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import com.application.discussion.project.application.services.security.JWTUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/v1/auth/login","POST");

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui/index.html",
        "/swagger-ui.html",
        "/swagger-ui.html/**",
        "/swagger-resources/**",
        "/configuration/**",
        "/webjars/**",
        "/api-docs/**",
        "/v3/api-docs",
        "/swagger-ui",
        "/swagger-resources",
        "/configuration",
        "/webjars",
        "/api-docs",
        "/maintopics",
        "/v1/auth/login"
    );

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private JWTAuthUserDetailsService jwtAuthUserDetailsService;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI=httpServletRequest.getRequestURI();
        logger.debug("called for URI : {}",requestURI);

        /**
         * * パブリックパスの場合は認証をスキップする
         * * 例: Swagger UIやログインエンドポイント
         * * これにより、これらのパスへのアクセスが認証なしで可能になる
         * ! セキュリティ上の注意: パブリックパスを適切に管理し、不要なパスが含まれないように注意すること
         * ! また、デプロイの際はリストを見直し、セキュリティを確保すること
         */
        if (isPublicPath(requestURI)) {
            logger.debug("Skipping JWT authentication for public path: {}", requestURI);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (requestMatcher.matches(httpServletRequest)) {
            logger.debug("Skipping JWT authentication for login request: {}", requestURI);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = jwtUtils.getJwtFromCookies(httpServletRequest);
        if (!StringUtils.hasText(token) || !jwtUtils.validateJwtToken(token)){
            logger.error("JWT token is invalid or missing");
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
        return;
    }

    private Boolean isPublicPath(final String requestURI) {
        logger.info("Public path is checking... , requestURI: {}", requestURI);
        return PUBLIC_PATHS.stream().anyMatch(requestURI::startsWith);
    }
}
