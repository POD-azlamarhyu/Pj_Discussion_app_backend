package com.application.discussion.project.domain.entities.discussions;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.discussions.Paragraph;

import lombok.Builder;

/**
 * ディスカッションエンティティ
 * 議論の基本情報と状態を管理するドメインモデル
 */
@Builder
public class Discussion {

    private final Long discussionId;
    private final Paragraph paragraph;
    private final Long maintopicId;

    /**
     * TODO: 議論を投稿したのが誰かを示すユーザーID
     * 現時点ではUserモデルが存在しないため，コメントアウトで対応．
     * 将来的にUserモデルが実装された際に，コメントアウトを解除して使用する
     */
    // private Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    private static final Logger logger = LoggerFactory.getLogger(Discussion.class);

    /**
     * ディスカッションエンティティのコンストラクタ
     * 
     * @param discussionId ディスカッションの一意識別子
     * @param paragraph ディスカッションの本文
     * @param maintopicId 関連するメイントピックのID
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     */
    public Discussion(
        Long discussionId,
        Paragraph paragraph,
        Long maintopicId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
        
    ) {
        if (paragraph == null){
            throw new DomainLayerErrorException(
                "Paragraph must not be null when creating a Discussion.",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }

        if (maintopicId == null){
            throw new DomainLayerErrorException(
                "MaintopicId must not be null when creating a Discussion.",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
        this.discussionId = discussionId;
        this.paragraph = paragraph;
        this.createdAt = createdAt;
        this.maintopicId = maintopicId;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * データベースから取得したデータを元にディスカッションエンティティを復元するファクトリメソッド
     * 
     * @param discussionId ディスカッションの一意識別子
     * @param paragraph ディスカッションの本文（文字列）
     * @param maintopicId 関連するメイントピックのID
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 復元されたディスカッションエンティティ
     */
    public static Discussion of(
        Long discussionId,
        String paragraph,
        Long maintopicId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        return new Discussion(
            discussionId, 
            Paragraph.of(paragraph),
            maintopicId,
            createdAt, 
            updatedAt, 
            deletedAt
        );
    }

    /**
     * 新しいディスカッションを作成するファクトリメソッド
     * IDや日時は初期値で設定され、永続化時に適切な値が設定される
     * 
     * @param paragraph ディスカッションの本文
     * @param maintopicId 関連するメイントピックのID
     * @return 新規作成されたディスカッションエンティティ
     */
    public static Discussion create(
        final Paragraph paragraph,
        final Long maintopicId
    ){  
        logger.info("Creating new Discussion with paragraph: {}", paragraph);

        
        return new Discussion(
            Long.valueOf(0),
            paragraph,
            maintopicId,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
            LocalDateTime.MIN
        );
    }

    /**
     * ディスカッションIDを取得する
     * 
     * @return ディスカッションの一意識別子
     */
    public Long getDiscussionId() {
        return this.discussionId;
    }
    
    /**
     * ディスカッションの本文を取得する
     * 
     * @return ディスカッションの本文
     */
    public String getParagraph() {
        return this.paragraph.getValue();
    }
    
    /**
     * 関連するメイントピックのIDを取得する
     * 
     * @return メイントピックの一意識別子
     */
    public Long getMaintopicId() {
        return this.maintopicId;
    }
    
    /**
     * ディスカッションの作成日時を取得する
     * 
     * @return 作成日時
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    
    /**
     * ディスカッションの更新日時を取得する
     * 
     * @return 最終更新日時
     */
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
    
    /**
     * ディスカッションの削除日時を取得する
     * 
     * @return 削除日時（論理削除）
     */
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }
}
