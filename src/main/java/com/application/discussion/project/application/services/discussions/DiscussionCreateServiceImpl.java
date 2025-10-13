package com.application.discussion.project.application.services.discussions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.domain.valueobjects.discussions.Paragraph;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;

/**
 * ディスカッション作成サービスの実装クラス
 * メイントピックに関連するディスカッションの作成処理を担当する
 */
@Service
public class DiscussionCreateServiceImpl implements DiscussionCreateService {
    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(DiscussionCreateServiceImpl.class);

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
    public DiscussionCreateResponse service(
        final Long maintopicId, 
        final DiscussionCreateRequest discussionCreateRequest
    ){
        logger.info("Creating discussion for maintopicId: {}", maintopicId);
        final Discussion discussion = Discussion.create(
            Paragraph.of(discussionCreateRequest.getParagraph()),
            maintopicId
        );
        
        final Maintopics maintopicEntity = maintopicRepository.findModelById(maintopicId);
        final Discussions entity = new Discussions();
        entity.setParagraph(discussion.getParagraph());
        entity.setMaintopic(maintopicEntity);
        final Discussion createdDiscussion = discussionRepository.createDiscussion(entity);
        logger.info("Discussion created with ID: {}", createdDiscussion.getDiscussionId());
        return new DiscussionCreateResponse(
            createdDiscussion.getDiscussionId(),
            createdDiscussion.getParagraph(),
            createdDiscussion.getMaintopicId()
        );
    }
}
