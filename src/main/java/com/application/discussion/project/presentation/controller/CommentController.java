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
@RequestMapping("/comments")
@Tag(name = "Comment Management", description = "API for managing comments")
public class CommentController {

    @Operation(summary = "Create a new comment", description = "Posts a new comment to the platform")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comment successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public void createComment(){}


    @Operation(summary = "Update comment information", description = "Edits the information of an existing comment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comment information successfully updated"),
        @ApiResponse(responseCode = "404", description = "Comment with the specified ID not found")
    })
    @PutMapping("/{id}")
    public void updateComment(){}

    @Operation(summary = "Get all comments", description = "Retrieves a list of all comments")
    @ApiResponse(responseCode = "200", description = "All comments retrieved successfully")
    @GetMapping
    public void getComments(){}
    
    @Operation(summary = "Get comment information", description = "Retrieves the details of a comment by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comment information retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Comment with the specified ID not found")
    })
    @GetMapping("/{id}")
    public void retrieveComments(){}

    @Operation(summary = "Delete a comment", description = "Deletes a comment by marking it as deleted")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comment successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Comment with the specified ID not found")
    })
    @DeleteMapping("/{id}")
    public void deleteComment(){}

    @Operation(summary = "Search Comments", description = "Searches comments based on the title or content")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comments retrieved based on search criteria"),
        @ApiResponse(responseCode = "400", description = "Invalid search query")
    })
    @GetMapping("/search")
    public void searchComments(){}
}
