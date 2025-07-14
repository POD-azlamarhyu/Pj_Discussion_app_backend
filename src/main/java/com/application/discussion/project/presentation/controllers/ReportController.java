package com.application.discussion.project.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "report",description = "API for report")
@RestController
@RequestMapping("/reports")
public class ReportController {

    @PostMapping
    public void addReport(){}
}
