package com.application.discussion.project.presentation.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.application.services.topics.MaintopicsListServiceImpl;


@WebMvcTest(MainTopicController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false) 
public class MaintopicControllerTests {
    // This class will contain tests for the MaintopicController.
    // The actual implementation will depend on the specific requirements and behavior
    // of the MaintopicController class.
    // You can add test methods here to verify the controller's endpoints and logic.
    // Example test method (to be implemented):
    private MaintopicListResponse response1;
    private MaintopicListResponse response2;
    

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private MaintopicsListServiceImpl maintopicsListService;

    @BeforeEach
    void setUp(){
        // This method can be used to set up any common test data or configurations
        // if needed in the future.
        MockitoAnnotations.openMocks(this);
        response1 = new MaintopicListResponse(
            1L, 
            "日本の政治体制の危うさについて", 
            "日本の政治体制について議論する場所", 
            LocalDateTime.of(2025, 12, 31, 10, 10, 10), 
            LocalDateTime.of(2025, 12, 31, 10, 20, 10), 
            false, false
        );
        response2 = new MaintopicListResponse(
            2L, 
            "Sample Topic 2", 
            "Description for Sample Topic 2", 
            LocalDateTime.of(2025, 12, 31, 10, 10, 10), 
            LocalDateTime.of(2025, 12, 31, 10, 20, 10), 
            false, false
        );
    }

    @DisplayName("MaintopicControllerのメイントピックリスト取得テスト")
    @Test
    void testFindMaintopicList() throws Exception {
        // This method will contain the test logic for retrieving the list of maintopics.
        // The actual implementation will depend on the specific requirements and behavior
        // of the MaintopicController class.
        // Assuming MaintopicListResponse is the response DTO returned by the service
        // when fetching the list of maintopics.
        
        List<MaintopicListResponse> mockResponse = Arrays.asList(response1, response2);
        when(maintopicsListService.service()).thenReturn(mockResponse);

        // Act & Assert: エンドポイントをテスト
        mockMvc.perform(get("/maintopics"))
                .andDo(print()) // リクエストとレスポンスを出力
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].maintopicId").value(1L))
                .andExpect(jsonPath("$[1].maintopicId").value(2L));

        verify(maintopicsListService, times(1)).service();
    }
}
