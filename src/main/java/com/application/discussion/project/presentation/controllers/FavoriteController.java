package com.application.discussion.project.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "favorite",description = "Add favorite to discussion topics")
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    
    @PostMapping
    public void toggleFavorite(){}
}
