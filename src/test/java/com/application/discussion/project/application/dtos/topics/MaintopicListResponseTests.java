package com.application.discussion.project.application.dtos.topics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

public class MaintopicListResponseTests {

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

    // 正常系テスト
    @Test
    void testConstructor() {
        MaintopicListResponse response = new MaintopicListResponse(
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
    void testInstanceCreation() {
        MaintopicListResponse response = new MaintopicListResponse(
                testMaintopicId,
                testTitle,
                testDescription,
                testCreatedAt,
                testUpdatedAt,
                testIsDeleted,
                testIsClosed);

        assertNotNull(response);
        assertInstanceOf(MaintopicListResponse.class, response);
    }

    @Test
    void testBuilder() {
        MaintopicListResponse response = MaintopicListResponse.builder()
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
        MaintopicListResponse response = new MaintopicListResponse(
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
        MaintopicListResponse response = new MaintopicListResponse(
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
        MaintopicListResponse response = new MaintopicListResponse(
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

    @Test
    void testWithDeletedAndClosedTopic() {
        MaintopicListResponse response = new MaintopicListResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            true,
            true
        );

        assertTrue(response.getIsDeleted());
        assertTrue(response.getIsClosed());
    }

    // 境界値分析テスト
    @Test
    void testWithEmptyStrings() {
        MaintopicListResponse response = new MaintopicListResponse(
            testMaintopicId,
            "",
            "",
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );

        assertEquals("", response.getTitle());
        assertEquals("", response.getDescription());
    }

    @Test
    void testWithZeroId() {
        MaintopicListResponse response = new MaintopicListResponse(
            0L,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );

        assertEquals(0L, response.getMaintopicId());
    }

    @Test
    void testWithNegativeId() {
        MaintopicListResponse response = new MaintopicListResponse(
            -1L,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );

        assertEquals(-1L, response.getMaintopicId());
    }

    @Test
    void testWithMaxId() {
        MaintopicListResponse response = new MaintopicListResponse(
            Long.MAX_VALUE,
            testTitle,
            testDescription,
            testCreatedAt,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );
        assertEquals(Long.MAX_VALUE, response.getMaintopicId());
    }

    @Test
    void testWithVeryOldDate() {
        LocalDateTime veryOldDate = LocalDateTime.of(1900, 1, 1, 0, 0);
        MaintopicListResponse response = new MaintopicListResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            veryOldDate,
            testUpdatedAt,
            testIsDeleted,
            testIsClosed
        );

        assertEquals(veryOldDate, response.getCreatedAt());
    }

    @Test
    void testWithFutureDate() {
        LocalDateTime futureDate = LocalDateTime.of(2100, 12, 31, 23, 59);
        MaintopicListResponse response = new MaintopicListResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            testCreatedAt,
            futureDate,
            testIsDeleted,
            testIsClosed
        );

        assertEquals(futureDate, response.getUpdatedAt());
    }

    @Test
    void testWithSameDateTimes() {
        LocalDateTime sameTime = LocalDateTime.now();
        MaintopicListResponse response = new MaintopicListResponse(
            testMaintopicId,
            testTitle,
            testDescription,
            sameTime,
            sameTime,
            testIsDeleted,
            testIsClosed
        );

        assertEquals(sameTime, response.getCreatedAt());
        assertEquals(sameTime, response.getUpdatedAt());
    }
}
