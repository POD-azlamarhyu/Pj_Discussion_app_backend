package com.application.discussion.project.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "like",description = "Add like to discussion and comment")
@RestController
@RequestMapping("/likes")
public class LikeController {

    @PostMapping
    public void toggleLike(){}
}
