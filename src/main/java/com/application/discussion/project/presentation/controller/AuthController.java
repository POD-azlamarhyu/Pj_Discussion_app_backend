package com.application.discussion.project.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="authentication",description = "Authentication API")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Operation(summary = "login to application",tags = {"authentication"})
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "login successful",
                content = @Content(
                    schema = @Schema(implementation = ResponseEntity.class),
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content
            ),
            @ApiResponse(responseCode = "403",description = "Forbidden",content = @Content)
        }
    )
    @PostMapping(value = "/login")
    public void login(){

    }
    
    @Operation(summary = "logout from application",tags = {"authentication"})
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "logout successful",
                content = @Content(
                    schema = @Schema(implementation = ResponseEntity.class),
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(responseCode = "401",description = "Not authorized",content = @Content),
            @ApiResponse(responseCode = "403",description = "Forbidden",content = @Content),
            @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content
            )
        }
    )
    @PostMapping("/logout")
    public void logout(){

    }
    
    
    @Operation(summary = "refresh access token jwt",tags = {"authentication"})
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "access token refresh successful",
                content = @Content(schema = @Schema(implementation = ResponseEntity.class))
            ),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)
        }
    )
    @PostMapping("/refresh")
    public void refresh(){

    }
}
