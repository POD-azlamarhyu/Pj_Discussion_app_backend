package com.application.discussion.project.domain.services.users;

import com.application.discussion.project.domain.valueobjects.users.Email;

public interface UserRegistrationDomainService {
    void ensureEmailIsUnique(Email email);
}
