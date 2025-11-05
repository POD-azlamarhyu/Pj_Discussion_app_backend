package com.application.discussion.project.presentation.validations;

import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.springframework.http.HttpStatusCode;

@DisplayName("DiscussionListRequestValidation ユニットテスト")
class DiscussionListRequestValidationTests {

    private static final Long VALID_MAINTOPIC_ID = 1L;
    private static final Long ZERO_MAINTOPIC_ID = 0L;
    private static final Long NEGATIVE_MAINTOPIC_ID = -1L;
    
    private static final Integer VALID_PAGE = 0;
    private static final Integer VALID_PAGE_MID = 500;
    private static final Integer MIN_PAGE = 0;
    private static final Integer MAX_PAGE = 1000;
    private static final Integer NEGATIVE_PAGE = -1;
    private static final Integer OVER_MAX_PAGE = 1001;
    private static final Integer BAD_REQUEST_CODE= 400;
    private static final Integer VALID_SIZE = 10;
    private static final Integer MIN_SIZE = 1;
    private static final Integer MAX_SIZE = 100;
    private static final Integer ZERO_SIZE = 0;
    private static final Integer NEGATIVE_SIZE = -1;
    private static final Integer OVER_MAX_SIZE = 101;
    
    private static final String VALID_SORT_BY_CREATED_AT = "createdAt";
    private static final String VALID_SORT_BY_UPDATED_AT = "updatedAt";
    private static final String VALID_SORT_BY_DISCUSSION_ID = "discussionId";
    private static final String VALID_SORT_BY_PARAGRAPH = "paragraph";
    private static final String INVALID_SORT_BY = "invalidField";
    private static final String EMPTY_SORT_BY = "";
    private static final String BLANK_SORT_BY = "   ";
    
    private static final String VALID_DIRECTION_ASC = "asc";
    private static final String VALID_DIRECTION_DESC = "desc";
    private static final String VALID_DIRECTION_ASC_UPPER = "ASC";
    private static final String VALID_DIRECTION_DESC_UPPER = "DESC";
    private static final String VALID_DIRECTION_MIXED = "AsC";
    private static final String INVALID_DIRECTION = "invalid";
    private static final String EMPTY_DIRECTION = "";
    private static final String BLANK_DIRECTION = "   ";
    
    private static final String ERROR_MESSAGE_MAINTOPIC_ID_REQUIRED = "メイントピックIDは必須です";
    private static final String ERROR_MESSAGE_MAINTOPIC_ID_POSITIVE = "メイントピックIDは正の整数である必要があります";
    private static final String ERROR_MESSAGE_PAGE_REQUIRED = "ページ番号は必須です";
    private static final String ERROR_MESSAGE_PAGE_MIN = "ページ番号は0以上である必要があります";
    private static final String ERROR_MESSAGE_PAGE_MAX = "ページ番号は1000以下である必要があります";
    private static final String ERROR_MESSAGE_SIZE_REQUIRED = "ページサイズは必須です";
    private static final String ERROR_MESSAGE_SIZE_MIN = "ページサイズは1以上である必要があります";
    private static final String ERROR_MESSAGE_SIZE_MAX = "ページサイズは100以下である必要があります";
    private static final String ERROR_MESSAGE_SORT_BY_REQUIRED = "ソート項目は必須です";
    private static final String ERROR_MESSAGE_SORT_BY_INVALID = "無効なソート項目です。許可された値: [createdAt, updatedAt, discussionId, paragraph]";
    private static final String ERROR_MESSAGE_DIRECTION_REQUIRED = "ソート順は必須です";
    private static final String ERROR_MESSAGE_DIRECTION_INVALID = "無効なソート順です。許可された値: [asc, desc]";

    @Test
    @DisplayName("正常系: すべてのパラメータが有効な場合、バリデーションが成功すること")
    void validateWithAllValidParametersTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 最小値のページ番号でバリデーションが成功すること")
    void validateWithMinPageTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            MIN_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 最大値のページ番号でバリデーションが成功すること")
    void validateWithMaxPageTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            MAX_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 最小値のページサイズでバリデーションが成功すること")
    void validateWithMinSizeTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            MIN_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 最大値のページサイズでバリデーションが成功すること")
    void validateWithMaxSizeTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            MAX_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: updatedAtでソートする場合、バリデーションが成功すること")
    void validateWithUpdatedAtSortByTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_UPDATED_AT,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: discussionIdでソートする場合、バリデーションが成功すること")
    void validateWithDiscussionIdSortByTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_DISCUSSION_ID,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: paragraphでソートする場合、バリデーションが成功すること")
    void validateWithParagraphSortByTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_PARAGRAPH,
            VALID_DIRECTION_DESC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 昇順ソートでバリデーションが成功すること")
    void validateWithAscDirectionTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_ASC
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 大文字の昇順ソートでバリデーションが成功すること")
    void validateWithUpperCaseAscDirectionTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_ASC_UPPER
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 大文字の降順ソートでバリデーションが成功すること")
    void validateWithUpperCaseDescDirectionTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC_UPPER
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("正常系: 大文字小文字混在のソート順でバリデーションが成功すること")
    void validateWithMixedCaseDirectionTest() {
        assertThatCode(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_MIXED
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("異常系: メイントピックIDがnullの場合、例外がスローされること")
    void validateWithNullMaintopicIdTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            null,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_MAINTOPIC_ID_REQUIRED)
            .hasFieldOrPropertyWithValue("status",HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: メイントピックIDが0の場合、例外がスローされること")
    void validateWithZeroMaintopicIdTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            ZERO_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_MAINTOPIC_ID_POSITIVE)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: メイントピックIDが負の値の場合、例外がスローされること")
    void validateWithNegativeMaintopicIdTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            NEGATIVE_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_MAINTOPIC_ID_POSITIVE)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: ページ番号がnullの場合、例外がスローされること")
    void validateWithNullPageTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            null,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_PAGE_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: ページ番号が負の値の場合、例外がスローされること")
    void validateWithNegativePageTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            NEGATIVE_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_PAGE_MIN)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: ページ番号が最大値を超える場合、例外がスローされること")
    void validateWithOverMaxPageTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            OVER_MAX_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_PAGE_MAX)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: ページサイズがnullの場合、例外がスローされること")
    void validateWithNullSizeTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            null,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SIZE_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: ページサイズが0の場合、例外がスローされること")
    void validateWithZeroSizeTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            ZERO_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SIZE_MIN)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
            .hasFieldOrPropertyWithValue("code", HttpStatusCode.valueOf(BAD_REQUEST_CODE));
    }

    @Test
    @DisplayName("異常系: ページサイズが負の値の場合、例外がスローされること")
    void validateWithNegativeSizeTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            NEGATIVE_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SIZE_MIN)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ページサイズが最大値を超える場合、例外がスローされること")
    void validateWithOverMaxSizeTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            OVER_MAX_SIZE,
            VALID_SORT_BY_CREATED_AT,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SIZE_MAX)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート項目がnullの場合、例外がスローされること")
    void validateWithNullSortByTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            null,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SORT_BY_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート項目が空文字の場合、例外がスローされること")
    void validateWithEmptySortByTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            EMPTY_SORT_BY,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SORT_BY_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート項目が空白文字の場合、例外がスローされること")
    void validateWithBlankSortByTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            BLANK_SORT_BY,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SORT_BY_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート項目が無効な値の場合、例外がスローされること")
    void validateWithInvalidSortByTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            INVALID_SORT_BY,
            VALID_DIRECTION_DESC
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_SORT_BY_INVALID)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート順がnullの場合、例外がスローされること")
    void validateWithNullDirectionTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            null
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_DIRECTION_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート順が空文字の場合、例外がスローされること")
    void validateWithEmptyDirectionTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            EMPTY_DIRECTION
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_DIRECTION_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート順が空白文字の場合、例外がスローされること")
    void validateWithBlankDirectionTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            BLANK_DIRECTION
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_DIRECTION_REQUIRED)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系: ソート順が無効な値の場合、例外がスローされること")
    void validateWithInvalidDirectionTest() {
        assertThatThrownBy(() -> DiscussionListRequestValidation.validate(
            VALID_MAINTOPIC_ID,
            VALID_PAGE,
            VALID_SIZE,
            VALID_SORT_BY_CREATED_AT,
            INVALID_DIRECTION
        ))
            .isInstanceOf(PresentationLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_DIRECTION_INVALID)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }
}
