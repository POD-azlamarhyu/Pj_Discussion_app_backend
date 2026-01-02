package com.application.discussion.project.infrastructure.repositories.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.models.users.Users;

@Repository
public interface JpaUsersRepository extends JpaRepository<Users, UUID> {
    
    Optional<Users> findByEmailOrLoginId(String email, String loginId);
    
    Boolean existsByEmail(String email);
}
