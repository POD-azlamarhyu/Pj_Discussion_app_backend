package com.application.discussion.project.infrastructure.repositories.discussions;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;

public interface JpaDiscussionsRepository extends JpaRepository<Discussions, Long> {

    /**
     * ディスカッションIDでディスカッションを取得する
     * @param discussionId 議論ID
     * @return 議論エンティティ
     */
    Optional<Discussions> findById(Long id);

    /**
     * メイントピックIDで関連するディスカッションを取得する
     * @param maintopicId メイントピックID
     * @return 議論エンティティのリスト
     */
    List<Discussions> findByMaintopicId(Long maintopicId);

    /**
     * メイントピックIDで関連するディスカッションをページング付きで取得する
     *
     * @param maintopicId メイントピックID
     * @param pageable ページネーション情報
     * @return 議論エンティティのページ
     */
    Page<Discussions> findByMaintopicId(Long maintopicId, Pageable pageable);
}
