package com.application.discussion.project.application.services.topics;

import com.application.discussion.project.application.dtos.topics.MaintopicUpdateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateResponse;

public interface MaintopicUpdateService {
    MaintopicUpdateResponse service(final Long id, final MaintopicUpdateRequest maintopicUpdateRequest);
}
