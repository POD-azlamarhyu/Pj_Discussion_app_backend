package com.application.discussion.project.domain.entities.topics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MaintopicTests {
    private Maintopic maintopic;

    @BeforeEach
    void setUp(){
        maintopic = Maintopic.of(
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
    void testGetMaintopicId() throws Exception {
        assertEquals(1L, maintopic.getMaintopicId());
    }

    @Test
    void testGetTitle() throws Exception {
        assertEquals("Sample Title", maintopic.getTitle());
    }

    @Test
    void testGetDescription() throws Exception {
        assertEquals("Sample Description", maintopic.getDescription());
    }

    @Test
    void testGetCreatedAt() throws Exception {
        assertEquals(LocalDateTime.of(2026, 7, 10, 12, 0, 0), maintopic.getCreatedAt());
    }

    @Test
    void testGetUpdatedAt() throws Exception {
        assertEquals(LocalDateTime.of(2026, 7, 10, 12, 0, 0), maintopic.getUpdatedAt());
    }

    @Test
    void testIsDeleted() throws Exception {
        assertFalse(maintopic.getIsDeleted());
    }

    @Test
    void testIsClosed()  throws Exception {
        assertFalse(maintopic.getIsClosed());
    }


}
