package com.application.discussion.project.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;

public interface MaintopicRepository {
    List<Maintopic> findMaintopicList();

    Maintopic findMaintopicById(Long maintopicId);

    Maintopic createMaintopic(Maintopic maintopic);

    Maintopics findModelById(Long maintopicId);

    Maintopic updateMaintopic(Maintopics updateMaintopics);

    public void deleteMaintopic(Long maintopicId);

    public Boolean existsMaintopic(Long maintopicId);
}
