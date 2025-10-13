package com.application.discussion.project.domain.valueobjects.discussions;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;


/**
 * 議論における文章を表現する値オブジェクト
 * ユーザーが入力する文章のビジネスルールと制約を管理する
 */
public class Paragraph {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 2000;
    private static final Pattern INVALID_PATTERN = Pattern.compile("^\\s*$");
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");
    private static final Pattern EXCESSIVE_WHITESPACE_PATTERN = Pattern.compile("\\s{3,}");

    private static final Logger logger = LoggerFactory.getLogger(Paragraph.class);

    private final String value;

    /**
     * 文章の値オブジェクトを作成する
     *
     * @param value 文章の内容
     * @throws DomainLayerErrorException 文章が制約に違反している場合
     */
    private Paragraph(String value) {
        String validatedValue = validateAndNormalize(value);
        this.value = validatedValue;
    }

    /**
     * デフォルトコンストラクタ（空の文章を生成）
     */
    private Paragraph() {
        this.value = "";
    }

    /**
     * ファクトリメソッド：安全な文章作成
     *
     * @param value 文章の内容
     * @return Paragraphのインスタンス、作成に失敗した場合はEmpty
     */
    public static Paragraph of(String value) {
        return new Paragraph(value);
    }

    /**
     * 文章を検証し正規化する
     *
     * @param input 入力文章
     * @return 正規化された文章
     * @throws DomainLayerErrorException 検証に失敗した場合
     */
    private String validateAndNormalize(String input) {
        logger.info("Validating and normalizing paragraph: {}", input);
        validateNotNull(input);

        String normalized = normalizeText(input);

        validateLength(normalized);
        validateContent(normalized);

        logger.info("Paragraph validated and normalized: {}", normalized);

        return normalized;
    }

    /**
     * 文章がnullまたは空白でないことを検証する
     *
     * @param input 入力文章
     * @throws DomainLayerErrorException 文章がnullまたは空白の場合
     */
    private void validateNotNull(String input) {
        if (StringUtils.isBlank(input) || StringUtils.isEmpty(input)) {
            logger.error("Paragraph cannot be null or empty: {}", input);
            throw new DomainLayerErrorException(
                "文章はnullまたは空白にできません",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }

    /**
     * 文章を正規化する（トリム、HTMLタグ除去、過剰な空白削除）
     *
     * @param input 入力文章
     * @return 正規化された文章
     */
    private String normalizeText(String input) {
        return Optional.ofNullable(input)
                .map(String::trim)
                .map(text -> HTML_TAG_PATTERN.matcher(text).replaceAll(""))
                .map(text -> EXCESSIVE_WHITESPACE_PATTERN.matcher(text).replaceAll(" "))
                .orElse("");
    }

    /**
     * 文章の長さが制約内であることを検証する
     *
     * @param text 正規化された文章
     * @throws DomainLayerErrorException 文章の長さが制約外の場合
     */
    private void validateLength(String text) {
        if (text.length() < MIN_LENGTH) {
            logger.error("Paragraph length is too short: {}", text.length());
            throw new DomainLayerErrorException(
                String.format("文章は%d文字以上である必要があります", MIN_LENGTH),
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }

        if (text.length() > MAX_LENGTH) {
            logger.error("Paragraph length is too long: {}", text.length());
            throw new DomainLayerErrorException(
                String.format("文章は%d文字以下である必要があります", MAX_LENGTH),
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }

    /**
     * 文章の内容を検証する（不正なパターンのチェックなど）
     *
     * @param text 正規化された文章
     * @throws DomainLayerErrorException 文章が不正な内容を含む場合
     */
    private void validateContent(String text) {
        if (INVALID_PATTERN.matcher(text).matches()) {
            logger.error("Paragraph contains invalid content: {}", text);
            throw new DomainLayerErrorException(
                "文章はnullまたは空白にできません",
                HttpStatus.BAD_REQUEST,
                HttpStatusCode.valueOf(400)
            );
        }
    }

    /**
     * 文章の内容を取得する
     *
     * @return 文章の内容
     */
    public String getValue() {
        return this.value;
    }

    /**
     * 文章の長さを取得する
     *
     * @return 文字数
     */
    public int length() {
        return this.value.length();
    }

    /**
     * 文章が空かどうかを判定する
     *
     * @return 空の場合true
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(this.value);
    }
}
