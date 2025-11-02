package com.application.discussion.project.application.services.discussions;

import com.application.discussion.project.application.dtos.discussions.DiscussionListResponse;
import org.springframework.data.domain.Pageable;

/**
 * 議論リスト取得サービスのインターフェース
 */
public interface DiscussionListService {

    /**
     * 議論のリストを取得する
     * @param maintopicId メイントピックID
     * @param pageable ページネーション情報
     * @return 議論リストのレスポンス
     */
    DiscussionListResponse service(
        Long maintopicId,
        Pageable pageable
    );

}
