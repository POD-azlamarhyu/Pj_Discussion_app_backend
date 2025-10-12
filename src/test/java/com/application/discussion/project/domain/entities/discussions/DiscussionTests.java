package com.application.discussion.project.domain.entities.discussions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.discussions.Paragraph;

@DisplayName("Discussion Domain Entityのテスト")
public class DiscussionTests {

    private static final String VALID_PARAGRAPH_TEXT = "これは有効なディスカッション内容です。";
    private static final String LONG_PARAGRAPH_TEXT = "これは長いディスカッション内容のテストデータです。" +
            "ユーザーが詳細な議論を投稿する場合を想定したテストケースになります。" +
            "文字数制限内での最大限の内容を含むテストデータとして使用します。";
    private static final String MIN_LENGTH_TEXT = "最小文字";
    private static final Long VALID_DISCUSSION_ID = 1L;
    private static final Long VALID_MAINTOPIC_ID = 100L;
    private static final LocalDateTime VALID_CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
    private static final LocalDateTime VALID_UPDATED_AT = LocalDateTime.of(2024, 1, 2, 15, 30, 0);
    private static final LocalDateTime VALID_DELETED_AT = LocalDateTime.of(2024, 1, 3, 20, 45, 0);

    @Test
    @DisplayName("有効な全パラメータでディスカッションを作成できる")
    void createDiscussionWithAllValidParametersTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertNotNull(discussion);
        assertEquals(VALID_DISCUSSION_ID, discussion.getDiscussionId());
        assertEquals(VALID_PARAGRAPH_TEXT, discussion.getParagraph());
        assertEquals(VALID_MAINTOPIC_ID, discussion.getMaintopicId());
        assertEquals(VALID_CREATED_AT, discussion.getCreatedAt());
        assertEquals(VALID_UPDATED_AT, discussion.getUpdatedAt());
        assertEquals(VALID_DELETED_AT, discussion.getDeletedAt());
    }

    @Test
    @DisplayName("新規ディスカッションを作成できる")
    void createNewDiscussionTest() {
        Paragraph paragraph = Paragraph.of(VALID_PARAGRAPH_TEXT);
        
        Discussion discussion = Discussion.create(paragraph, VALID_MAINTOPIC_ID);

        assertNotNull(discussion);
        assertEquals(Long.valueOf(0), discussion.getDiscussionId());
        assertEquals(VALID_PARAGRAPH_TEXT, discussion.getParagraph());
        assertEquals(VALID_MAINTOPIC_ID, discussion.getMaintopicId());
        assertEquals(LocalDateTime.MIN, discussion.getCreatedAt());
        assertEquals(LocalDateTime.MIN, discussion.getUpdatedAt());
        assertEquals(LocalDateTime.MIN, discussion.getDeletedAt());
    }

    @Test
    @DisplayName("コンストラクタで直接ディスカッションを作成できる")
    void createDiscussionWithConstructorTest() {
        Paragraph paragraph = Paragraph.of(VALID_PARAGRAPH_TEXT);
        
        Discussion discussion = new Discussion(
            VALID_DISCUSSION_ID,
            paragraph,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertNotNull(discussion);
        assertEquals(VALID_DISCUSSION_ID, discussion.getDiscussionId());
        assertEquals(VALID_PARAGRAPH_TEXT, discussion.getParagraph());
        assertEquals(VALID_MAINTOPIC_ID, discussion.getMaintopicId());
        assertEquals(VALID_CREATED_AT, discussion.getCreatedAt());
        assertEquals(VALID_UPDATED_AT, discussion.getUpdatedAt());
        assertEquals(VALID_DELETED_AT, discussion.getDeletedAt());
    }

    @Test
    @DisplayName("最小文字数の段落でディスカッションを作成できる")
    void createDiscussionWithMinimumLengthParagraphTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            MIN_LENGTH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertNotNull(discussion);
        assertEquals(MIN_LENGTH_TEXT, discussion.getParagraph());
    }

    @Test
    @DisplayName("長い段落でディスカッションを作成できる")
    void createDiscussionWithLongParagraphTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            LONG_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertNotNull(discussion);
        assertEquals(LONG_PARAGRAPH_TEXT, discussion.getParagraph());
    }

    @Test
    @DisplayName("nullの段落文字列で例外が発生する")
    void throwExceptionForNullParagraphStringTest() {
        assertThrows(
            DomainLayerErrorException.class,
            () -> Discussion.of(
                VALID_DISCUSSION_ID,
                null,
                VALID_MAINTOPIC_ID,
                VALID_CREATED_AT,
                VALID_UPDATED_AT,
                VALID_DELETED_AT
            )
        );
    }

    @Test
    @DisplayName("空文字列の段落で例外が発生する")
    void throwExceptionForEmptyParagraphStringTest() {
        assertThrows(
            DomainLayerErrorException.class,
            () -> Discussion.of(
                VALID_DISCUSSION_ID,
                "",
                VALID_MAINTOPIC_ID,
                VALID_CREATED_AT,
                VALID_UPDATED_AT,
                VALID_DELETED_AT
            )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "\t", "\n", "\r\n", "   \t  \n  "})
    @DisplayName("空白のみの段落で例外が発生する")
    void throwExceptionForWhitespaceOnlyParagraphTest(String whitespaceOnly) {
        assertThrows(
            DomainLayerErrorException.class,
            () -> Discussion.of(
                VALID_DISCUSSION_ID,
                whitespaceOnly,
                VALID_MAINTOPIC_ID,
                VALID_CREATED_AT,
                VALID_UPDATED_AT,
                VALID_DELETED_AT
            )
        );
    }

    @Test
    @DisplayName("短すぎる段落で例外が発生する")
    void throwExceptionForTooShortParagraphTest() {
        String tooShort = "短い";
        
        assertThrows(
            DomainLayerErrorException.class,
            () -> Discussion.of(
                VALID_DISCUSSION_ID,
                tooShort,
                VALID_MAINTOPIC_ID,
                VALID_CREATED_AT,
                VALID_UPDATED_AT,
                VALID_DELETED_AT
            )
        );
    }

    @Test
    @DisplayName("長すぎる段落で例外が発生する")
    void throwExceptionForTooLongParagraphTest() {
        String tooLong = "あ".repeat(2001);
        
        assertThrows(
            DomainLayerErrorException.class,
            () -> Discussion.of(
                VALID_DISCUSSION_ID,
                tooLong,
                VALID_MAINTOPIC_ID,
                VALID_CREATED_AT,
                VALID_UPDATED_AT,
                VALID_DELETED_AT
            )
        );
    }

    @Test
    @DisplayName("createメソッドでnullの段落を渡すと例外が発生する")
    void throwExceptionForNullParagraphInCreateTest() {
        assertThrows(
            NullPointerException.class,
            () -> Discussion.create(null, VALID_MAINTOPIC_ID)
        );
    }

    @Test
    @DisplayName("HTMLタグを含む段落が正規化される")
    void normalizeHtmlTagsInParagraphTest() {
        String paragraphWithHtml = "<p>これは<strong>重要な</strong>ディスカッションです。</p>";
        String expectedText = "これは重要なディスカッションです。";
        
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            paragraphWithHtml,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(expectedText, discussion.getParagraph());
    }

    @Test
    @DisplayName("過剰な空白を含む段落が正規化される")
    void normalizeExcessiveWhitespaceInParagraphTest() {
        String paragraphWithSpaces = "これは    重要な     ディスカッションです。";
        String expectedText = "これは 重要な ディスカッションです。";
        
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            paragraphWithSpaces,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(expectedText, discussion.getParagraph());
    }

    @Test
    @DisplayName("ディスカッションIDが正しく取得できる")
    void getDiscussionIdTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(VALID_DISCUSSION_ID, discussion.getDiscussionId());
    }

    @Test
    @DisplayName("段落内容が正しく取得できる")
    void getParagraphTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(VALID_PARAGRAPH_TEXT, discussion.getParagraph());
    }

    @Test
    @DisplayName("メイントピックIDが正しく取得できる")
    void getMaintopicIdTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(VALID_MAINTOPIC_ID, discussion.getMaintopicId());
    }

    @Test
    @DisplayName("作成日時が正しく取得できる")
    void getCreatedAtTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(VALID_CREATED_AT, discussion.getCreatedAt());
    }

    @Test
    @DisplayName("更新日時が正しく取得できる")
    void getUpdatedAtTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(VALID_UPDATED_AT, discussion.getUpdatedAt());
    }

    @Test
    @DisplayName("削除日時が正しく取得できる")
    void getDeletedAtTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(VALID_DELETED_AT, discussion.getDeletedAt());
    }

    @Test
    @DisplayName("境界値：ちょうど3文字の段落で作成できる")
    void createWithExactlyThreeCharacterParagraphTest() {
        String exactThreeChars = "あいう";
        
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            exactThreeChars,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(exactThreeChars, discussion.getParagraph());
    }

    @Test
    @DisplayName("境界値：ちょうど2000文字の段落で作成できる")
    void createWithExactlyTwoThousandCharacterParagraphTest() {
        String exactTwoThousandChars = "あ".repeat(2000);
        
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            exactTwoThousandChars,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(exactTwoThousandChars, discussion.getParagraph());
    }

    @Test
    @DisplayName("IDが0のディスカッションを作成できる")
    void createDiscussionWithZeroIdTest() {
        Discussion discussion = Discussion.of(
            0L,
            VALID_PARAGRAPH_TEXT,
            VALID_MAINTOPIC_ID,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(Long.valueOf(0), discussion.getDiscussionId());
    }

    @Test
    @DisplayName("メイントピックIDが0のディスカッションを作成できる")
    void createDiscussionWithZeroMaintopicIdTest() {
        Discussion discussion = Discussion.of(
            VALID_DISCUSSION_ID,
            VALID_PARAGRAPH_TEXT,
            0L,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT
        );

        assertEquals(Long.valueOf(0), discussion.getMaintopicId());
    }
}
