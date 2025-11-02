package com.application.discussion.project.application.services.discussions;

import com.application.discussion.project.application.dtos.discussions.DiscussionListResponse;
import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.application.discussion.project.application.dtos.discussions.DiscussionResponse;

/**
 * 議論リスト取得サービスの実装クラス
 */
@Service
@Transactional(readOnly = true)
public class DiscussionListServiceImpl implements DiscussionListService {

    @Autowired
    private DiscussionRepository discussionRepository;

    private static final Logger logger = LoggerFactory.getLogger(DiscussionListServiceImpl.class); 

    /**
     * 議論のリストを取得する
     *
     * @param maintopicId メイントピックID
     * @param pageable ページネーション情報
     * @return 議論リストのレスポンス
     */
    @Override
    public DiscussionListResponse service(Long maintopicId,Pageable pageable) {
        logger.info("getting discussion list: {}, size: {}", 
            pageable.getPageNumber(), pageable.getPageSize());

        Page<Discussion> discussionPage = discussionRepository.findAllDiscussions(maintopicId,pageable);

        return buildDiscussionListResponse(discussionPage);
    }


    /**
     * DiscussionエンティティのページからDiscussionListResponseを構築する
     *
     * @param discussionPage 議論エンティティのページ
     * @return 議論リストのレスポンス
     */
    private DiscussionListResponse buildDiscussionListResponse(Page<Discussion> discussionPage) {

        logger.info("Building DiscussionListResponse from discussion page");
        List<DiscussionResponse> discussionDtos = discussionPage.getContent().stream()
            .map(this::convertToDto)
            .toList();
        Integer totalCount = (int) discussionPage.getTotalElements();
        Integer currentPage = discussionPage.getNumber();
        Integer pageSize = discussionPage.getSize();
        Integer totalPages = discussionPage.getTotalPages();

        return DiscussionListResponse.of(discussionDtos, totalCount, currentPage, pageSize, totalPages);
    }

    /**
     * DiscussionエンティティをDiscussionDtoに変換する
     *
     * @param discussion 議論エンティティ
     * @return 議論DTO
     */
    private DiscussionResponse convertToDto(Discussion discussion) {

        logger.info("Converting Discussion entity to DiscussionResponse DTO: id {}", discussion.getDiscussionId());
        return DiscussionResponse.of(
            discussion.getDiscussionId(),
            discussion.getParagraph(),
            discussion.getMaintopicId(),
            discussion.getCreatedAt(),
            discussion.getUpdatedAt()
        );
    }
}
