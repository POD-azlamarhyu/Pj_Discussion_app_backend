package com.application.discussion.project.application.services.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;

@Service
public class MaintopicUpdateServiceImpl implements MaintopicUpdateService {

    @Autowired
    private MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicUpdateServiceImpl.class);

    /**
     * メイントピックを更新をするためのサービス
     * @param id メイントピックのID
     * @param maintopicUpdateRequest 更新内容を含むリクエストオブジェクト
     * @return 更新されたメイントピックの情報を含むレスポンスオブジェクト  
     */
    @Override
    public MaintopicUpdateResponse service(
        final Long id, 
        final MaintopicUpdateRequest maintopicUpdateRequest
    ){
        try {
            logger.info("Starting update service for maintopic ID: {}", id);
            Maintopics maintopicEntity = maintopicRepository.findModelById(id);
            Maintopic existingMaintopic = maintopicRepository.findMaintopicById(id);
            Maintopic updateMaintopic = existingMaintopic.update(
                Title.of(maintopicUpdateRequest.getTitle()),
                Description.of(maintopicUpdateRequest.getDescription())
            );

            maintopicEntity.setTitle(updateMaintopic.getTitle());
            maintopicEntity.setDescription(updateMaintopic.getDescription());
            Maintopic updatedMaintopic = maintopicRepository.updateMaintopic(maintopicEntity);

            logger.info("Successfully updated maintopic with ID: {}", id);
            return new MaintopicUpdateResponse(
                updatedMaintopic.getMaintopicId(),
                updatedMaintopic.getTitle(),
                updatedMaintopic.getDescription(),
                updatedMaintopic.getCreatedAt(),
                updatedMaintopic.getUpdatedAt()
            );
        } catch (Exception e) {
            logger.error("Error occurred while updating maintopic with ID: {}", id, e);
            throw new InternalServerErrorException("アップデートに失敗しました．","Internal_Server_Error");
        }
        
    }
}
