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
     * ディスカッションを新規作成してデータベースに保存する
     * ドメインエンティティをインフラストラクチャ層のエンティティに変換し、
     * 保存後に再度ドメインエンティティとして返却する
     * 
     * @param discussion 作成するディスカッションドメインエンティティ
     * @return 保存されたディスカッションドメインエンティティ
     */
    @Override
    public Discussion createDiscussion(Discussion discussion) {
        logger.info("Creating discussion with paragraph: {}", discussion.getParagraph());
        Discussions entity = new Discussions();
        entity.setParagraph(discussion.getParagraph());
        Discussions savedEntity = jpaDiscussionsRepository.save(entity);
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
