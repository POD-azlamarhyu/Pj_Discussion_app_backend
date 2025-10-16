package com.application.discussion.project.infrastructure.repositories.discussions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;

import java.util.List;
import java.util.Optional;

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
     * @param discussions 作成するディスカッションインフラストラクチャエンティティ
     * @return 保存されたディスカッションドメインエンティティ（IDや日時が設定済み）
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

    /**
     * 指定されたIDのディスカッションを取得する
     * データベースから該当するディスカッションを検索し、
     * 見つかった場合はドメインエンティティに変換して返却する
     * 
     * @param discussionId 取得対象のディスカッションID
     * @return 取得されたディスカッションドメインエンティティを含むOptional（存在しない場合はOptional.empty()）
     */
    @Override
    public Optional<Discussion> findDiscussionById(final Long discussionId) {
        logger.info("Finding discussion with ID: {}", discussionId);
        return jpaDiscussionsRepository.findById(discussionId)
            .map(entity -> {
                logger.info("Discussion found with ID: {}", entity.getId());
                return Discussion.of(
                    entity.getId(),
                    entity.getParagraph(),
                    entity.getMaintopic().getId(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt(),
                    entity.getDeletedAt()
                );
            });
    }

    /**
     * 指定されたメイントピックIDに紐づく全てのディスカッションを取得する
     * データベースから該当するメイントピックに関連するディスカッションを全て検索し、
     * ドメインエンティティのリストに変換して返却する
     * 
     * @param maintopicId 取得対象のメイントピックID
     * @return 取得されたディスカッションドメインエンティティのリスト（該当なしの場合は空のリスト）
     */
    @Override
    public List<Discussion> findAllDiscussions(final Long maintopicId) {
        logger.info("Finding all discussions for maintopic ID: {}", maintopicId);
        final List<Discussions> entities = jpaDiscussionsRepository.findByMaintopicId(maintopicId);
        logger.info("Found {} discussions for maintopic ID: {}", entities.size(), maintopicId);
        return entities.stream()
            .map(entity -> Discussion.of(
                entity.getId(),
                entity.getParagraph(),
                entity.getMaintopic().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
            ))
            .toList();
    }

    /**
     * ページネーション対応で全てのディスカッションを取得する
     * データベースから指定されたページング情報に基づいてディスカッションを検索し、
     * ドメインエンティティのPageオブジェクトに変換して返却する
     * 
     * @param pageable ページネーション情報（ページ番号、ページサイズ、ソート条件など）
     * @return ページング情報を含むディスカッションドメインエンティティのPageオブジェクト
     */
    @Override
    public Page<Discussion> findAllDiscussions(final Pageable pageable) {
        logger.info("Finding all discussions with pagination: page {}, size {}", 
            pageable.getPageNumber(), pageable.getPageSize());
        final Page<Discussions> entitiesPage = jpaDiscussionsRepository.findAll(pageable);
        logger.info("Found {} discussions in total", entitiesPage.getTotalElements());
        return entitiesPage.map(entity -> Discussion.of(
            entity.getId(),
            entity.getParagraph(),
            entity.getMaintopic().getId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt()
        ));
    }
}
