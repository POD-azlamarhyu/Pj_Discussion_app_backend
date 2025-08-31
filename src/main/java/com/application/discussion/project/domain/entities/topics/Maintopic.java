package com.application.discussion.project.domain.entities.topics;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.domain.exceptions.BadRequestException;
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

    /**
     * タイトルと説明を更新した新しいMaintopicインスタンスを返す
     *
     * @param title 新しいタイトル
     * @param description 新しい説明
     * @return 更新された新しいMaintopicインスタンス
     * @throws BadRequestException titleまたはdescriptionがnullの場合
     */
    public Maintopic update(
        final Title title, 
        final Description description
    ) {
        logger.info("Updating maintopic ID: {} with new title and description", this.maintopicId);
        if (title.isEmpty()) {
            logger.error("Attempted to update maintopic with empty title");
            throw new BadRequestException("Title cannot be null", "Bad_Request");
        }
        if (description.isEmpty()) {
            logger.error("Attempted to update maintopic with empty description");
            throw new BadRequestException("Description cannot be null", "Bad_Request");
        }

        if (title.equals(this.title)) {
            logger.error("Attempted to update maintopic with unchanged title",this.title);
            throw new BadRequestException("同じ内容です", "Bad_Request");
        }
        if (description.equals(this.description)) {
            logger.error("Attempted to update maintopic with unchanged description",this.description);
            throw new BadRequestException("同じ内容です", "Bad_Request");
        }
        
        return new Maintopic(
            this.maintopicId,
            title.getValue(),
            description.getValue(),
            this.createdAt,
            LocalDateTime.now(),
            this.isDeleted,
            this.isClosed
        );
    }
}
