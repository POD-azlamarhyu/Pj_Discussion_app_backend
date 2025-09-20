package com.application.discussion.project.presentation.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;

public class MaintopicIdRequestValidation {
    private static final String MAINTOPIC_ID_REQUIRED = "メイントピックIDは必須です";
    private static final String MAINTOPIC_ID_TYPE = "メイントピックIDが不正です";
    private static final Long MAX_MAINTOPIC_ID = Long.MAX_VALUE;

    public static void isValidateMaintopicId(final Long maintopicId) {
        validateIdNotNull(maintopicId);
        validateIdType(maintopicId);
        validateIdNotNegative(maintopicId);
        validateIdNotTooLarge(maintopicId);
    }

    private static void validateIdNotNull(final Long maintopicId) {
        if (maintopicId == null) {
            throw new PresentationLayerErrorException(
                MAINTOPIC_ID_REQUIRED,
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }

    /*
     * TODO: Github Copilotのレビューがあったように，Long型で型を指定しているので，このバリデーションは不要になる可能性がある．
     */
    private static void validateIdType(final Long maintopicId) {
        if (!(maintopicId instanceof Long)) {
            throw new PresentationLayerErrorException(
                MAINTOPIC_ID_TYPE,
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }

    private static void validateIdNotNegative(final Long maintopicId) {
        if (maintopicId < 0) {
            throw new PresentationLayerErrorException(
                MAINTOPIC_ID_TYPE,
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }

    /*
     * TODO: Github Copilotのレビューがあったように，Long型で型を指定しているので，このバリデーションは不要になる可能性がある．
     */
    private static void validateIdNotTooLarge(final Long maintopicId) {
        if (maintopicId > MAX_MAINTOPIC_ID) {
            throw new PresentationLayerErrorException(
                MAINTOPIC_ID_TYPE,
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }
}
