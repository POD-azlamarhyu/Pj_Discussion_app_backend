package com.application.discussion.project.application.services.topics;

import org.springframework.beans.factory.annotation.Autowired;

import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;

public class MaintopicCreateServiceImpl implements MaintopicCreateService {
    
    @Autowired
    private MaintopicRepository maintopicRepository;

    @Override
    public MaintopicCreateResponse service(final MaintopicCreateRequest maintopicCreateRequest){
        // Validate the request
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
    }
}
