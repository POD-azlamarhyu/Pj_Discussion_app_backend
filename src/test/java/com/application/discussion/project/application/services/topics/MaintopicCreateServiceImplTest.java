package com.application.discussion.project.application.services.topics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.infrastructure.repositories.topics.MaintopicRepositoryImpl;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;

@ExtendWith(MockitoExtension.class)
public class MaintopicCreateServiceImplTest {

    @Mock
    private MaintopicRepositoryImpl maintopicRepository;

    @InjectMocks
    private MaintopicCreateServiceImpl maintopicCreateServiceImpl;

    private Maintopic testMaintopic;
    private final String testTitle = "FugeHoge";
    private final String testDescription = "FugeHoge Description";
    private final LocalDateTime testLocalDateTime = LocalDateTime.now();
    private MaintopicCreateRequest testMaintopicCreateRequest = new MaintopicCreateRequest(
            testTitle,
            testDescription);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMaintopic = new Maintopic(
			1L,
			testTitle,
			testDescription,
			testLocalDateTime,
			testLocalDateTime,
			false,
			false
		);
    }

    @Test
    void testCreateMaintopic() {

        when(maintopicRepository.createMaintopic(any(Maintopic.class))).thenReturn(testMaintopic);

        MaintopicCreateResponse result = maintopicCreateServiceImpl.service(
			testMaintopicCreateRequest
		);

        assertNotNull(result);
        assertEquals(testMaintopic.getMaintopicId(), result.getId());
        assertEquals(testMaintopic.getTitle(), result.getTitle());
        assertEquals(testMaintopic.getDescription(), result.getDescription());
        verify(maintopicRepository, times(1)).createMaintopic(any(Maintopic.class));
    }

    @Test
    void testCreateMaintopicThrowsException() {
        when(maintopicRepository.createMaintopic(any(Maintopic.class)))
                .thenThrow(new InternalServerErrorException("登録に失敗しました。", "INTERNAL_SERVER_ERROR"));

        InternalServerErrorException thrown = assertThrows(
                InternalServerErrorException.class,
                () -> maintopicCreateServiceImpl.service(testMaintopicCreateRequest));

        assertEquals("登録に失敗しました。", thrown.getMessage());
        assertEquals("INTERNAL_SERVER_ERROR", thrown.getType());
        verify(maintopicRepository, times(1)).createMaintopic(any(Maintopic.class));
    }

}
