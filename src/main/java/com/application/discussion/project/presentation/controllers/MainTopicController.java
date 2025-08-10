package com.application.discussion.project.presentation.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicResponse;
import com.application.discussion.project.application.services.topics.MaintopicCreateService;
import com.application.discussion.project.application.services.topics.MaintopicDetailService;
import com.application.discussion.project.application.services.topics.MaintopicsListService;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;
import com.application.discussion.project.presentation.validations.MaintopicCreateRequestValidation;

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

    @Autowired
    private MaintopicDetailService maintopicDetailService;

    @Autowired
    private MaintopicCreateService maintopicCreateService;
    
    private static final Logger logger = LoggerFactory.getLogger(MainTopicController.class);
    
    @Operation(summary = "Create a new topic", description = "Registers a new discussion topic in the system")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Topic has been created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error",content = @Content)
    })
    @PostMapping
    public ResponseEntity<MaintopicCreateResponse> createMainTopic(@RequestBody  MaintopicCreateRequest maintopicCreateRequest){
        MaintopicCreateRequestValidation.isValidTitle(maintopicCreateRequest.getTitle());
        MaintopicCreateRequestValidation.isValidDescription(maintopicCreateRequest.getDescription());
        logger.info("Creating a new main topic with title: {}", maintopicCreateRequest.getTitle());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MaintopicCreateResponse response = maintopicCreateService.service(maintopicCreateRequest);
        logger.info("Main topic created with ID: {}", response.getId());
        return  ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(response);
    }

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
        logger.info("Retrieving list of main topics");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(maintopicsListService.service());
    }

    @Operation(summary = "find topic information", description = "Fetches the information of a topic based on its ID")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Topic information found successfully",
            content = @Content(
                schema = @Schema(implementation = MaintopicResponse.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Topic with the specified ID not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation=ResourceNotFoundException.class) 
            )
        )
    })
    @GetMapping("/{maintopicId}")
    public ResponseEntity<MaintopicResponse> findMaintopicById(@PathVariable Long maintopicId){
        logger.info("Finding maintopic by ID: {}", maintopicId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(maintopicDetailService.service(maintopicId));
    }

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
