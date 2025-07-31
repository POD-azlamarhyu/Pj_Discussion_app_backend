package com.application.discussion.project.infrastructure.repositories.topics;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;

@Repository
public class MaintopicRepositoryImpl implements MaintopicRepository {

    private final JpaMaintopicsRepository jpaMaintopicsRepository;

    private static final Logger logger = LoggerFactory.getLogger(MaintopicRepositoryImpl.class);

    public MaintopicRepositoryImpl(JpaMaintopicsRepository jpaMaintopicsRepository) {
        this.jpaMaintopicsRepository = jpaMaintopicsRepository;
    }
    @Override
    public Maintopic findMaintopicById(Long maintopicId) {
        logger.info("Finding maintopic by ID: {}", maintopicId);
        return jpaMaintopicsRepository.findById(maintopicId)
                .map(entity -> new Maintopic(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt(),
                        entity.getIsDeleted(),
                        entity.getIsClosed()
                ))
                .orElseThrow(() -> {
                    logger.error("Maintopic with ID {} not found", maintopicId);
                    return new RuntimeException("Topic not found");
                });
    }

    @Override
    public List<Maintopic> findMaintopicList() {
        List<Maintopics> maintopics = jpaMaintopicsRepository.findAll();
        logger.info("Retrieved {} maintopics from the database", maintopics.size());
        if (maintopics.isEmpty()) {
            logger.warn("No maintopics found in the database");
            throw new RuntimeException("No topics found");
            // Return an empty list if no topics are found
        }
        return maintopics.stream()
                .map(entity -> new Maintopic(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt(),
                        entity.getIsDeleted(),
                        entity.getIsClosed()
                ))
                .collect(Collectors.toList());
    }
}
