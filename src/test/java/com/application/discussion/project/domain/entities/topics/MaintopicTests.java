package com.application.discussion.project.domain.entities.topics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;

public class MaintopicTests {
    private Maintopic testMaintopic;
    private Title testTitle;
    private Description testDescription;
    private final String TITLE_TEXT = "タイトル名のテストサンプル";
    private final String DESCRIPTION_TEXT = "説明文のテストサンプル";
    private final String ALTERNATE_TITLE_TEXT = "別のタイトル名のテストサンプル";
    private final String ALTERNATE_DESCRIPTION_TEXT = "別の説明文のテストサンプル";

    @BeforeEach
    void setUp(){
        testMaintopic = Maintopic.of(
            1L,
            "Sample Title", 
            "Sample Description", 
            UUID.randomUUID(),
            LocalDateTime.of(2026, 7, 10, 12, 0, 0), 
            LocalDateTime.of(2026, 7, 10, 12, 0, 0),
            false, 
            false
        );

        testTitle = Title.of(TITLE_TEXT);
        testDescription = Description.of(DESCRIPTION_TEXT);
    }

    @Test
    void testMaintopicCreation(){
        final Maintopic maintopic = Maintopic.create(
            Title.of("New Title"),
            Description.of("New Description"),
            UUID.randomUUID()
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
            UUID.randomUUID(),
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

    @Test
    void testMaintopicInstance() throws Exception {
        assertInstanceOf(Maintopic.class, testMaintopic);
    }

    @Test
    void testUpdateMaintopicEntity() throws Exception {
        final Maintopic updatedMaintopic = testMaintopic.update(
            testTitle,
            testDescription
        );

        assertNotNull(updatedMaintopic);
        assertEquals(testTitle.getValue(), updatedMaintopic.getTitle());
        assertEquals(testDescription.getValue(), updatedMaintopic.getDescription());
    }

    @Test
    void testUpdateNullMaintopicFailure() throws Exception {
        assertNotNull(testMaintopic);
        assertThrows(DomainLayerErrorException.class, () -> {
            testMaintopic.update(
                Title.of(""), 
                Description.of("")
            );
        });
    }

    @Test
    void testUpdateSameMaintopicFailture(){
        final Maintopic maintopic = Maintopic.create(
            Title.of(ALTERNATE_TITLE_TEXT),
            Description.of(ALTERNATE_DESCRIPTION_TEXT),
            UUID.randomUUID()
        );
        assertNotNull(maintopic);
        assertThrows(DomainLayerErrorException.class, () -> {
            maintopic.update(
                Title.of(ALTERNATE_TITLE_TEXT),
                Description.of(ALTERNATE_DESCRIPTION_TEXT)
            );
        });
    }
}
