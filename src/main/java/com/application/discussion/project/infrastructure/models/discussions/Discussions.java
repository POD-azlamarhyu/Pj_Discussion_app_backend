package com.application.discussion.project.infrastructure.models.discussions;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.models.users.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;

/**
 * ディスカッションテーブルのJPAエンティティクラス
 * データベースのdiscussionsテーブルとのマッピングを定義し、
 * ディスカッション情報の永続化を担当する
 */
@Entity
@Table(name="discussions")
@Builder
public class Discussions {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, updatable=false)
    private Long id;

    @Column(name="paragraph", nullable=false, length=1000)
    private String paragraph;

    @ManyToOne
    @JoinColumn(name="maintopic_id", nullable=false)
    private Maintopics maintopic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false, referencedColumnName="user_id")
    private Users user;

    @Column(name="created_at", nullable=false, updatable=false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=true)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name="deleted_at", nullable=true)
    private LocalDateTime deletedAt;

    /**
     * デフォルトコンストラクタ
     * JPAの要件により必要
     */
    public Discussions() {}

    /**
     * 全フィールドを初期化するコンストラクタ
     * 
     * @param id ディスカッションID
     * @param paragraph ディスカッションの本文
     * @param maintopic 関連するメイントピック
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     */
    public Discussions(
        Long id,
        String paragraph,
        Maintopics maintopic,
        Users user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        this.id = id;
        this.paragraph = paragraph;
        this.maintopic = maintopic;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * ディスカッションIDを取得する
     * 
     * @return ディスカッションの一意識別子
     */
    public Long getId() {
        return id;
    }

    /**
     * ディスカッションIDを設定する
     * 
     * @param id 設定するディスカッションID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ディスカッションの本文を取得する
     * 
     * @return ディスカッションの本文
     */
    public String getParagraph() {
        return paragraph;
    }

    /**
     * ディスカッションの本文を設定する
     * 
     * @param paragraph 設定するディスカッションの本文
     */
    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    /**
     * 関連するメイントピックを取得する
     * 
     * @return 関連するメイントピックエンティティ
     */
    public Maintopics getMaintopic() {
        return maintopic;
    }

    /**
     * 関連するメイントピックを設定する
     * 
     * @param maintopic 設定するメイントピックエンティティ
     */
    public void setMaintopic(Maintopics maintopic) {
        this.maintopic = maintopic;
    }

    /**
     * ディスカッションの作成日時を取得する
     * 
     * @return 作成日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * ディスカッションの作成日時を設定する
     * 
     * @param createdAt 設定する作成日時
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * ディスカッションの更新日時を取得する
     * 
     * @return 最終更新日時
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * ディスカッションの更新日時を設定する
     * 
     * @param updatedAt 設定する更新日時
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * ディスカッションの削除日時を取得する
     * 
     * @return 削除日時（論理削除）
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * ディスカッションの削除日時を設定する
     * 
     * @param deletedAt 設定する削除日時（論理削除用）
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * ディスカッションを投稿したユーザーを取得する
     * 
     * @return ユーザーエンティティ
     */
    public Users getUser() {
        return user;
    }

    /**
     * ディスカッションを投稿したユーザーを設定する
     * 
     * @param user 設定するユーザーエンティティ
     */
    public void setUser(Users user) {
        this.user = user;
    }
}
