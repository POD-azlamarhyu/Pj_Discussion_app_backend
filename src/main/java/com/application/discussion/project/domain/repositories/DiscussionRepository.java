package com.application.discussion.project.domain.repositories;

import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;

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
}
