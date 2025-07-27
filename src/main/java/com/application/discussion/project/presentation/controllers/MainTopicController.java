package com.application.discussion.project.presentation.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.application.services.topics.MaintopicsListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "maintopic",description = "API for manipulating the main topic information of discussions.")
@RestController
@RequestMapping("/maintopics")
public class MainTopicController {
    @Autowired
    private MaintopicsListService maintopicsListService;

    private static final Logger logger = LoggerFactory.getLogger(MainTopicController.class);
    
    @Operation(summary = "Create a new topic", description = "Registers a new discussion topic in the system")
    @ApiResponses(
        value = {
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
        @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public void updateMainTopic(){}


    @Operation(summary = "Retrieve topic list information", description = "Fetches the information of a topics")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200", 
                description = "List of topics retrieved successfully",
                content = @Content(
                    array = @ArraySchema(
                        schema = @Schema(implementation = MaintopicListResponse.class)
                    ),
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Bad request - Invalid parameters or request format"
            )
    })
    @GetMapping
    public ResponseEntity<List<MaintopicListResponse>> findMaintopicList() throws Exception {
        // This method will be implemented to return a list of main topics
        // The implementation will typically call a service that fetches the data from the repository
        logger.info("Retrieving list of main topics");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(maintopicsListService.service());
    }

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
