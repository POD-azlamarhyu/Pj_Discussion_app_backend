package com.application.discussion.project.presentation.controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.application.discussion.project.presentation.config.WebSecurityConfig;
import com.application.discussion.project.presentation.exceptions.GlobalExceptionHandler;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GlobalExceptionHandler.class, WebSecurityConfig.class})
public @interface RestAPIControllerTest {}
