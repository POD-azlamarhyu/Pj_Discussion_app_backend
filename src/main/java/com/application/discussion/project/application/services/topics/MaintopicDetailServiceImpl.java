package com.application.discussion.project.application.services.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.topics.MaintopicResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;

@Service
public class MaintopicDetailServiceImpl implements MaintopicDetailService {

    @Autowired
    private MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicDetailServiceImpl.class);

    @Override
    public MaintopicResponse service(Long id) {
        // Implementation logic to retrieve the details of a main topic by its ID
        // This is a placeholder implementation and should be replaced with actual logic
        logger.info("Fetching maintopic details for ID: {}", id);
        Maintopic maintopic = maintopicRepository.findMaintopicById(id);
        MaintopicResponse maintopicResponse = new MaintopicResponse(
                maintopic.getMaintopicId(),
                maintopic.getTitle(),
                maintopic.getDescription(),
                maintopic.getCreatedAt(),
                maintopic.getUpdatedAt(),
                maintopic.getIsDeleted(),
                maintopic.getIsClosed()
        );
        logger.info("Maintopic details retrieved successfully for ID: {}", id);
        return maintopicResponse;
    }

}
