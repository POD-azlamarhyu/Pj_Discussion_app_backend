package com.application.discussion.project.application.services.topics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.infrastructure.repositories.topics.MaintopicRepositoryImpl;


@ExtendWith(MockitoExtension.class)
public class MaintopicsListServiceImplTests {

    @Mock
    private MaintopicRepositoryImpl maintopicRepository;

    @InjectMocks
    private MaintopicsListServiceImpl maintopicsListServiceImpl;

    private Maintopic maintopic1;
    private Maintopic maintopic2;

    @BeforeEach
    void setUp() {
        // This method can be used to set up any common test data or configurations
        // if needed in the future.
        MockitoAnnotations.openMocks(this);
        maintopic1 = new Maintopic(
            1L, 
            "日本の政治体制の危うさについて", 
            "日本の政治体制について議論する場所", 
            LocalDateTime.of(2025, 12, 31, 10, 10, 10), 
            LocalDateTime.of(2025, 12, 31, 10, 20, 10), 
            false, 
            false
        );

        maintopic2 = new Maintopic(
            2L, 
            "Sample Topic 2", 
            "Description for Sample Topic 2", 
            LocalDateTime.of(2025, 12, 31, 10, 10, 10), 
            LocalDateTime.of(2025, 12, 31, 10, 20, 10), 
            false, 
            false
        );

        
    }

    @Test
    void testGetMaintopicList(){
        // This method will contain the test logic for retrieving the list of maintopics.
        // The actual implementation will depend on the specific requirements and behavior
        // of the MaintopicsListServiceImpl class.
        // Assuming Maintopic is the entity returned by the repository
        List<Maintopic> mockList = Arrays.asList(maintopic1, maintopic2);
        when(maintopicRepository.findMaintopicList()).thenReturn(mockList);

        List<MaintopicListResponse> response = maintopicsListServiceImpl.service();
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getMaintopicId());
        assertEquals("日本の政治体制の危うさについて", response.get(0).getTitle());
        assertEquals("日本の政治体制について議論する場所", response.get(0).getDescription());
        assertEquals(LocalDateTime.of(2025, 12, 31, 10, 10, 10), response.get(0).getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 12, 31, 10, 20, 10), response.get(0).getUpdatedAt());

        assertEquals(2L, response.get(1).getMaintopicId());
        assertEquals("Sample Topic 2", response.get(1).getTitle());
        assertEquals("Description for Sample Topic 2", response.get(1).getDescription());
        assertEquals(LocalDateTime.of(2025, 12, 31, 10, 10, 10), response.get(1).getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 12, 31, 10, 20, 10), response.get(1).getUpdatedAt());

        verify(maintopicRepository, times(1)).findMaintopicList();
    }

}
