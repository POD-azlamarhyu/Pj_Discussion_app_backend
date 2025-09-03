package com.application.discussion.project.domain.valueobjects.topics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.application.discussion.project.domain.exceptions.BadRequestException;

public class TitleTests {
    private Title title;
    private final String testValue = "HogeFuga";

    @BeforeEach
    void setUp(){
        title = Title.of(testValue);
    }

    @Test
    void testGetValue(){
        assertEquals(testValue, title.getValue());
    }

    @Test
    void testCreateInstance(){
        final Title testHoge = Title.of(testValue);
        assertNotNull(testHoge);
        assertInstanceOf(Title.class, testHoge);
    }

    @Test
    void testCreateInstanceBorder(){
        final String value = "テスト"; 
        final Title testFuga = Title.of(value);
        assertNotNull(testFuga);
        assertInstanceOf(Title.class, testFuga);
    }

    @Test
    void testCreateTopicMinLengthFailure(){
        final String value = "AI";
        assertThrows(BadRequestException.class,() -> {Title.of(value);});
    }

    @Test
    void testCreateTopicMaxLengthFailure(){
        String value = "F".repeat(101);
        assertThrows(BadRequestException.class,() -> {Title.of(value);});
    }

    @Test
    void testCreateTopicNull(){
        final String value = null;
        assertThrows(BadRequestException.class, () -> {Title.of(value);});
    }

    @Test
    void testEqualsTrue(){
        assertTrue(title.equals(testValue));
    }

    @Test
    void testEqualsFalse(){
        assertFalse(title.equals("DifferentTitle"));
    }

    @Test
    void testIsEmptyFalse(){
        assertFalse(title.isEmpty());
    }

    @Test
    void testIsEmptyTrue(){
        final Title emptyTitle = Title.of("");
        assertTrue(emptyTitle.isEmpty());
    }
}
