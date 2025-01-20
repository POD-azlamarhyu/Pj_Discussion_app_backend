package com.application.discussion.project.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification Management", description = "API for managing notifications")
public class NotificationController {

    @GetMapping
    @Operation(summary = "get unread notifications")
    public void getNotifications(){}

    @Operation(summary = "retrieve unread notification")
    @GetMapping("/{id}")
    public void retrieveNotification(){}

    @Operation(summary = "add read notification")
    @PostMapping
    public void addReadNotifications(){}

}
