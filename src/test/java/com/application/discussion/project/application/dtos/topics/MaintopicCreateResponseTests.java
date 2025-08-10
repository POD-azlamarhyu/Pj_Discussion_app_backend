package com.application.discussion.project.application.dtos.topics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaintopicCreateResponseTests {

    private final Long id = 1L;
    private final String title = "テストタイトル";
    private final String description = "テスト説明";
    private final String createdAt = "2023-12-01T10:00:00";

    @Test
    void testCreateInstanceSuccessfully() {

        MaintopicCreateResponse response = new MaintopicCreateResponse(
                id,
                title,
                description,
                createdAt);

        // Assert
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(title, response.getTitle());
        assertEquals(description, response.getDescription());
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void testNullValueCreateInstanceSuccess() {

        MaintopicCreateResponse response = new MaintopicCreateResponse(
            null,
            null,
            null,
            null
        );

        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getTitle());
        assertNull(response.getDescription());
        assertNull(response.getCreatedAt());
    }

    @Test
    void testReturnCorrectId() {

        Long expectedId = 123L;
        MaintopicCreateResponse response = new MaintopicCreateResponse(
            expectedId,
            "title",
            "desc",
            "date"
        );

        assertEquals(expectedId, response.getId());
    }

    @Test
    void testReturnCorrectTitle() {

        String expectedTitle = "テストタイトル";
        MaintopicCreateResponse response = new MaintopicCreateResponse(
            1L, 
            expectedTitle, 
            "desc", 
            createdAt
        );

        assertEquals(expectedTitle, response.getTitle());
    }

    @Test
    void testReturnCorrectDescription() {

        String expectedDescription = "テスト説明";
        MaintopicCreateResponse response = new MaintopicCreateResponse(
            1L, 
            "title", 
            expectedDescription, 
            createdAt
        );

        assertEquals(expectedDescription, response.getDescription());
    }

    @Test
    void testReturnCorrectCreatedAt() {

        String expectedCreatedAt = "2023-12-01T10:00:00";
        MaintopicCreateResponse response = new MaintopicCreateResponse(
            1L, 
            "title", 
            "desc", 
            expectedCreatedAt
        );

        assertEquals(expectedCreatedAt, response.getCreatedAt());
    }

    @Test
    void testSetIdCorrectly() {

        MaintopicCreateResponse response = new MaintopicCreateResponse(
            1L, 
            "title", 
            "desc", 
            createdAt
        );
        Long newId = 999L;

        response.setId(newId);
        assertEquals(newId, response.getId());
    }

    @Test
    void testSetTitleCorrectly() {
        MaintopicCreateResponse response = new MaintopicCreateResponse(
            1L, 
            "title", 
            "desc", 
            createdAt
        );
        String newTitle = "新しいタイトル";

        response.setTitle(newTitle);

        assertEquals(newTitle, response.getTitle());
    }

    @Test
    void testSetDescriptionCorrectly() {
        MaintopicCreateResponse response = new MaintopicCreateResponse(1L, "title", "desc", "date");
        String newDescription = "新しい説明";

        response.setDescription(newDescription);

        assertEquals(newDescription, response.getDescription());
    }

    @Test
    void testSetCreatedAtCorrectly() {
        MaintopicCreateResponse response = new MaintopicCreateResponse(1L, "title", "desc", "date");
        String newCreatedAt = "2023-12-02T15:30:00";

        response.setCreatedAt(newCreatedAt);

        assertEquals(newCreatedAt, response.getCreatedAt());
    }

    @Test
    void testSetNullValuesWithSetters() {
        MaintopicCreateResponse response = new MaintopicCreateResponse(1L, "title", "desc", "date");

        response.setId(null);
        response.setTitle(null);
        response.setDescription(null);
        response.setCreatedAt(null);

        assertNull(response.getId());
        assertNull(response.getTitle());
        assertNull(response.getDescription());
        assertNull(response.getCreatedAt());
    }
}
