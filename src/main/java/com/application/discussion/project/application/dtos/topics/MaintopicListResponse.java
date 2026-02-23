package com.application.discussion.project.application.dtos.topics;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public class MaintopicListResponse {

    
    private Long maintopicId;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("isClosed")
    private Boolean isClosed;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicListResponse.class);

    public MaintopicListResponse(Long maintopicId, String title, String description, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted, Boolean isClosed) {
        this.maintopicId = maintopicId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;

        logger.info("MaintopicListResponse DTO created with ID: {}", this.maintopicId);
    }

    public Long getMaintopicId() {
        return maintopicId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }
    public Boolean getIsClosed() {
        return isClosed;
    }
}
