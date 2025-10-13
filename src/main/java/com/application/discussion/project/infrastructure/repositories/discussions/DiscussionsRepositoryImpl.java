package com.application.discussion.project.infrastructure.repositories.discussions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;

/**
 * ディスカッションリポジトリの実装クラス
 * ドメイン層のDiscussionRepositoryインターフェイスを実装し、
 * データベースへのディスカッション永続化処理を担当する
 */
@Repository
public class DiscussionsRepositoryImpl implements DiscussionRepository {
    private final JpaDiscussionsRepository jpaDiscussionsRepository;

    private static final Logger logger = LoggerFactory.getLogger(DiscussionsRepositoryImpl.class);

    /**
     * コンストラクタ
     * 
     * @param jpaDiscussionsRepository JPA用のディスカッションリポジトリ
     */
    public DiscussionsRepositoryImpl(JpaDiscussionsRepository jpaDiscussionsRepository) {
        this.jpaDiscussionsRepository = jpaDiscussionsRepository;
    }

    /**
     * FIXME: このメソッドには、例外処理を追加する必要があります。
     *       例えば、データベース接続エラーやデータ整合性エラーなどのシナリオを考慮してください。
     * NOTE: 例外処理については、今のところは時間的な理由で実装していませんが、将来的には追加する予定です。
     */

    /**
     * ディスカッションを新規作成してデータベースに保存する
     * ドメインエンティティをインフラストラクチャ層のエンティティに変換し、
     * 保存後に再度ドメインエンティティとして返却する
     * 
     * @param discussion 作成するディスカッションドメインエンティティ
     * @return 保存されたディスカッションドメインエンティティ
     */
    @Override
    public Discussion createDiscussion(final Discussions discussions) {
        logger.info("Creating discussion with paragraph: {}", discussions.getParagraph());
        final Discussions savedEntity = jpaDiscussionsRepository.save(discussions);
        logger.info("Discussion created with ID: {}", savedEntity.getId());
        return Discussion.of(
            savedEntity.getId(),
            savedEntity.getParagraph(),
            savedEntity.getMaintopic().getId(),
            savedEntity.getCreatedAt(),
            savedEntity.getUpdatedAt(),
            savedEntity.getDeletedAt()
        );
    }
}
