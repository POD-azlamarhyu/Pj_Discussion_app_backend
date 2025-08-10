package com.application.discussion.project.application.dtos.topics;

import lombok.Builder;

@Builder
public class MaintopicCreateResponse {
    private Long id;
    private String title;
    private String description;
    private String createdAt;

    public MaintopicCreateResponse(
        Long id, 
        String title,
        String description, 
        String createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
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
}
