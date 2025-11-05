package com.application.discussion.project.presentation.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.application.dtos.discussions.DiscussionListResponse;
import com.application.discussion.project.application.services.discussions.DiscussionCreateService;
import com.application.discussion.project.application.services.discussions.DiscussionListService;
import com.application.discussion.project.presentation.validations.DiscussionCreateRequestValidation;
import com.application.discussion.project.presentation.validations.DiscussionListRequestValidation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/maintopics/{maintopicId}/discussions")
@Tag(name = "Discussion Management", description = "API for managing discussions")
public class DiscussionController {

    @Autowired
    private DiscussionCreateService discussionCreateService;

    @Autowired
    private DiscussionListService discussionListService;

    private static final Logger logger = LoggerFactory.getLogger(DiscussionController.class);

    @Operation(
        summary = "ディスカッションを新規作成する", 
        description = "指定されたメイントピックに対して新しいディスカッションを投稿する。" +
                     "ディスカッションの本文は必須で、2000文字以内で入力する必要がある。"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "ディスカッションが正常に作成された",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DiscussionCreateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "リクエストデータが無効 - 本文が空または文字数制限を超過",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "指定されたメイントピックIDが存在しない",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<DiscussionCreateResponse> createDiscussion(
        @Parameter(
            description = "ディスカッションを関連付けるメイントピックのID", 
            required = true,
            example = "1"
        )
        @PathVariable Long maintopicId,
        @Parameter(
            description = "ディスカッション作成リクエスト", 
            required = true
        )
        @RequestBody DiscussionCreateRequest discussionCreateRequest
    ){
        logger.info("Received request to create discussion: {}", discussionCreateRequest);
        DiscussionCreateRequestValidation.validate(discussionCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            discussionCreateService.service(
                maintopicId,
                discussionCreateRequest
            )
        );
    }


    @Operation(summary = "Update discussion information", description = "Edits the information of an existing discussion")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Discussion information successfully updated"),
        @ApiResponse(responseCode = "404", description = "Discussion with the specified ID not found")
    })
    @PutMapping("/{id}")
    public void updateDiscussion(){}

    @Operation(
        summary = "議論リストを取得する", 
        description = "指定されたメイントピックに関連する議論のリストをページング形式で取得する。" +
                     "ページ番号、ページサイズ、ソート順を指定できる。"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "議論リストが正常に取得された",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DiscussionListResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "指定されたメイントピックIDが存在しない",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping
    public ResponseEntity<DiscussionListResponse> getDiscussions(
        @Parameter(
            description = "議論を取得するメイントピックのID", 
            required = true,
            example = "1"
        )
        @PathVariable Long maintopicId,
        @Parameter(
            description = "ページ番号（0から開始）", 
            example = "0"
        )
        @RequestParam(defaultValue = "0") int page,
        @Parameter(
            description = "1ページあたりの件数", 
            example = "10"
        )
        @RequestParam(defaultValue = "10") int size,
        @Parameter(
            description = "ソート項目（createdAt, updatedAtなど）", 
            example = "createdAt"
        )
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @Parameter(
            description = "ソート順（asc: 昇順, desc: 降順）", 
            example = "desc"
        )
        @RequestParam(defaultValue = "desc") String direction
    ) {
        logger.info("Fetching discussions for maintopicId: {}, page: {}, size: {}", maintopicId, page, size);
        
        DiscussionListRequestValidation.validate(maintopicId, page, size, sortBy, direction);
        
        final Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        final DiscussionListResponse response = discussionListService.service(
            maintopicId, 
            pageable
        );
        
        logger.info("Successfully fetched {} discussions", response.getTotalCount());
        return ResponseEntity.ok(response);
    }
    
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
