package com.application.discussion.project.domain.valueobjects.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.application.discussion.project.domain.exceptions.BadRequestException;

public class Description {
    private final String value;
    private static final int MAX_LENGTH = 500;
    private static final int MIN_LENGTH = 10;

    private static final Logger logger = LoggerFactory.getLogger(Description.class);

    private Description(String value){
        if (value == null || value.isBlank()) {
            logger.error("Description cannot be null or empty: {}", value);
            throw new BadRequestException("説明が空です", "Bad_Request");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            logger.error("Description must be between {} and {} characters: {}", MIN_LENGTH, MAX_LENGTH, value);
            throw new BadRequestException("説明の文字数は " + MIN_LENGTH + " と " + MAX_LENGTH + " の間でなければなりません", "Bad_Request");
        }
        this.value = value;
    }

    public static Description of(String value) {
        return new Description(value);
    }

    public String getValue() {
        return this.value;
    }

    /**
     * 説明が空かどうかを判定する
     *
     * @return 説明が空の場合true、そうでなければfalse
     */
    public Boolean isEmpty() {
        return this.value == null || this.value.isEmpty() || this.value.isBlank();
    }

    /**
     * 他のDescriptionオブジェクトと値を比較する
     *
     * @param recentDescription 比較する現在のdescription
     * @return 値が等しい場合true、そうでなければfalse
     */
    public Boolean equals(String recentDescription){
        return this.value.equals(recentDescription);
    }
}
