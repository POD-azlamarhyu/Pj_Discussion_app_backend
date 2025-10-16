package com.application.discussion.project.domain.repositories;

import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * ディスカッションリポジトリインターフェイス
 * ディスカッションエンティティの永続化処理を定義するドメイン層のリポジトリ
 * 実装はインフラストラクチャ層で行われる
 */
public interface DiscussionRepository {
    
    /**
     * ディスカッションを新規作成してデータストアに保存する
     * 
     * @param discussion 保存するディスカッションエンティティ
     * @return 保存されたディスカッションエンティティ（IDや日時が設定済み）
     */
    Discussion createDiscussion(Discussions discussions);
    
    /**
     * 指定されたIDのディスカッションを取得する
     * 
     * @param discussionId 取得対象のディスカッションID
     * @return 取得されたディスカッションエンティティ（存在しない場合はOptional.empty()）
     */
    Optional<Discussion> findDiscussionById(Long discussionId);
    
    /**
     * 全てのディスカッションを取得する
     * 
     * @return 全てのディスカッションエンティティのリスト
     */
    List<Discussion> findAllDiscussions(Long maintopicId);
    
    /**
     * ページネーション対応でディスカッションを取得する
     * 
     * @param pageable ページネーション情報
     * @return ページング情報を含むディスカッションエンティティのリスト
     */
    Page<Discussion> findAllDiscussions(Pageable pageable);
}
