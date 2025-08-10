package com.application.discussion.project.presentation.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicResponse;
import com.application.discussion.project.application.services.topics.MaintopicCreateServiceImpl;
import com.application.discussion.project.application.services.topics.MaintopicDetailServiceImpl;
import com.application.discussion.project.application.services.topics.MaintopicsListServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MainTopicController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class MaintopicControllerTests {
    // This class will contain tests for the MaintopicController.
    // The actual implementation will depend on the specific requirements and
    // behavior
    // of the MaintopicController class.
    // You can add test methods here to verify the controller's endpoints and logic.
    // Example test method (to be implemented):
    private MaintopicListResponse response1;
    private MaintopicListResponse response2;
    private MaintopicResponse response3;
    private MaintopicCreateRequest testMaintopicCreateRequest;
    private MaintopicCreateResponse testMaintopicCreateResponse;

    private final String testCreateRequestTitle = "HogeFuge";
    private final String testCreateRequestDescription = "HogeFuge Description";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaintopicsListServiceImpl maintopicsListService;

    @MockitoBean
    private MaintopicDetailServiceImpl maintopicDetailService;

    @MockitoBean
    private MaintopicCreateServiceImpl maintopicCreateServiceImpl;

    @InjectMocks
    private MainTopicController maintopicController;

    @BeforeEach
    void setUp() {
        // This method can be used to set up any common test data or configurations
        // if needed in the future.
        MockitoAnnotations.openMocks(this);
        response1 = new MaintopicListResponse(
                1L,
                "日本の政治体制の危うさについて",
                "日本の政治体制について議論する場所",
                LocalDateTime.of(2025, 12, 31, 10, 10, 10),
                LocalDateTime.of(2025, 12, 31, 10, 20, 10),
                false, false);
        response2 = new MaintopicListResponse(
                2L,
                "Sample Topic 2",
                "Description for Sample Topic 2",
                LocalDateTime.of(2025, 12, 31, 10, 10, 10),
                LocalDateTime.of(2025, 12, 31, 10, 20, 10),
                false, false);

        testMaintopicCreateRequest = new MaintopicCreateRequest(
                testCreateRequestTitle,
                testCreateRequestDescription);
        testMaintopicCreateResponse = new MaintopicCreateResponse(
                4L,
                testCreateRequestTitle,
                testCreateRequestDescription,
                LocalDateTime.now().toString());
    }

    @DisplayName("MaintopicControllerのメイントピックリスト取得テスト")
    @Test
    void testFindMaintopicList() throws Exception {
        // This method will contain the test logic for retrieving the list of
        // maintopics.
        // The actual implementation will depend on the specific requirements and
        // behavior
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

    @DisplayName("MaintopicControllerのメイントピック詳細取得テスト")
    @Test
    void testFindMaintopicDetail() throws Exception {
        response3 = new MaintopicResponse(
                3L,
                "Unko",
                "Unko",
                LocalDateTime.of(2025, 12, 31, 10, 10, 10),
                LocalDateTime.of(2025, 12, 31, 10, 20, 10),
                false,
                false);
        when(maintopicDetailService.service(3L)).thenReturn(response3);

        mockMvc.perform(get("/maintopics/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.maintopicId").value(3L))
                .andExpect(jsonPath("$.title").value("Unko"))
                .andExpect(jsonPath("$.description").value("Unko"));

        verify(maintopicDetailService, times(1)).service(3L);
    }

    @DisplayName("MaintopicControllerのメイントピック詳細取得テスト - 存在しないID")
    @Test
    void testFindMaintopicDetailNotFound() throws Exception {
        when(maintopicDetailService.service(999L)).thenReturn(null);

        mockMvc.perform(get("/maintopics/999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").doesNotExist());
        verify(maintopicDetailService, times(1)).service(999L);
    }

    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト")
    void testCreateMaintopic() throws Exception {

        when(maintopicCreateServiceImpl.service(any(MaintopicCreateRequest.class))).thenReturn(
                testMaintopicCreateResponse);

        String requestJson = objectMapper.writeValueAsString(testMaintopicCreateRequest);
        mockMvc.perform(
                post("/maintopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testMaintopicCreateResponse.getId()))
                .andExpect(jsonPath("$.title").value(testMaintopicCreateResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(testMaintopicCreateResponse.getDescription()));

        verify(maintopicCreateServiceImpl, times(1)).service(any(MaintopicCreateRequest.class));
    }

    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - タイトルが空の場合")
    void testCreateMaintopicWithEmptyTitle() throws Exception {
        MaintopicCreateRequest invalidRequest = new MaintopicCreateRequest("", testCreateRequestDescription);

        String requestJson = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(
                post("/maintopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }

    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - 説明が空の場合")
    void testCreateMaintopicWithEmptyDescription() throws Exception {
        MaintopicCreateRequest invalidRequest = new MaintopicCreateRequest(testCreateRequestTitle, "");

        String requestJson = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(
                post("/maintopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }

    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - nullのリクエストボディ")
    void testCreateMaintopicWithNullRequest() throws Exception {
        mockMvc.perform(
                post("/maintopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }

    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - 不正なJSON形式")
    void testCreateMaintopicWithInvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(
                post("/maintopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }

    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - サービス層で例外発生")
    void testCreateMaintopicServiceException() throws Exception {
        when(maintopicCreateServiceImpl.service(any(MaintopicCreateRequest.class)))
                .thenThrow(new InternalServerErrorException("サービス層でエラーが発生しました", "SERVICE_ERROR"));

        String requestJson = objectMapper.writeValueAsString(testMaintopicCreateRequest);
        mockMvc.perform(
                post("/maintopics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(maintopicCreateServiceImpl, times(1)).service(any(MaintopicCreateRequest.class));
    }
}
