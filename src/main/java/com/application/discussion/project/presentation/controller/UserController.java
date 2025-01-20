package com.application.discussion.project.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user",description = "user / account api")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Operation(summary = "user registration to application",tags = {"user"},description = "When a user registers an account, if unintended input or server-side errors occur, error messages are returned.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "account register successful",
                content = @Content(schema = @Schema(implementation = ResponseEntity.class),
                mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "400",description = "password length < 10",content = @Content),
            @ApiResponse(responseCode = "403",description = "Forbidden",content = @Content),
            @ApiResponse(responseCode = "409",description = "Resource conflict",content = @Content),
            @ApiResponse(responseCode = "500",description = "Internal Server Error",content = @Content)
        }
    )
    @PostMapping
    public void register(){

    }

    @Operation(summary = "get my account info",tags = {"user"},description = "Retrieve your account information.")
    @GetMapping("/account")
    public void retrieveMyUser(){

    }

    @Operation(summary = "get users info",tags = {"user"},description = "Retrieve all users' information.")
    @GetMapping
    public void retrieveUsers(){

    }
    
    @Operation(summary = "get user info",tags = {"user"},description = "Retrieve user information specified by UserId.")
    @GetMapping("/{id}")
    public void retrieveUser(){

    }

    @Operation(summary = "edit/update authenticate password",tags = {"user"},description = "")
    @PatchMapping("/password")
    public void updatePassword(){

    }

    @Operation(summary = "edit/update authenticate login ID",tags = {"user"},description = "")
    @PatchMapping("/loginid")
    public void updateLoginId(){

    }

    @Operation(summary = "edit update user info",tags = {"user"},description = "Update user information except for the password and login ID.")
    @PatchMapping
    public void updateMyUser(){

    }

    @Operation(summary = "delete user info",tags = {"user"},description = "Perform a physical deletion of user information.")
    @DeleteMapping
    public void deleteMyUser(){

    }

}
