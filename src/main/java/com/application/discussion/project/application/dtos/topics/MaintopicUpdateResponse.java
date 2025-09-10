package com.application.discussion.project.application.dtos.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Builder;

@Builder
public class MaintopicUpdateResponse {
    private Long id;
    private String title;
    private String description;
    private String createdAt;
    private String updatedAt;
    private static final Logger logger = LoggerFactory.getLogger(MaintopicUpdateResponse.class);

    public MaintopicUpdateResponse(
        Long id, 
        String title,
        String description, 
        String createdAt,
        String updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        logger.info("MaintopicUpdateResponse DTO created with ID: {}", this.id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
