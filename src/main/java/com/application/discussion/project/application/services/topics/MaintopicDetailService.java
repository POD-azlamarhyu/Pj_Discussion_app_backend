package com.application.discussion.project.application.services.topics;


import com.application.discussion.project.application.dtos.topics.MaintopicResponse;

public interface MaintopicDetailService {
    MaintopicResponse service(Long id);
}
