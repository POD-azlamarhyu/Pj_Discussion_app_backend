package com.application.discussion.project.application.dtos.discussions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DiscussionResponse ユニットテスト")
class DiscussionResponseTests {

    private static final Long DISCUSSION_ID = 1L;
    private static final String PARAGRAPH = "このプロジェクトの目的は...";
    private static final Long MAINTOPIC_ID = 10L;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 1, 15, 15, 45, 0);

    @Test
    @DisplayName("正常系: 全てのパラメータが正常な場合、DiscussionResponseインスタンスが生成されること")
    void testOfMethodWithValidParameters() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussionId()).isEqualTo(DISCUSSION_ID);
        assertThat(response.getParagraph()).isEqualTo(PARAGRAPH);
        assertThat(response.getMaintopicId()).isEqualTo(MAINTOPIC_ID);
        assertThat(response.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(response.getUpdatedAt()).isEqualTo(UPDATED_AT);
    }

    @Test
    @DisplayName("正常系: discussionIdが0の場合でもインスタンスが生成されること")
    void testOfMethodWithZeroDiscussionId() {
        DiscussionResponse response = DiscussionResponse.of(
            0L,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussionId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("正常系: paragraphが空文字の場合でもインスタンスが生成されること")
    void testOfMethodWithEmptyParagraph() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            "",
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response).isNotNull();
        assertThat(response.getParagraph()).isEmpty();
    }

    @Test
    @DisplayName("正常系: 同じ日時でcreatedAtとupdatedAtが設定されること")
    void testOfMethodWithSameDateTime() {
        LocalDateTime sameDateTime = LocalDateTime.now();
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            sameDateTime,
            sameDateTime
        );

        assertThat(response).isNotNull();
        assertThat(response.getCreatedAt()).isEqualTo(sameDateTime);
        assertThat(response.getUpdatedAt()).isEqualTo(sameDateTime);
    }

    @Test
    @DisplayName("境界値: 負のdiscussionIdでもインスタンスが生成されること")
    void testOfMethodWithNegativeDiscussionId() {
        DiscussionResponse response = DiscussionResponse.of(
            -1L,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussionId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("境界値: Long.MAX_VALUEのIDでもインスタンスが生成されること")
    void testOfMethodWithMaxLongValue() {
        DiscussionResponse response = DiscussionResponse.of(
            Long.MAX_VALUE,
            PARAGRAPH,
            Long.MAX_VALUE,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response).isNotNull();
        assertThat(response.getDiscussionId()).isEqualTo(Long.MAX_VALUE);
        assertThat(response.getMaintopicId()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("正常系: getDiscussionIdが正しい値を返すこと")
    void testGetDiscussionId() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response.getDiscussionId()).isEqualTo(DISCUSSION_ID);
    }

    @Test
    @DisplayName("正常系: getParagraphが正しい値を返すこと")
    void testGetParagraph() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response.getParagraph()).isEqualTo(PARAGRAPH);
    }

    @Test
    @DisplayName("正常系: getMaintopicIdが正しい値を返すこと")
    void testGetMaintopicId() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response.getMaintopicId()).isEqualTo(MAINTOPIC_ID);
    }

    @Test
    @DisplayName("正常系: getCreatedAtが正しい値を返すこと")
    void testGetCreatedAt() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response.getCreatedAt()).isEqualTo(CREATED_AT);
    }

    @Test
    @DisplayName("正常系: getUpdatedAtが正しい値を返すこと")
    void testGetUpdatedAt() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response.getUpdatedAt()).isEqualTo(UPDATED_AT);
    }

    @Test
    @DisplayName("正常系: 複数回getterを呼び出しても同じ値が返されること")
    void testGetterConsistency() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        Long firstCallId = response.getDiscussionId();
        Long secondCallId = response.getDiscussionId();
        String firstCallParagraph = response.getParagraph();
        String secondCallParagraph = response.getParagraph();

        assertThat(firstCallId).isEqualTo(secondCallId);
        assertThat(firstCallParagraph).isEqualTo(secondCallParagraph);
    }

    @Test
    @DisplayName("正常系: インスタンス生成後に値が変更されないこと")
    void testImmutability() {
        DiscussionResponse response = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        Long originalDiscussionId = response.getDiscussionId();
        String originalParagraph = response.getParagraph();
        Long originalMaintopicId = response.getMaintopicId();
        LocalDateTime originalCreatedAt = response.getCreatedAt();
        LocalDateTime originalUpdatedAt = response.getUpdatedAt();

        assertThat(response.getDiscussionId()).isEqualTo(originalDiscussionId);
        assertThat(response.getParagraph()).isEqualTo(originalParagraph);
        assertThat(response.getMaintopicId()).isEqualTo(originalMaintopicId);
        assertThat(response.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(response.getUpdatedAt()).isEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("正常系: 異なる値で複数のインスタンスを生成できること")
    void testMultipleInstances() {
        DiscussionResponse response1 = DiscussionResponse.of(
            1L,
            "議論1",
            10L,
            CREATED_AT,
            UPDATED_AT
        );

        DiscussionResponse response2 = DiscussionResponse.of(
            2L,
            "議論2",
            20L,
            CREATED_AT.plusDays(1),
            UPDATED_AT.plusDays(1)
        );

        assertThat(response1).isNotNull();
        assertThat(response2).isNotNull();
        assertThat(response1.getDiscussionId()).isNotEqualTo(response2.getDiscussionId());
        assertThat(response1.getParagraph()).isNotEqualTo(response2.getParagraph());
        assertThat(response1.getMaintopicId()).isNotEqualTo(response2.getMaintopicId());
    }

    @Test
    @DisplayName("正常系: 同じ値で複数のインスタンスを生成できること")
    void testMultipleInstancesWithSameValues() {
        DiscussionResponse response1 = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        DiscussionResponse response2 = DiscussionResponse.of(
            DISCUSSION_ID,
            PARAGRAPH,
            MAINTOPIC_ID,
            CREATED_AT,
            UPDATED_AT
        );

        assertThat(response1).isNotNull();
        assertThat(response2).isNotNull();
        assertThat(response1.getDiscussionId()).isEqualTo(response2.getDiscussionId());
        assertThat(response1.getParagraph()).isEqualTo(response2.getParagraph());
    }
}
