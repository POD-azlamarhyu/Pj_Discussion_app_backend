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
@RequestMapping("/categories")
@Tag(name = "Category Management", description = "API for managing categories")
public class CategoryController {

    @Operation(summary = "Create a new category", description = "Creates a new category to the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public void createCategory(){}

    @Operation(summary = "Edit category information", description = "Edits the information of an existing category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category successfully updated"),
        @ApiResponse(responseCode = "404", description = "Category with the specified ID not found")
    })
    @PutMapping("/{id}")
    public void updateCategory(){}

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponse(responseCode = "200", description = "All categories retrieved successfully")
    @GetMapping
    public void getCategories(){}

    @Operation(summary = "Get category information", description = "Retrieves the details of a category by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category with the specified ID not found")
    })
    @GetMapping("/{id}")
    public void retrieveCategory(){}

    @Operation(summary = "Delete a category", description = "Deletes a category by marking it as deleted")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Category with the specified ID not found")
    })
    @DeleteMapping("/{id}")
    public void deleteCategory(){}

    @Operation(summary = "Search categories", description = "Searches categories based on the name or description")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories retrieved based on search criteria"),
        @ApiResponse(responseCode = "400", description = "Invalid search query")
    })
    @GetMapping("/search")
    public void searchCategory(){}
}
