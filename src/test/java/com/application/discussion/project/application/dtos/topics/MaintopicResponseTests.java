package com.application.discussion.project.application.dtos.topics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

public class MaintopicResponseTests {

    private Long testMaintopicId;
    private String testTitle;
    private String testDescription;
    private LocalDateTime testCreatedAt;
    private LocalDateTime testUpdatedAt;
    private Boolean testIsDeleted;
    private Boolean testIsClosed;

    @BeforeEach
    void setUp() {
        testMaintopicId = 1L;
        testTitle = "テストタイトル";
        testDescription = "テスト説明";
        testCreatedAt = LocalDateTime.now().minusDays(1);
        testUpdatedAt = LocalDateTime.now();
        testIsDeleted = false;
        testIsClosed = false;
    }

    @Test
    void testConstructor() {
        MaintopicResponse response = new MaintopicResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );

        assertNotNull(response);
        assertEquals(testMaintopicId, response.getMaintopicId());
        assertEquals(testTitle, response.getTitle());
        assertEquals(testDescription, response.getDescription());
        assertEquals(testCreatedAt, response.getCreatedAt());
        assertEquals(testUpdatedAt, response.getUpdatedAt());
        assertEquals(testIsDeleted, response.getIsDeleted());
        assertEquals(testIsClosed, response.getIsClosed());
    }

    @Test
    void testInstanceCreation(){
        MaintopicResponse response = new MaintopicResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );

        assertNotNull(response);
        assertInstanceOf(MaintopicResponse.class, response);
    }

    @Test
    void testBuilder() {
        MaintopicResponse response = MaintopicResponse.builder()
                .maintopicId(testMaintopicId)
                .title(testTitle)
                .description(testDescription)
                .createdAt(testCreatedAt)
                .updatedAt(testUpdatedAt)
                .isDeleted(testIsDeleted)
                .isClosed(testIsClosed)
                .build();

        assertNotNull(response);
        assertEquals(testMaintopicId, response.getMaintopicId());
        assertEquals(testTitle, response.getTitle());
        assertEquals(testDescription, response.getDescription());
        assertEquals(testCreatedAt, response.getCreatedAt());
        assertEquals(testUpdatedAt, response.getUpdatedAt());
        assertEquals(testIsDeleted, response.getIsDeleted());
        assertEquals(testIsClosed, response.getIsClosed());
    }

    @Test
    void testGettersWithNullValues() {
        MaintopicResponse response = new MaintopicResponse(
            null, 
            null, 
            null, 
            null, 
            null, 
            null, 
            null
        );

        assertNull(response.getMaintopicId());
        assertNull(response.getTitle());
        assertNull(response.getDescription());
        assertNull(response.getCreatedAt());
        assertNull(response.getUpdatedAt());
        assertNull(response.getIsDeleted());
        assertNull(response.getIsClosed());
    }

    @Test
    void testWithDeletedTopic() {
        MaintopicResponse response = new MaintopicResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            true,
            testIsClosed
        );

        assertTrue(response.getIsDeleted());
    }

    @Test
    void testWithClosedTopic() {
        MaintopicResponse response = new MaintopicResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            true
        );

        assertTrue(response.getIsClosed());
    }
}
