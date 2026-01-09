package com.application.discussion.project.application.services.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.domain.services.users.UserAuthenticationDomainService;

@Service
public class MaintopicCreateServiceImpl implements MaintopicCreateService {
    
    @Autowired
    private MaintopicRepository maintopicRepository;

    @Autowired
    private UserAuthenticationDomainService userAuthenticationDomainService;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicCreateServiceImpl.class);

    @Override
    public MaintopicCreateResponse service(final MaintopicCreateRequest maintopicCreateRequest){
        logger.info("Starting maintopic creation service");
        final User authenticatedUser = userAuthenticationDomainService.getAuthenticatedUser();
        logger.info("Authenticated user ID: {}", authenticatedUser.getUserId());

        try{
            logger.info("Creating a new maintopic with title: {}", maintopicCreateRequest.getTitle());
            final Title title = Title.of(maintopicCreateRequest.getTitle());
            final Description description = Description.of(maintopicCreateRequest.getDescription());
            final Maintopic maintopic = Maintopic.create(title, description, authenticatedUser.getUserId());
            
            final Maintopic savedMaintopic = maintopicRepository.createMaintopic(maintopic, authenticatedUser.getUserId());
            logger.info("Maintopic created successfully with ID: {}", savedMaintopic.getMaintopicId());
            return new MaintopicCreateResponse(
                savedMaintopic.getMaintopicId(),
                savedMaintopic.getTitle(),
                savedMaintopic.getDescription(),
                savedMaintopic.getCreatedAt().toString()
            );
        }catch (ApplicationLayerException e){
            logger.error("Error creating maintopic: {}", e.getMessage());
            throw new InternalServerErrorException("登録に失敗しました","INTERNAL_SERVER_ERROR");
        }
    }
}
