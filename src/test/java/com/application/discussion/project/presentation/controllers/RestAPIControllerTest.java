package com.application.discussion.project.presentation.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.application.discussion.project.presentation.config.WebSecurityConfig;
import com.application.discussion.project.presentation.exceptions.GlobalExceptionHandler;

@ContextConfiguration
@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
@Import({GlobalExceptionHandler.class, WebSecurityConfig.class})
public @interface RestAPIControllerTest {}
