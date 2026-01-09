package com.application.discussion.project.domain.valueobjects.topics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

class DescriptionTests {
    private Description testDescription;
    private final String testValue = "これはテストの説明文です。";
    

    @BeforeEach
    void setUp() {
        testDescription = Description.of(testValue);
    }

    @Test
    void testValidInstance(){
        assertNotNull(testDescription);
        assertInstanceOf(Description.class, testDescription);
    }

    @Test
    void testValidDescriptionCreateInstance() {
        String value = "これは有効な説明文です。";
        Description description = Description.of(value);
        assertNotNull(description);
        assertInstanceOf(Description.class, description);
        assertEquals(value, description.getValue());
    }

    @Test
    void testMaxLengthDescription() {
        String value = "あ".repeat(500);
        Description description = Description.of(value);
        assertNotNull(description);
        assertInstanceOf(Description.class, description);
        assertEquals(value, description.getValue());
    }

    @Test
    void testMinLengthDescription() {
        String value = "F".repeat(10);
        Description description = Description.of(value);
        assertEquals(value, description.getValue());
    }

    @Test
    void nullDescriptionThrowException() {
        assertThrows(DomainLayerErrorException.class, () -> {
            Description.of(null);
        });
    }

    @Test
    void emptyDescriptionThrowException() {
        assertThrows(DomainLayerErrorException.class, () -> {
            Description.of("");
        });
    }

    @Test
    void tooLongDescriptionThrowException() {
        String value = "a".repeat(501);
        assertThrows(DomainLayerErrorException.class, () -> {
            Description.of(value);
        });
    }

    @Test
    void testEqualsTrue() {
        assertTrue(testDescription.equals(testValue));
    }

    @Test
    void testEqualsFalse() {
        assertFalse(testDescription.equals("異なる説明文"));
    }

    @Test
    void testIsEmptyFalse() {
        assertFalse(testDescription.isEmpty());
    }

}
