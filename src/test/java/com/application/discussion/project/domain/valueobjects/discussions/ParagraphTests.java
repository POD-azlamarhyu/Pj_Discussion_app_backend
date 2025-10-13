package com.application.discussion.project.domain.valueobjects.discussions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Paragraph value objectsのテスト")
public class ParagraphTests {

    private static final String VALID_TEXT = "これは有効な段落文章です。";
    private static final String MIN_LENGTH_TEXT = "あいう";
    private static final String JAPANESE_TEXT = "テスト文章です。";
    private static final String NULL_OR_EMPTY_MESSAGE = "文章はnullまたは空白にできません";
    private static final String MIN_LENGTH_MESSAGE = "文章は3文字以上である必要があります";
    private static final String MAX_LENGTH_MESSAGE = "文章は2000文字以下である必要があります";
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 2000;
    private static final int INVALID_SHORT_LENGTH = 2;
    private static final int INVALID_LONG_LENGTH = 2001;

    @Test
    @DisplayName("有効な文章で段落を作成できる")
    void createParagraphWithValidTextTest() {
        Paragraph paragraph = Paragraph.of(VALID_TEXT);
        
        assertEquals(VALID_TEXT, paragraph.getValue());
        assertEquals(VALID_TEXT.length(), paragraph.length());
        assertFalse(paragraph.isEmpty());
    }

    @Test
    @DisplayName("最小文字数で段落を作成できる")
    void createParagraphWithMinimumLengthTest() {
        Paragraph paragraph = Paragraph.of(MIN_LENGTH_TEXT);
        
        assertEquals(MIN_LENGTH_TEXT, paragraph.getValue());
        assertEquals(MIN_LENGTH, paragraph.length());
    }

    @Test
    @DisplayName("最大文字数で段落を作成できる")
    void createParagraphWithMaximumLengthTest() {
        String maxLengthText = "あ".repeat(MAX_LENGTH);
        
        Paragraph paragraph = Paragraph.of(maxLengthText);
        
        assertEquals(maxLengthText, paragraph.getValue());
        assertEquals(MAX_LENGTH, paragraph.length());
    }

    @Test
    @DisplayName("前後の空白が正しくトリムされる")
    void trimWhitespaceCorrectlyTest() {
        String textWithWhitespace = "   " + VALID_TEXT + "   ";
        
        Paragraph paragraph = Paragraph.of(textWithWhitespace);
        
        assertEquals(VALID_TEXT, paragraph.getValue());
    }

    @Test
    @DisplayName("HTMLタグが正しく除去される")
    void removeHtmlTagsCorrectlyTest() {
        String textWithHtml = "<p>これは<strong>有効な</strong>文章です。</p>";
        String expectedText = "これは有効な文章です。";
        
        Paragraph paragraph = Paragraph.of(textWithHtml);
        
        assertEquals(expectedText, paragraph.getValue());
    }

    @Test
    @DisplayName("過剰な空白が正しく正規化される")
    void normalizeExcessiveWhitespaceTest() {
        String textWithExcessiveWhitespace = "これは    有効な     文章です。";
        String expectedText = "これは 有効な 文章です。";
        
        Paragraph paragraph = Paragraph.of(textWithExcessiveWhitespace);
        
        assertEquals(expectedText, paragraph.getValue());
    }

    @Test
    @DisplayName("複数の正規化ルールが同時に適用される")
    void applyMultipleNormalizationRulesTest() {
        String complexText = "  <div>これは     <strong>有効な</strong>   文章です。</div>  ";
        String expectedText = "これは 有効な 文章です。";
        
        Paragraph paragraph = Paragraph.of(complexText);
        
        assertEquals(expectedText, paragraph.getValue());
    }

    @Test
    @DisplayName("null入力で例外が発生する")
    void throwExceptionForNullInputTest() {
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(null)
        );
        
        assertEquals(NULL_OR_EMPTY_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("空文字列で例外が発生する")
    void throwExceptionForEmptyStringTest() {
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of("")
        );
        
        assertEquals(NULL_OR_EMPTY_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "\t", "\n", "\r\n", "   \t  \n  "})
    @DisplayName("空白のみの文字列で例外が発生する")
    void throwExceptionForWhitespaceOnlyStringTest(String whitespaceOnly) {
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(whitespaceOnly)
        );
        assertEquals(NULL_OR_EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("短すぎる文章で例外が発生する")
    void throwExceptionForTooShortTextTest() {
        String shortText = "あ".repeat(INVALID_SHORT_LENGTH);
        
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(shortText)
        );
        
        assertEquals(MIN_LENGTH_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("長すぎる文章で例外が発生する")
    void throwExceptionForTooLongTextTest() {
        String longText = "あ".repeat(INVALID_LONG_LENGTH);
        
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(longText)
        );
        
        assertEquals(MAX_LENGTH_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("HTMLタグのみの文字列で例外が発生する")
    void throwExceptionForHtmlTagsOnlyTest() {
        String htmlTagsOnly = "<div><p></p></div>";
        
        assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(htmlTagsOnly)
        );
    }

    @Test
    @DisplayName("正規化後に空白のみになる文字列で例外が発生する")
    void throwExceptionForTextThatBecomesWhitespaceAfterNormalizationTest() {
        String textThatBecomesEmpty = "<p>   </p>";
        
        assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(textThatBecomesEmpty)
        );
    }

    @Test
    @DisplayName("ちょうど3文字の文章は有効")
    void exactlyThreeCharactersIsValidTest() {
        assertDoesNotThrow(() -> Paragraph.of(MIN_LENGTH_TEXT));
        
        Paragraph paragraph = Paragraph.of(MIN_LENGTH_TEXT);
        assertEquals(MIN_LENGTH, paragraph.length());
        assertEquals(MIN_LENGTH_TEXT, paragraph.getValue());
        assertTrue(paragraph instanceof Paragraph);
    }

    @Test
    @DisplayName("ちょうど2000文字の文章は有効")
    void exactlyTwoThousandCharactersIsValidTest() {
        String exactMaxLengthText = "あ".repeat(MAX_LENGTH);
        
        assertDoesNotThrow(() -> Paragraph.of(exactMaxLengthText));
        
        Paragraph paragraph = Paragraph.of(exactMaxLengthText);
        assertEquals(MAX_LENGTH, paragraph.length());
    }

    @Test
    @DisplayName("2文字の文章は無効")
    void twoCharactersIsInvalidTest() {
        String twoChars = "あ".repeat(INVALID_SHORT_LENGTH);
        
        assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(twoChars)
        );
    }

    @Test
    @DisplayName("2001文字の文章は無効")
    void twoThousandOneCharactersIsInvalidTest() {
        String tooLongText = "あ".repeat(INVALID_LONG_LENGTH);
        
        assertThrows(
            DomainLayerErrorException.class,
            () -> Paragraph.of(tooLongText)
        );
    }

    @Test
    @DisplayName("getValueが正しい値を返す")
    void getValueReturnsCorrectValueTest() {
        Paragraph paragraph = Paragraph.of(JAPANESE_TEXT);
        
        assertEquals(JAPANESE_TEXT, paragraph.getValue());
    }

    @Test
    @DisplayName("lengthが正しい文字数を返す")
    void lengthReturnsCorrectLengthTest() {
        Paragraph paragraph = Paragraph.of(JAPANESE_TEXT);
        
        assertEquals(JAPANESE_TEXT.length(), paragraph.length());
    }

    @Test
    @DisplayName("isEmptyが正しい結果を返す")
    void isEmptyReturnsCorrectResultTest() {
        Paragraph paragraph = Paragraph.of(JAPANESE_TEXT);
        
        assertFalse(paragraph.isEmpty());
    }

    @Test
    @DisplayName("ネストしたHTMLタグが除去される")
    void removeNestedHtmlTagsTest() {
        String nestedHtml = "<div><p><strong>テスト</strong>文章</p></div>";
        String expectedText = "テスト文章";
        
        Paragraph paragraph = Paragraph.of(nestedHtml);
        
        assertEquals(expectedText, paragraph.getValue());
    }

    @Test
    @DisplayName("HTMLエンティティは除去されない")
    void htmlEntitiesAreNotRemovedTest() {
        String textWithEntities = "&lt;これは&gt;テスト&amp;文章です。";
        
        Paragraph paragraph = Paragraph.of(textWithEntities);
        
        assertEquals(textWithEntities, paragraph.getValue());
    }

    @Test
    @DisplayName("複数行の過剰な空白が正規化される")
    void normalizeMultiLineExcessiveWhitespaceTest() {
        String multiLineText = "これは\n\n\n    テスト     文章\t\t\tです。";
        String expectedText = "これは テスト 文章 です。";
        
        Paragraph paragraph = Paragraph.of(multiLineText);
        
        assertEquals(expectedText, paragraph.getValue());
    }
}


