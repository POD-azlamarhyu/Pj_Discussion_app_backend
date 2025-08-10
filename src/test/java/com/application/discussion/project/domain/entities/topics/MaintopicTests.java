package com.application.discussion.project.domain.entities.topics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;

public class MaintopicTests {
    private Maintopic testMaintopic;

    @BeforeEach
    void setUp(){
        testMaintopic = Maintopic.of(
            1L, 
            "Sample Title", 
            "Sample Description", 
            LocalDateTime.of(2026, 7, 10, 12, 0, 0), 
            LocalDateTime.of(2026, 7, 10, 12, 0, 0),
            false, 
            false
        );
    }

    @Test
    void testMaintopicCreation(){
        final Maintopic maintopic = Maintopic.create(
            Title.of("New Title"),
            Description.of("New Description")
        );

        assertNotNull(maintopic);
        assertInstanceOf(Maintopic.class, maintopic);
        assertEquals("New Title", maintopic.getTitle());
        assertEquals("New Description", maintopic.getDescription());
    }

    @Test
    void testMaintopicOf(){
        final Maintopic maintopic = Maintopic.of(
            99L,
            "Test Title",
            "Test Description",
            LocalDateTime.now(),
            LocalDateTime.now(),
            false,
            false
        );
        assertNotNull(maintopic);
        assertInstanceOf(Maintopic.class, maintopic);
        assertEquals(99L, maintopic.getMaintopicId());
        assertEquals("Test Title", maintopic.getTitle());
        assertEquals("Test Description", maintopic.getDescription());
        assertFalse(maintopic.getIsDeleted());
        assertFalse(maintopic.getIsClosed());
    }


    @Test
    void testGetMaintopicId() throws Exception {
        assertEquals(1L, testMaintopic.getMaintopicId());
    }

    @Test
    void testGetTitle() throws Exception {
        assertEquals("Sample Title", testMaintopic.getTitle());
    }

    @Test
    void testGetDescription() throws Exception {
        assertEquals("Sample Description", testMaintopic.getDescription());
    }

    @Test
    void testGetCreatedAt() throws Exception {
        assertEquals(LocalDateTime.of(2026, 7, 10, 12, 0, 0), testMaintopic.getCreatedAt());
    }

    @Test
    void testGetUpdatedAt() throws Exception {
        assertEquals(LocalDateTime.of(2026, 7, 10, 12, 0, 0), testMaintopic.getUpdatedAt());
    }

    @Test
    void testIsDeleted() throws Exception {
        assertFalse(testMaintopic.getIsDeleted());
    }

    @Test
    void testIsClosed()  throws Exception {
        assertFalse(testMaintopic.getIsClosed());
    }


}
