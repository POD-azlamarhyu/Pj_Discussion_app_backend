package com.application.discussion.project.infrastructure.repositories.discussions;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.discussion.project.infrastructure.models.discussions.Discussions;

public interface JpaDiscussionsRepository extends JpaRepository<Discussions, Long> {

}
