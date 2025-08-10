package com.application.discussion.project.domain.entities.topics;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;

import lombok.Builder;


@Builder
public class Maintopic {

    private final Long maintopicId;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean isDeleted;
    private final Boolean isClosed;

    private static final Logger logger = LoggerFactory.getLogger(Maintopic.class);

    // Additional fields and methods can be added as needed
    public Maintopic(
        Long maintopicId, 
        String title, 
        String description, 
        LocalDateTime createdAt, 
        LocalDateTime updatedAt,
        Boolean isDeleted,
        Boolean isClosed
    ) {
        this.maintopicId = maintopicId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;

        logger.info("Maintopic domain entity created with ID: {}", this.maintopicId);
    }
    public static Maintopic of(
        Long maintopicId, 
        String title, 
        String description, 
        LocalDateTime createdAt, 
        LocalDateTime updatedAt,
        Boolean isDeleted,
        Boolean isClosed
    ) {
        return new Maintopic(maintopicId, title, description, createdAt, updatedAt, isDeleted, isClosed);
    }
    public static Maintopic create(
        Title title,
        Description description
    ){
        return new Maintopic(
            null,
            title.getValue(),
            description.getValue(),
            null,
            null,
            false,
            false
        );
    }

    public Long getMaintopicId() {
        return this.maintopicId;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
    public Boolean getIsDeleted() {
        return this.isDeleted;
    }
    public Boolean getIsClosed() {
        return this.isClosed;
    }
}
