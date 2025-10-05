package com.application.discussion.project.application.services.discussions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import com.application.discussion.project.domain.valueobjects.discussions.Paragraph;

/**
 * ディスカッション作成サービスの実装クラス
 * メイントピックに関連するディスカッションの作成処理を担当する
 */
@Service
public class DiscussionCreateServiceImpl implements DiscussionCreateService {
    @Autowired
    private DiscussionRepository discussionRepository;

    /**
     * 指定されたメイントピックに対してディスカッションを作成する
     * リクエストから本文を取得してParagraphバリューオブジェクトに変換し、
     * ディスカッションエンティティを生成してリポジトリに保存する
     * 
     * @param maintopicId 関連付けるメイントピックのID
     * @param discussionCreateRequest ディスカッション作成に必要な情報を含むリクエストDTO
     * @return 作成されたディスカッションのレスポンスDTO
     */
    @Override
    public DiscussionCreateResponse service(Long maintopicId, DiscussionCreateRequest discussionCreateRequest){
        Discussion discussion = Discussion.create(
            Paragraph.of(discussionCreateRequest.getParagraph()),
            maintopicId
        );
        Discussion createdDiscussion = discussionRepository.createDiscussion(discussion);

        return new DiscussionCreateResponse(
            createdDiscussion.getDiscussionId(),
            createdDiscussion.getParagraph(),
            createdDiscussion.getMaintopicId()
        );
    }
}
