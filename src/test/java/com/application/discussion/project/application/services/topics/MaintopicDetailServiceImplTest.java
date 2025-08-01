package com.application.discussion.project.application.services.topics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.application.dtos.topics.MaintopicResponse;
import com.application.discussion.project.infrastructure.repositories.topics.MaintopicRepositoryImpl;

@ExtendWith(MockitoExtension.class)
public class MaintopicDetailServiceImplTest {

    @Mock
    private MaintopicRepositoryImpl maintopicRepository;

    @InjectMocks
    private MaintopicDetailServiceImpl maintopicDetailServiceImpl;

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
    void testGetMaintopicDetails(){
        when(maintopicRepository.findMaintopicById(1L)).thenReturn(maintopic1);
        MaintopicResponse response = maintopicDetailServiceImpl.service(1L);

        assertNotNull(response);
        assertEquals(1L, response.getMaintopicId());

        verify(maintopicRepository, times(1)).findMaintopicById(1L);
    }

    @Test
    void testGetMaintopicDetailsDifferent(){
        when(maintopicRepository.findMaintopicById(2L)).thenReturn(maintopic2);
        MaintopicResponse response = maintopicDetailServiceImpl.service(2L);
        assertNotNull(response);
        assertNotEquals(1, response.getMaintopicId());
        
        verify(maintopicRepository, times(1)).findMaintopicById(2L);
    }

    @Test
    void testGetMaintopicDetailsNotFound() {
        when(maintopicRepository.findMaintopicById(999L)).thenThrow(new RuntimeException("Topic not found"));
        assertThrows(RuntimeException.class, () -> maintopicDetailServiceImpl.service(999L));
        verify(maintopicRepository, times(1)).findMaintopicById(999L);
    }
}
