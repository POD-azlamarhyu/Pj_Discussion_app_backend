package com.application.discussion.project.domain.valueobjects.topics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Title {
    private final String value;
    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 3;

    private static final Logger logger = LoggerFactory.getLogger(Title.class);
    
    // 引数ありコンストラクタ
    private Title(String value) {
        if (value == null || value.isBlank()) {
            logger.error("Title cannot be null or empty: {}", value);
            throw new IllegalArgumentException("タイトルが空です");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH){
            logger.error("Title must be between {} and {} characters: {}", MIN_LENGTH, MAX_LENGTH, value);
            throw new IllegalArgumentException("タイトルの文字数は " + MIN_LENGTH + " と " + MAX_LENGTH + " の間でなければなりません");
        }
        logger.info("Title value object created with value: {}", value);
        this.value = value;
    }

    public static Title of(String value) {
        return new Title(value);
    }
    
    public String getValue() {
        return this.value;
    }
}
