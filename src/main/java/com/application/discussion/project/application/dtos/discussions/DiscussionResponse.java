package com.application.discussion.project.application.dtos.discussions;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 議論詳細のレスポンス
 */
@Schema(description = "議論のレスポンス")
public class DiscussionResponse {

    @Schema(description = "議論ID", example = "1")
    private Long discussionId;

    @Schema(description = "パラグラフ", example = "このプロジェクトの目的は...")
    private String paragraph;

    @Schema(description = "メイントピックID", example = "10")
    private Long maintopicId;

    @Schema(description = "作成日時", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2024-01-15T15:45:00")
    private LocalDateTime updatedAt;

    private DiscussionResponse(Long discussionId, String paragraph, Long maintopicId,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.discussionId = discussionId;
        this.paragraph = paragraph;
        this.maintopicId = maintopicId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DiscussionResponse of(
        Long discussionId, String paragraph, Long maintopicId,
        LocalDateTime createdAt, LocalDateTime updatedAt
    ){
        return new DiscussionResponse(
            discussionId, paragraph, maintopicId,
            createdAt, updatedAt
        );
    }

    public Long getDiscussionId() {
        return discussionId;
    }

    public String getParagraph() {
        return paragraph;
    }

    public Long getMaintopicId() {
        return maintopicId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
