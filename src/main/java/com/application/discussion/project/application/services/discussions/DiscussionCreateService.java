package com.application.discussion.project.application.services.discussions;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;

/**
 * ディスカッション作成サービスインターフェイス
 * メイントピックに関連するディスカッションの作成処理を定義する
 */
public interface DiscussionCreateService {
    
    /**
     * 指定されたメイントピックに対してディスカッションを作成する
     * 
     * @param maintopicId 関連付けるメイントピックのID
     * @param discussionCreateRequest ディスカッション作成に必要な情報を含むリクエストDTO
     * @return 作成されたディスカッションのレスポンスDTO
     */
    DiscussionCreateResponse service(Long maintopicId, DiscussionCreateRequest discussionCreateRequest);
}
