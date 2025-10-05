package com.application.discussion.project.presentation.validations;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;

/**
 * ディスカッション作成時のバリデーションクラス
 */
public class DiscussionCreateRequestValidation {
    
    private static final int MIN_PARAGRAPH_LENGTH = 10;
    private static final int MAX_PARAGRAPH_LENGTH = 2000;
    private static final String PARAGRAPH_NOT_BLANK = "文章は空白にできません";
    private static final String PARAGRAPH_SIZE = String.format("文章は%d文字以上, %d文字以下で入力してください", MIN_PARAGRAPH_LENGTH, MAX_PARAGRAPH_LENGTH);

    private static final Logger logger = LoggerFactory.getLogger(DiscussionCreateRequestValidation.class);

    private DiscussionCreateRequestValidation() {}
    

    /**
     * 
     * @param paragraph 文章
     * @return  true: nullまたは空白, false: nullでも空白でもない
     */
    private static Boolean isValidateNullParagraph(
        final String paragraph
    ){
        return StringUtils.isEmpty(paragraph) || StringUtils.isBlank(paragraph);
    }

    /**
     * 
     * @param paragraph 文章
     * @return true: 文字数制限を超過, false: 文字数制限内
     */
    private static Boolean isValidateParagraphSize(
        final String paragraph
    ){
        return StringUtils.length(paragraph) < MIN_PARAGRAPH_LENGTH || StringUtils.length(paragraph) > MAX_PARAGRAPH_LENGTH;
    }
    
    /**
     * DiscussionCreateRequestのバリデーションを行う
     * @param discussionCreateRequest DiscussionCreateRequest
     */
    public static void validate(
        final DiscussionCreateRequest discussionCreateRequest
    ){
        if(isValidateNullParagraph(discussionCreateRequest.getParagraph())) {
            logger.error("Paragraph is required and cannot be blank");
            throw new PresentationLayerErrorException(
                PARAGRAPH_NOT_BLANK,
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }

        if(isValidateParagraphSize(discussionCreateRequest.getParagraph())){
            logger.error("Paragraph must be between {} and {} characters", MIN_PARAGRAPH_LENGTH, MAX_PARAGRAPH_LENGTH);
            throw new PresentationLayerErrorException(
                PARAGRAPH_SIZE,
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }
}
