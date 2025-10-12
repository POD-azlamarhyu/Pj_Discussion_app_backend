package com.application.discussion.project.application.dtos.discussions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ディスカッション作成リクエストDTOのテスト")
public class DiscussionCreateRequestTests {

    private static final String VALID_PARAGRAPH = "これは有効なディスカッション内容です。";
    private static final String LONG_PARAGRAPH = "これは長いディスカッション内容のテストデータです。" +
            "複数の文章を含む長い本文でも正しく処理できることを確認するためのテストケースになります。" +
            "実際のアプリケーションでは、ユーザーが詳細な議論を投稿する場合があります。";
    private static final String SHORT_PARAGRAPH = "短い文章";
    private static final String JAPANESE_PARAGRAPH = "日本語での議論投稿テスト内容です。";
    private static final String EMPTY_STRING = "";

    @Test
    @DisplayName("デフォルトコンストラクタでインスタンスを作成できる")
    void createInstanceWithDefaultConstructorTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest();
        
        assertNotNull(request);
        assertNull(request.getParagraph());
    }

    @Test
    @DisplayName("パラメータ付きコンストラクタで有効な本文を設定できる")
    void createInstanceWithParameterizedConstructorTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(VALID_PARAGRAPH);
        
        assertNotNull(request);
        assertEquals(VALID_PARAGRAPH, request.getParagraph());
    }

    @Test
    @DisplayName("日本語の本文でインスタンスを作成できる")
    void createInstanceWithJapaneseParagraphTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(JAPANESE_PARAGRAPH);
        
        assertEquals(JAPANESE_PARAGRAPH, request.getParagraph());
    }

    @Test
    @DisplayName("長い本文でインスタンスを作成できる")
    void createInstanceWithLongParagraphTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(LONG_PARAGRAPH);
        
        assertEquals(LONG_PARAGRAPH, request.getParagraph());
    }

    @Test
    @DisplayName("短い本文でインスタンスを作成できる")
    void createInstanceWithShortParagraphTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(SHORT_PARAGRAPH);
        
        assertEquals(SHORT_PARAGRAPH, request.getParagraph());
    }

    @Test
    @DisplayName("空文字列の本文でインスタンスを作成できる")
    void createInstanceWithEmptyStringTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(EMPTY_STRING);
        
        assertEquals(EMPTY_STRING, request.getParagraph());
    }

    @Test
    @DisplayName("null値の本文でインスタンスを作成できる")
    void createInstanceWithNullParagraphTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(null);
        
        assertNull(request.getParagraph());
    }

    @Test
    @DisplayName("getParagraphが正しい値を返す")
    void getParagraphReturnsCorrectValueTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest(VALID_PARAGRAPH);
        
        assertEquals(VALID_PARAGRAPH, request.getParagraph());
    }

    @Test
    @DisplayName("デフォルトコンストラクタ後のgetParagraphがnullを返す")
    void getParagraphReturnsNullAfterDefaultConstructorTest() {
        DiscussionCreateRequest request = new DiscussionCreateRequest();
        
        assertNull(request.getParagraph());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "テスト議論の内容です。",
        "これは議論のテストデータです。詳細な内容を含みます。",
        "短文",
        "HTMLタグ<strong>太字</strong>を含む内容",
        "改行を含む\n議論内容\nテスト",
        "特殊文字!@#$%^&*()を含む議論",
        "数字123456789を含む議論内容"
    })
    @DisplayName("様々な形式の本文でインスタンスを作成できる")
    void createInstanceWithVariousParagraphFormatsTest(String paragraph) {
        DiscussionCreateRequest request = new DiscussionCreateRequest(paragraph);
        
        assertEquals(paragraph, request.getParagraph());
    }

    @Test
    @DisplayName("空白のみの本文でインスタンスを作成できる")
    void createInstanceWithWhitespaceOnlyParagraphTest() {
        String whitespaceOnly = "   ";
        DiscussionCreateRequest request = new DiscussionCreateRequest(whitespaceOnly);
        
        assertEquals(whitespaceOnly, request.getParagraph());
    }

    @Test
    @DisplayName("タブ文字を含む本文でインスタンスを作成できる")
    void createInstanceWithTabCharacterTest() {
        String paragraphWithTab = "議論内容\tタブ文字を含む";
        DiscussionCreateRequest request = new DiscussionCreateRequest(paragraphWithTab);
        
        assertEquals(paragraphWithTab, request.getParagraph());
    }

    @Test
    @DisplayName("複数のインスタンスが独立している")
    void multipleInstancesAreIndependentTest() {
        String firstParagraph = "最初の議論内容";
        String secondParagraph = "2番目の議論内容";
        
        DiscussionCreateRequest request1 = new DiscussionCreateRequest(firstParagraph);
        DiscussionCreateRequest request2 = new DiscussionCreateRequest(secondParagraph);
        
        assertEquals(firstParagraph, request1.getParagraph());
        assertEquals(secondParagraph, request2.getParagraph());
        assertNotEquals(request1.getParagraph(), request2.getParagraph());
    }

    @Test
    @DisplayName("同じ本文で作成した複数のインスタンスが同じ値を持つ")
    void multipleInstancesWithSameParagraphTest() {
        DiscussionCreateRequest request1 = new DiscussionCreateRequest(VALID_PARAGRAPH);
        DiscussionCreateRequest request2 = new DiscussionCreateRequest(VALID_PARAGRAPH);
        
        assertEquals(request1.getParagraph(), request2.getParagraph());
    }

    @Test
    @DisplayName("本文が非常に長い場合でも正しく処理される")
    void handleVeryLongParagraphTest() {
        String veryLongParagraph = "あ".repeat(1000);
        DiscussionCreateRequest request = new DiscussionCreateRequest(veryLongParagraph);
        
        assertEquals(veryLongParagraph, request.getParagraph());
        assertEquals(1000, request.getParagraph().length());
    }

    @Test
    @DisplayName("Unicode文字を含む本文でインスタンスを作成できる")
    void createInstanceWithUnicodeCharactersTest() {
        String unicodeParagraph = "絵文字😊と特殊文字♪★を含む議論内容";
        DiscussionCreateRequest request = new DiscussionCreateRequest(unicodeParagraph);
        
        assertEquals(unicodeParagraph, request.getParagraph());
    }
}
