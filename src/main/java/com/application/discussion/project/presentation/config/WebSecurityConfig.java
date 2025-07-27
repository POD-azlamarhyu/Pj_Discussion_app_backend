package com.application.discussion.project.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(
            (authorize) -> authorize.requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui/index.html",
                "/swagger-ui.html",
                "/maintopics/*"
            ).permitAll()
        );

        return httpSecurity.build();
    }
}
