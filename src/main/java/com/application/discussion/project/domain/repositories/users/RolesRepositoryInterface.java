package com.application.discussion.project.domain.repositories.users;

import java.util.Set;
import java.util.UUID;


import com.application.discussion.project.infrastructure.models.users.Roles;

public interface RolesRepositoryInterface {

    Set<Roles> findUserRolesById(UUID userId);
}
