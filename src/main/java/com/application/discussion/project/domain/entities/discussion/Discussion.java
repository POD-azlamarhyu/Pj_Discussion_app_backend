package com.application.discussion.project.domain.entities.discussion;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.valueobjects.discussion.Paragraph;

import lombok.Builder;

/**
 * ディスカッションエンティティ
 * 議論の基本情報と状態を管理するドメインモデル
 */
@Builder
public class Discussion {

    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_DESCRIPTION_LENGTH = 2000;

    private final Long discussionId;
    private final String paragraph;

    /**
     * TODO: 議論を投稿したのが誰かを示すユーザーID
     * 現時点ではUserモデルが存在しないため，コメントアウトで対応．
     * 将来的にUserモデルが実装された際に，コメントアウトを解除して使用する
     */
    // private Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    private static final Logger logger = LoggerFactory.getLogger(Maintopic.class);

    
    public Discussion(
        Long discussionId,
        String paragraph,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
        
    ) {
        this.discussionId = discussionId;
        this.paragraph = paragraph;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * GetなどでDBから取得したときにドメインモデルを生成するファクトリメソッド
     * @param discussionId
     * @param paragraph
     * @param createdAt
     * @param updatedAt
     * @param deletedAt
     * @return
     */
    public static Discussion of(
        Long discussionId,
        String paragraph,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        return new Discussion(
            discussionId, 
            paragraph, 
            createdAt, 
            updatedAt, 
            deletedAt
        );
    }

    /**
     * 新しい議論を作成する際にドメインモデルを生成するファクトリメソッド
     * @param paragraph
     * @return
     */
    public static Discussion create(
        Paragraph paragraph
    ){  
        logger.info("Creating new Discussion with paragraph: {}", paragraph);
        return new Discussion(
            Long.valueOf(0),
            paragraph.getValue(),
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            LocalDateTime.MIN
        );
    }

    public Long getDiscussionId() {
        return this.discussionId;
    }
    public String getParagraph() {
        return this.paragraph;
    }
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }
}
