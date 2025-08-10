package com.application.discussion.project.application.services.topics;

import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;

public interface MaintopicCreateService {
    MaintopicCreateResponse service(final MaintopicCreateRequest maintopicCreateRequest);
}
