package com.application.discussion.project.presentation.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.application.dtos.topics.MaintopicUpdateRequest;
import com.application.discussion.project.presentation.exceptions.RequestNotValidException;

public class MaintopicUpdateRequestValidations {
    private static final String EATHER_REQUIRED = "タイトル，説明のどちらかは必須です";
    private static final String TYPE = "Request_Not_Valid";
    private static final Logger logger = LoggerFactory.getLogger(MaintopicUpdateRequestValidations.class);

    private static Boolean isValidateTitle(final String title) {
        return title.isBlank() || title.isEmpty();
    }
    
    private static Boolean isValidateDescription(final String description) {
        return description.isBlank() || description.isEmpty();
    }

    public static void isValidateMaintopicUpdate(
        final MaintopicUpdateRequest maintopicUpdateRequest
    ){
        if (isValidateTitle(maintopicUpdateRequest.getTitle()) || isValidateDescription(maintopicUpdateRequest.getDescription())) {
            logger.error("Title is required and cannot be null");
            throw new RequestNotValidException(EATHER_REQUIRED, TYPE);
        }
    }
}
