package com.application.discussion.project.domain.services.users;

import com.application.discussion.project.domain.entities.users.User;

public interface UserAuthenticationDomainService {
    User getAuthenticatedUser();
}
