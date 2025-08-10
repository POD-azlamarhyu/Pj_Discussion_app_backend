package com.application.discussion.project.application.services.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;

@Service
public class MaintopicCreateServiceImpl implements MaintopicCreateService {
    
    @Autowired
    private MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicCreateServiceImpl.class);

    @Override
    public MaintopicCreateResponse service(final MaintopicCreateRequest maintopicCreateRequest){
        // Validate the request
        try{
            logger.info("Creating a new maintopic with title: {}", maintopicCreateRequest.getTitle());
            final Title title = Title.of(maintopicCreateRequest.getTitle());
            final Description description = Description.of(maintopicCreateRequest.getDescription());
            final Maintopic maintopic = Maintopic.create(title, description);
            
            // Save the Maintopic entity
            final Maintopic savedMaintopic = maintopicRepository.createMaintopic(maintopic);
            return new MaintopicCreateResponse(
                savedMaintopic.getMaintopicId(),
                savedMaintopic.getTitle(),
                savedMaintopic.getDescription(),
                savedMaintopic.getCreatedAt().toString()
            );
        }catch (Exception e){
            logger.error("Error creating maintopic: {}", e);
            throw new InternalServerErrorException("登録に失敗しました。","INTERNAL_SERVER_ERROR");
        }
        
    }
}
