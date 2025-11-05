package com.application.discussion.project.application.dtos.discussions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DiscussionListResponse ユニットテスト")
class DiscussionListResponseTests {

    private static final Long DISCUSSION_ID_1 = 1L;
    private static final Long DISCUSSION_ID_2 = 2L;
    private static final Long DISCUSSION_ID_3 = 3L;
    private static final String PARAGRAPH_1 = "議論内容1";
    private static final String PARAGRAPH_2 = "議論内容2";
    private static final String PARAGRAPH_3 = "議論内容3";
    private static final Long MAINTOPIC_ID = 10L;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 1, 15, 15, 45, 0);
    
    private static final Integer TOTAL_COUNT = 25;
    private static final Integer CURRENT_PAGE = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Integer TOTAL_PAGES = 3;
    
    private static final Integer ZERO_COUNT = 0;
    private static final Integer FIRST_PAGE = 0;
    private static final Integer ONE_PAGE = 1;

    @Test
    @DisplayName("正常系: 全てのパラメータが正常な場合、DiscussionListResponseインスタンスが生成されること")
    void testOfMethodWithValidParameters() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussions()).hasSize(3);
        assertThat(response.getTotalCount()).isEqualTo(TOTAL_COUNT);
        assertThat(response.getCurrentPage()).isEqualTo(CURRENT_PAGE);
        assertThat(response.getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(response.getTotalPages()).isEqualTo(TOTAL_PAGES);
    }

    @Test
    @DisplayName("正常系: 空のリストでインスタンスが生成されること")
    void testOfMethodWithEmptyList() {
        List<DiscussionResponse> emptyDiscussions = new ArrayList<>();

        DiscussionListResponse response = DiscussionListResponse.of(
            emptyDiscussions,
            ZERO_COUNT,
            FIRST_PAGE,
            PAGE_SIZE,
            ZERO_COUNT
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussions()).isEmpty();
        assertThat(response.getTotalCount()).isEqualTo(ZERO_COUNT);
        assertThat(response.getCurrentPage()).isEqualTo(FIRST_PAGE);
        assertThat(response.getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(response.getTotalPages()).isEqualTo(ZERO_COUNT);
    }

    @Test
    @DisplayName("正常系: デフォルトコンストラクタでインスタンスが生成されること")
    void testDefaultConstructor() {
        DiscussionListResponse response = new DiscussionListResponse();

        assertThat(response).isNotNull();
        assertThat(response.getDiscussions()).isNull();
        assertThat(response.getTotalCount()).isNull();
        assertThat(response.getCurrentPage()).isNull();
        assertThat(response.getPageSize()).isNull();
        assertThat(response.getTotalPages()).isNull();
    }

    @Test
    @DisplayName("正常系: 1ページ分のデータでインスタンスが生成されること")
    void testOfMethodWithSinglePage() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            discussions.size(),
            FIRST_PAGE,
            PAGE_SIZE,
            ONE_PAGE
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussions()).hasSize(3);
        assertThat(response.getTotalCount()).isEqualTo(3);
        assertThat(response.getCurrentPage()).isEqualTo(FIRST_PAGE);
        assertThat(response.getTotalPages()).isEqualTo(ONE_PAGE);
    }

    @Test
    @DisplayName("境界値: currentPageが0の場合でもインスタンスが生成されること")
    void testOfMethodWithZeroCurrentPage() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            FIRST_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        assertThat(response).isNotNull();
        assertThat(response.getCurrentPage()).isEqualTo(FIRST_PAGE);
    }

    @Test
    @DisplayName("境界値: Integer.MAX_VALUEのカウント値でもインスタンスが生成されること")
    void testOfMethodWithMaxIntegerValue() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            Integer.MAX_VALUE,
            CURRENT_PAGE,
            PAGE_SIZE,
            Integer.MAX_VALUE
        );

        assertThat(response).isNotNull();
        assertThat(response.getTotalCount()).isEqualTo(Integer.MAX_VALUE);
        assertThat(response.getTotalPages()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("正常系: getDiscussionsが正しいリストを返すこと")
    void testGetDiscussions() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        List<DiscussionResponse> result = response.getDiscussions();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getDiscussionId()).isEqualTo(DISCUSSION_ID_1);
        assertThat(result.get(1).getDiscussionId()).isEqualTo(DISCUSSION_ID_2);
        assertThat(result.get(2).getDiscussionId()).isEqualTo(DISCUSSION_ID_3);
    }

    @Test
    @DisplayName("正常系: getTotalCountが正しい値を返すこと")
    void testGetTotalCount() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        assertThat(response.getTotalCount()).isEqualTo(TOTAL_COUNT);
    }

    @Test
    @DisplayName("正常系: getCurrentPageが正しい値を返すこと")
    void testGetCurrentPage() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        assertThat(response.getCurrentPage()).isEqualTo(CURRENT_PAGE);
    }

    @Test
    @DisplayName("正常系: getPageSizeが正しい値を返すこと")
    void testGetPageSize() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        assertThat(response.getPageSize()).isEqualTo(PAGE_SIZE);
    }

    @Test
    @DisplayName("正常系: getTotalPagesが正しい値を返すこと")
    void testGetTotalPages() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        assertThat(response.getTotalPages()).isEqualTo(TOTAL_PAGES);
    }

    @Test
    @DisplayName("正常系: 複数回getterを呼び出しても同じ値が返されること")
    void testGetterConsistency() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        Integer firstCallTotalCount = response.getTotalCount();
        Integer secondCallTotalCount = response.getTotalCount();
        Integer firstCallCurrentPage = response.getCurrentPage();
        Integer secondCallCurrentPage = response.getCurrentPage();

        assertThat(firstCallTotalCount).isEqualTo(secondCallTotalCount);
        assertThat(firstCallCurrentPage).isEqualTo(secondCallCurrentPage);
    }

    @Test
    @DisplayName("正常系: インスタンス生成後に値が変更されないこと")
    void testImmutability() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        List<DiscussionResponse> originalDiscussions = response.getDiscussions();
        Integer originalTotalCount = response.getTotalCount();
        Integer originalCurrentPage = response.getCurrentPage();
        Integer originalPageSize = response.getPageSize();
        Integer originalTotalPages = response.getTotalPages();

        assertThat(response.getDiscussions()).isEqualTo(originalDiscussions);
        assertThat(response.getTotalCount()).isEqualTo(originalTotalCount);
        assertThat(response.getCurrentPage()).isEqualTo(originalCurrentPage);
        assertThat(response.getPageSize()).isEqualTo(originalPageSize);
        assertThat(response.getTotalPages()).isEqualTo(originalTotalPages);
    }

    @Test
    @DisplayName("正常系: 異なる値で複数のインスタンスを生成できること")
    void testMultipleInstances() {
        List<DiscussionResponse> discussions1 = createDiscussionResponseList();
        List<DiscussionResponse> discussions2 = List.of(
            createDiscussionResponse(DISCUSSION_ID_1, PARAGRAPH_1)
        );

        DiscussionListResponse response1 = DiscussionListResponse.of(
            discussions1,
            TOTAL_COUNT,
            CURRENT_PAGE,
            PAGE_SIZE,
            TOTAL_PAGES
        );

        DiscussionListResponse response2 = DiscussionListResponse.of(
            discussions2,
            ONE_PAGE,
            FIRST_PAGE,
            ONE_PAGE,
            ONE_PAGE
        );

        assertThat(response1).isNotNull();
        assertThat(response2).isNotNull();
        assertThat(response1.getDiscussions()).hasSize(3);
        assertThat(response2.getDiscussions()).hasSize(1);
        assertThat(response1.getTotalCount()).isNotEqualTo(response2.getTotalCount());
    }

    @Test
    @DisplayName("正常系: ページネーション情報が整合性を保つこと")
    void testPaginationConsistency() {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        Integer expectedTotalPages = 3;
        Integer expectedPageSize = 10;

        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CURRENT_PAGE,
            expectedPageSize,
            expectedTotalPages
        );

        assertThat(response.getPageSize()).isEqualTo(expectedPageSize);
        assertThat(response.getTotalPages()).isEqualTo(expectedTotalPages);
        assertThat(response.getCurrentPage()).isLessThan(response.getTotalPages());
    }

    private List<DiscussionResponse> createDiscussionResponseList() {
        return List.of(
            createDiscussionResponse(DISCUSSION_ID_1, PARAGRAPH_1),
            createDiscussionResponse(DISCUSSION_ID_2, PARAGRAPH_2),
            createDiscussionResponse(DISCUSSION_ID_3, PARAGRAPH_3)
        );
    }

    private DiscussionResponse createDiscussionResponse(Long discussionId, String paragraph) {
        return DiscussionResponse.of(
            discussionId,
            paragraph,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );
    }
}
