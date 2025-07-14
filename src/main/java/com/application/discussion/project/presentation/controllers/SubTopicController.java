package com.application.discussion.project.presentation.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "subtopic",description = "API for manipulating the sub topic information of discussions.")
@RestController
@RequestMapping("/subtopics")
public class SubTopicController {
    @Operation(summary = "Create a new topic", description = "Registers a new discussion topic in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Topic has been created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500",description = "Internal Server Error",content = @Content)
    })
    @PostMapping
    public void createMainTopic(){}

    @Operation(summary = "Update topic information", description = "Edits the information of an existing topic")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Topic information has been updated successfully"),
        @ApiResponse(responseCode = "404", description = "Topic with the specified ID not found"),
        @ApiResponse(responseCode = "500",description = "Internal Server Error",content = @Content)
    })
    @PutMapping("/{id}")
    public void updateMainTopic(){}


    @Operation(summary = "Retrieve topic information", description = "Fetches the information of a topics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Topic information retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping
    public void retrieveMainTopics(){}

    @Operation(summary = "Retrieve topic information", description = "Fetches the information of a topic based on its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Topic information retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Topic with the specified ID not found")
    })
    @GetMapping("/{id}")
    public void retrieveMainTopic(){}

    @Operation(summary = "Delete topic information", description = "Delete the information of a topic based on its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Topic information delete successfully"),
        @ApiResponse(responseCode = "404", description = "Topic with the specified ID not found")
    })
    @DeleteMapping
    public void deleteMainTopic(){}

    @Operation(summary = "Close a topic", description = "Marks a topic as closed, preventing further discussion")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Topic has been successfully closed"),
        @ApiResponse(responseCode = "404", description = "Topic with the specified ID not found")
    })
    @PatchMapping("/{id}/close")
    public void closeMainTopic(){}
}
