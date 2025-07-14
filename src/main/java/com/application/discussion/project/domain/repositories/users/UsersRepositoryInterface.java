package com.application.discussion.project.domain.repositories.users;

import java.util.Optional;


import com.application.discussion.project.infrastructure.models.users.Users;

public interface UsersRepositoryInterface {

    Optional<Users> findByEmailOrLoginId(String emailOrLoginId);
}
