package com.application.discussion.project.application.services.users;

import com.application.discussion.project.application.dtos.users.SignUpRequest;
import com.application.discussion.project.application.dtos.users.SignUpResponse;

public interface UserRegistrationService {
    SignUpResponse service(SignUpRequest request);
}
