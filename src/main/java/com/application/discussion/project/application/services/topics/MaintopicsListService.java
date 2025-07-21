package com.application.discussion.project.application.services.topics;

import java.util.List;

import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;

public interface  MaintopicsListService {
    List<MaintopicListResponse> service();
}
