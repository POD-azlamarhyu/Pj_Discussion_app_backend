package com.application.discussion.project.application.services.topics;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.domain.repositories.MaintopicRepository;

@Service
public class MaintopicsListServiceImpl implements MaintopicsListService {

    private final MaintopicRepository maintopicRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicsListServiceImpl.class);

    public MaintopicsListServiceImpl(MaintopicRepository maintopicRepository) {
        this.maintopicRepository = maintopicRepository;
    }

    @Override
    public List<MaintopicListResponse> service() {
        logger.info("Fetching list of maintopics");
        List<MaintopicListResponse> maintopicList = maintopicRepository.findMaintopicList().stream()
                .map(maintopic -> new MaintopicListResponse(
                        maintopic.getMaintopicId(),
                        maintopic.getTitle(),
                        maintopic.getDescription(),
                        maintopic.getCreatedAt(),
                        maintopic.getUpdatedAt(),
                        maintopic.getIsDeleted(),
                        maintopic.getIsClosed()
                ))
                .collect(Collectors.toList());
        
        return maintopicList;
    }

}
