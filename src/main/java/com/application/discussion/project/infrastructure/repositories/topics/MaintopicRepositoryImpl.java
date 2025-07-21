package com.application.discussion.project.infrastructure.repositories.topics;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;

@Repository
public class MaintopicRepositoryImpl implements MaintopicRepository {

    private final JpaMaintopicsRepository jpaMaintopicsRepository;

    public MaintopicRepositoryImpl(JpaMaintopicsRepository jpaMaintopicsRepository) {
        this.jpaMaintopicsRepository = jpaMaintopicsRepository;
    }

    @Override
    public List<Maintopic> findMaintopicList() {
        List<Maintopics> maintopics = jpaMaintopicsRepository.findAll();
        if (maintopics.isEmpty()) {
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
