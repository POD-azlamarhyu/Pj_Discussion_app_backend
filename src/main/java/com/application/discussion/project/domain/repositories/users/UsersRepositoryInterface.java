package com.application.discussion.project.domain.repositories.users;

import java.util.Optional;
import java.util.UUID;

import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.valueobjects.users.Email;
import com.application.discussion.project.infrastructure.models.users.Users;

public interface UsersRepositoryInterface {
    
    Optional<Users> findByEmailOrLoginId(String emailOrLoginId);
    
    void saveSeededUsers(Users user);
    
    Boolean existsByEmail(Email email);

    User save(User user);

    Optional<User> findById(UUID userId);
}
