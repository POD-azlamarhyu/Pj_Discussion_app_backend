package com.application.discussion.project.infrastructure.repositories.topics;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.models.topics.Maintopics;

@Repository
public interface JpaMaintopicsRepository extends JpaRepository<Maintopics, Long> {
    // カスタムクエリメソッドを必要に応じて追加できます
    Boolean existsByTitle(String title);
}
