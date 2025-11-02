package com.application.discussion.project.presentation.validations;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;

/**
 * 議論リスト取得リクエストのバリデーションクラス
 */
public class DiscussionListRequestValidation {

    private static final int MIN_PAGE = 0;
    private static final int MAX_PAGE = 1000;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList(
        "createdAt", 
        "updatedAt", 
        "discussionId",
        "paragraph"
    );
    private static final List<String> ALLOWED_DIRECTIONS = Arrays.asList("asc", "desc");

    /**
     * 議論リスト取得リクエストのバリデーションを実行する
     * 
     * @param maintopicId メイントピックID
     * @param page ページ番号
     * @param size ページサイズ
     * @param sortBy ソート項目
     * @param direction ソート順
     * @throws PresentationLayerErrorException バリデーションエラーが発生した場合
     */
    public static void validate(
        final Long maintopicId,
        final Integer page,
        final Integer size,
        final String sortBy,
        final String direction
    ) {
        validateMaintopicId(maintopicId);
        validatePage(page);
        validateSize(size);
        validateSortBy(sortBy);
        validateDirection(direction);
    }

    /**
     * メイントピックIDのバリデーション
     */
    private static void validateMaintopicId(final Long maintopicId) {
        if (maintopicId == null) {
            throw new PresentationLayerErrorException(
                "メイントピックIDは必須です",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (maintopicId <= 0) {
            throw new PresentationLayerErrorException(
                "メイントピックIDは正の整数である必要があります",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * ページ番号のバリデーション
     */
    private static void validatePage(final Integer page) {
        if (page == null) {
            throw new PresentationLayerErrorException(
                "ページ番号は必須です",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (page < MIN_PAGE) {
            throw new PresentationLayerErrorException(
                String.format("ページ番号は%d以上である必要があります", MIN_PAGE),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (page > MAX_PAGE) {
            throw new PresentationLayerErrorException(
                String.format("ページ番号は%d以下である必要があります", MAX_PAGE),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * ページサイズのバリデーション
     */
    private static void validateSize(final Integer size) {
        if (size == null) {
            throw new PresentationLayerErrorException(
                "ページサイズは必須です",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (size < MIN_SIZE) {
            throw new PresentationLayerErrorException(
                String.format("ページサイズは%d以上である必要があります", MIN_SIZE),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (size > MAX_SIZE) {
            throw new PresentationLayerErrorException(
                String.format("ページサイズは%d以下である必要があります", MAX_SIZE),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * ソート項目のバリデーション
     */
    private static void validateSortBy(final String sortBy) {
        if (StringUtils.isBlank(sortBy) || StringUtils.trimToNull(sortBy) == null || StringUtils.trimToNull(sortBy).isEmpty()) {
            throw new PresentationLayerErrorException(
                "ソート項目は必須です",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new PresentationLayerErrorException(
                String.format("無効なソート項目です。許可された値: %s", ALLOWED_SORT_FIELDS),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * ソート順のバリデーション
     */
    private static void validateDirection(final String direction) {
        if (StringUtils.isBlank(direction) || StringUtils.trimToNull(direction) == null || StringUtils.trimToNull(direction).isEmpty()) {
            throw new PresentationLayerErrorException(
                "ソート順は必須です",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
        if (!ALLOWED_DIRECTIONS.contains(direction.toLowerCase())) {
            throw new PresentationLayerErrorException(
                String.format("無効なソート順です。許可された値: %s", ALLOWED_DIRECTIONS),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
