package com.application.discussion.project.presentation.validations;

import com.application.discussion.project.presentation.exceptions.RequestNotValidException;

public class MaintopicCreateRequestValidation {
    private static final String TITLE_REQUIRED = "タイトルは必須です";
    private static final String TITLE_NOT_BLANK = "タイトルは空白にできません";
    private static final String TITLE_SIZE = "タイトルは3文字以上100文字以下で入力してください";
    
    private static final String DESCRIPTION_REQUIRED = "説明は必須です";
    private static final String DESCRIPTION_NOT_BLANK = "説明は空白にできません";
    private static final String DESCRIPTION_SIZE = "説明は10文字以上500文字以下で入力してください";
    private static final String TYPE = "Request_Not_Valid";
    

    public static Boolean isValidTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new RequestNotValidException(TITLE_REQUIRED, TYPE);
        }
        if (title.length() < 3 || title.length() > 100) {
            throw new RequestNotValidException(TITLE_SIZE, TYPE);
        }
        return true;
    }
    
    public static Boolean isValidDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new RequestNotValidException(DESCRIPTION_REQUIRED, TYPE);
        }
        if (description.length() < 10 || description.length() > 500) {
            throw new RequestNotValidException(DESCRIPTION_SIZE, TYPE);
        }
        return true;
    }

}
