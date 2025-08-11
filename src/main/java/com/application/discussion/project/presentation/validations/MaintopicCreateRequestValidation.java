package com.application.discussion.project.presentation.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.application.discussion.project.presentation.exceptions.RequestNotValidException;

public class MaintopicCreateRequestValidation {
    private static final String TITLE_REQUIRED = "タイトルは必須です";
    private static final String TITLE_NOT_BLANK = "タイトルは空白にできません";
    private static final String TITLE_SIZE = "タイトルは3文字以上100文字以下で入力してください";
    
    private static final String DESCRIPTION_REQUIRED = "説明は必須です";
    private static final String DESCRIPTION_NOT_BLANK = "説明は空白にできません";
    private static final String DESCRIPTION_SIZE = "説明は10文字以上500文字以下で入力してください";
    private static final String TYPE = "Request_Not_Valid";
    private static final Logger logger = LoggerFactory.getLogger(MaintopicCreateRequestValidation.class);
    

    public static void isValidateTitle(String title) {
        if (title == null || title.isBlank()) {
            logger.error("Title is required and cannot be blank");
            throw new RequestNotValidException(TITLE_REQUIRED, TYPE);
        }
        if (title.length() < 3 || title.length() > 100) {
            logger.error("Title must be between 3 and 100 characters");
            throw new RequestNotValidException(TITLE_SIZE, TYPE);
        }
        
    }
    
    public static void isValidateDescription(String description) {
        if (description == null || description.isBlank()) {
            logger.error("Description is required and cannot be blank");
            throw new RequestNotValidException(DESCRIPTION_REQUIRED, TYPE);
        }
        if (description.length() < 10 || description.length() > 500) {
            logger.error("Description must be between 10 and 500 characters");
            throw new RequestNotValidException(DESCRIPTION_SIZE, TYPE);
        }
    }

}
