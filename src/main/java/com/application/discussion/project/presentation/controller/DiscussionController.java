package com.application.discussion.project.presentation.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/discussions")
@Tag(name = "Discussion Management", description = "API for managing discussions")
public class DiscussionController {

    @Operation(summary = "Create a new discussion", description = "Posts a new discussion to the platform")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Discussion successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public void createDiscussion(){}


    @Operation(summary = "Update discussion information", description = "Edits the information of an existing discussion")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Discussion information successfully updated"),
        @ApiResponse(responseCode = "404", description = "Discussion with the specified ID not found")
    })
    @PutMapping("/{id}")
    public void updateDiscussion(){}

    @Operation(summary = "Get all discussions", description = "Retrieves a list of all discussions")
    @ApiResponse(responseCode = "200", description = "All discussions retrieved successfully")
    @GetMapping
    public void getDiscussions(){}
    
    @Operation(summary = "Get discussion information", description = "Retrieves the details of a discussion by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Discussion information retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Discussion with the specified ID not found")
    })
    @GetMapping("/{id}")
    public void retrieveDiscussions(){}

    @Operation(summary = "Delete a discussion", description = "Deletes a discussion by marking it as deleted")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Discussion successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Discussion with the specified ID not found")
    })
    @DeleteMapping("/{id}")
    public void deleteDiscussion(){}

    @Operation(summary = "Search discussions", description = "Searches discussions based on the title or content")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Discussions retrieved based on search criteria"),
        @ApiResponse(responseCode = "400", description = "Invalid search query")
    })
    @GetMapping("/search")
    public void searchDiscussions(){}
}
