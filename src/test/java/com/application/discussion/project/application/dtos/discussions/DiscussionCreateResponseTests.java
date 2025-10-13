package com.application.discussion.project.application.dtos.discussions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DiscussionCreateResponse クラスのテスト")
public class DiscussionCreateResponseTests {

    @Test
    @DisplayName("デフォルトコンストラクタで正常にインスタンスが作成されること")
    void defaultConstructorTest() {
        // When
        DiscussionCreateResponse response = new DiscussionCreateResponse();

        // Then
        assertNotNull(response);
        assertNull(response.getDiscussionId());
        assertNull(response.getParagraph());
        assertNull(response.getMaintopicId());
    }

    @Test
    @DisplayName("全フィールド初期化コンストラクタで正常にインスタンスが作成されること")
    void fullConstructorTest() {
        // Given
        Long discussionId = 1L;
        String paragraph = "これは議論の内容です。新しい機能について話し合いましょう。";
        Long maintopicId = 100L;

        // When
        DiscussionCreateResponse response = new DiscussionCreateResponse(discussionId, paragraph, maintopicId);

        // Then
        assertNotNull(response);
        assertEquals(discussionId, response.getDiscussionId());
        assertEquals(paragraph, response.getParagraph());
        assertEquals(maintopicId, response.getMaintopicId());
    }

    @Test
    @DisplayName("DiscussionIdのゲッターが正常に動作すること")
    void getDiscussionIdTest() {
        // Given
        Long expectedId = 999L;
        DiscussionCreateResponse response = new DiscussionCreateResponse(expectedId, "テスト用議論", 50L);

        // When
        Long actualId = response.getDiscussionId();

        // Then
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Paragraphのゲッターが正常に動作すること")
    void getParagraphTest() {
        // Given
        String expectedParagraph = "Javaの新機能について議論したいと思います。ラムダ式の活用方法を中心に話し合いましょう。";
        DiscussionCreateResponse response = new DiscussionCreateResponse(1L, expectedParagraph, 10L);

        // When
        String actualParagraph = response.getParagraph();

        // Then
        assertEquals(expectedParagraph, actualParagraph);
    }

    @Test
    @DisplayName("MaintopicIdのゲッターが正常に動作すること")
    void getMaintopicIdTest() {
        // Given
        Long expectedMaintopicId = 777L;
        DiscussionCreateResponse response = new DiscussionCreateResponse(5L, "Spring Bootの設定について", expectedMaintopicId);

        // When
        Long actualMaintopicId = response.getMaintopicId();

        // Then
        assertEquals(expectedMaintopicId, actualMaintopicId);
    }

    @Test
    @DisplayName("nullパラメータでもコンストラクタが正常に動作すること")
    void constructorWithNullParametersTest() {
        // When
        DiscussionCreateResponse response = new DiscussionCreateResponse(null, null, null);

        // Then
        assertNotNull(response);
        assertNull(response.getDiscussionId());
        assertNull(response.getParagraph());
        assertNull(response.getMaintopicId());
    }

    @Test
    @DisplayName("空文字列のparagraphでもコンストラクタが正常に動作すること")
    void constructorWithEmptyStringTest() {
        // Given
        Long discussionId = 123L;
        String emptyParagraph = "";
        Long maintopicId = 456L;

        // When
        DiscussionCreateResponse response = new DiscussionCreateResponse(discussionId, emptyParagraph, maintopicId);

        // Then
        assertNotNull(response);
        assertEquals(discussionId, response.getDiscussionId());
        assertEquals(emptyParagraph, response.getParagraph());
        assertEquals(maintopicId, response.getMaintopicId());
    }
}
