package com.application.discussion.project.domain.repositories;

import java.util.List;

import com.application.discussion.project.domain.entities.topics.Maintopic;

public interface MaintopicRepository {
    List<Maintopic> findMaintopicList();
}
