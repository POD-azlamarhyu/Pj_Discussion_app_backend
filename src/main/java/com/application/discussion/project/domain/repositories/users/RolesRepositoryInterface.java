package com.application.discussion.project.domain.repositories.users;

import java.util.Set;
import java.util.UUID;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

public interface RolesRepositoryInterface {

    Set<Role> findUserRolesById(UUID userId);

    Boolean existsByRoleName(RoleType roleType);

    Boolean existsByUserAndRoleBoolean(User user, Role role);

    Role saveRole(Role role);

    Role findByRoleName(RoleType roleType);

    void saveUserRoleMapping(User user, Role role);
}
