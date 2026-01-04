package com.application.discussion.project.domain.services.users;

import java.util.List;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

public interface RoleRegistrationDomainService {
    Boolean ensureRoleIsUnique(RoleType roleType);

    Boolean ensureUserRolesAreValid(User user, Role role);
}
