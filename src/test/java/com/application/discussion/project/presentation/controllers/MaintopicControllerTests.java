package com.application.discussion.project.presentation.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicDeleteResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicListResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicResponse;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateResponse;
import com.application.discussion.project.application.services.topics.MaintopicCreateService;
import com.application.discussion.project.application.services.topics.MaintopicDeleteService;
import com.application.discussion.project.application.services.topics.MaintopicDetailService;
import com.application.discussion.project.application.services.topics.MaintopicUpdateService;
import com.application.discussion.project.application.services.topics.MaintopicsListService;
import com.application.discussion.project.domain.exceptions.BadRequestException;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;
import com.application.discussion.project.presentation.exceptions.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.application.discussion.project.presentation.config.WebSecurityConfig;


/**
 * ! ContextConfigurationを使用して、クラスを指定するとエラーが起きる
 * ? Beanの生成ができなくなるエラーと考えられる
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import({GlobalExceptionHandler.class, WebSecurityConfig.class})
@ContextConfiguration
@ActiveProfiles("dev")
@DisplayName("MaintopicControllerの単体テスト")
public class MaintopicControllerTests {

    private static final String SUCCESS_DELETE_MESSAGE = "メイントピックが正常に削除されました";
    private static final String NOT_FOUND_DELETE_MESSAGE = "メイントピックは存在しません";
    private static final String CANNOT_DELETE_MESSAGE = "このメイントピックは削除できません";
    private static final String DELETE_RESPONSE_MESSAGE = "このリソースは存在しません";
    private static final String MAINTOPIC_ID_TYPE = "メイントピックIDが不正です";
	private MaintopicListResponse response1;
    private MaintopicListResponse response2;
    private MaintopicResponse response3;
    private MaintopicCreateRequest testMaintopicCreateRequest;
    private MaintopicCreateResponse testMaintopicCreateResponse;
    private MaintopicUpdateRequest testMaintopicUpdateRequest;
    private MaintopicUpdateResponse testMaintopicUpdateResponse;
    private MaintopicDeleteResponse testMaintopicDeleteResponse;

    private final String testCreateRequestTitle = "HogeFuge";
    private final String testCreateRequestDescription = "HogeFuge Description";
    private final String testUpdateRequestTitle = "Updated Title";
    private final String testUpdateRequestDescription = "Updated Description";
	private final LocalDateTime testCreatedAt = LocalDateTime.of(2025, 12, 31, 10, 10, 10);
	private final LocalDateTime testUpdatedAt = LocalDateTime.of(2026, 1, 31, 10, 20, 10);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaintopicsListService maintopicsListService;

    @MockitoBean
    private MaintopicDetailService maintopicDetailService;

    @MockitoBean
    private MaintopicCreateService maintopicCreateServiceImpl;

	@MockitoBean
	private MaintopicUpdateService maintopicUpdateServiceImpl;

    @MockitoBean
    private MaintopicDeleteService maintopicDeleteServiceImpl;

    @BeforeEach
    void setUp() {
        response1 = new MaintopicListResponse(
			1L,
			"日本の政治体制の危うさについて",
			"日本の政治体制について議論する場所",
			LocalDateTime.of(2025, 12, 31, 10, 10, 10),
			LocalDateTime.of(2025, 12, 31, 10, 20, 10),
			false, 
			false
		);
        response2 = new MaintopicListResponse(
			2L,
			"Sample Topic 2",
			"Description for Sample Topic 2",
			LocalDateTime.of(2025, 12, 31, 10, 10, 10),
			LocalDateTime.of(2025, 12, 31, 10, 20, 10),
			false, 
			false
		);

        testMaintopicCreateRequest = new MaintopicCreateRequest(
			testCreateRequestTitle,
			testCreateRequestDescription
		);
        testMaintopicCreateResponse = new MaintopicCreateResponse(
			4L,
			testCreateRequestTitle,
			testCreateRequestDescription,
			LocalDateTime.now().toString()
		);
        testMaintopicUpdateRequest = new MaintopicUpdateRequest(
            testUpdateRequestTitle,
            testUpdateRequestDescription
        );
        testMaintopicUpdateResponse = new MaintopicUpdateResponse(
            1L,
            testUpdateRequestTitle,
            testUpdateRequestDescription,
            testCreatedAt.toString(),
			testUpdatedAt.toString()
        );
        testMaintopicDeleteResponse = new MaintopicDeleteResponse();
    }

    @WithMockUser
    @DisplayName("MaintopicControllerのメイントピックリスト取得テスト")
    @Test
    void testFindMaintopicList() throws Exception {
        List<MaintopicListResponse> mockResponse = Arrays.asList(response1, response2);
        when(maintopicsListService.service()).thenReturn(mockResponse);

        mockMvc.perform(get("/maintopics"))
			.andDo(print()) 
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].maintopicId").value(1L))
			.andExpect(jsonPath("$[1].maintopicId").value(2L)
		);

        verify(maintopicsListService, times(1)).service();
    }

    @WithMockUser
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
			false
		);
        when(maintopicDetailService.service(3L)).thenReturn(response3);

        mockMvc.perform(get("/maintopics/3"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.maintopicId").value(3L))
			.andExpect(jsonPath("$.title").value("Unko"))
			.andExpect(jsonPath("$.description").value("Unko")
		);

        verify(maintopicDetailService, times(1)).service(3L);
    }
    @WithMockUser
    @DisplayName("MaintopicControllerのメイントピック詳細取得テスト - 存在しないID")
    @Test
    void testFindMaintopicDetailNotFound() throws Exception {
        when(maintopicDetailService.service(999L)).thenReturn(null);

        mockMvc.perform(get("/maintopics/999"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").doesNotExist()
		);
        verify(maintopicDetailService, times(1)).service(999L);
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト")
    void testCreateMaintopic() throws Exception {

        when(maintopicCreateServiceImpl.service(
			any(MaintopicCreateRequest.class))
		).thenReturn(testMaintopicCreateResponse);

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
			.andExpect(jsonPath("$.description").value(testMaintopicCreateResponse.getDescription())
		);

        verify(maintopicCreateServiceImpl, times(1)).service(any(MaintopicCreateRequest.class));
    }
    @WithMockUser
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
			.andExpect(status().isBadRequest()
		);

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }
    @WithMockUser
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
			.andExpect(status().isBadRequest()
		);

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - nullのリクエストボディ")
    void testCreateMaintopicWithNullRequest() throws Exception {
        mockMvc.perform(
			post("/maintopics")
			.contentType(MediaType.APPLICATION_JSON)
			.content(""))
			.andDo(print())
			.andExpect(status().isBadRequest()
		);

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック作成テスト - 不正なJSON形式")
    void testCreateMaintopicWithInvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(
			post("/maintopics")
			.contentType(MediaType.APPLICATION_JSON)
			.content(invalidJson))
			.andDo(print())
			.andExpect(status().isBadRequest()
		);

        verify(maintopicCreateServiceImpl, never()).service(any(MaintopicCreateRequest.class));
    }
    @WithMockUser
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
			.andExpect(status().isInternalServerError()
		);

        verify(maintopicCreateServiceImpl, times(1)).service(any(MaintopicCreateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック更新テスト")
    void testUpdateMaintopic() throws Exception {
        when(maintopicUpdateServiceImpl.service(eq(1L), any(MaintopicUpdateRequest.class)))
            .thenReturn(testMaintopicUpdateResponse);

        String requestJson = objectMapper.writeValueAsString(testMaintopicUpdateRequest);
        mockMvc.perform(
            put("/maintopics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testMaintopicUpdateResponse.getId()))
            .andExpect(jsonPath("$.title").value(testMaintopicUpdateResponse.getTitle()))
            .andExpect(jsonPath("$.description").value(testMaintopicUpdateResponse.getDescription()));

        verify(maintopicUpdateServiceImpl, times(1)).service(eq(1L), any(MaintopicUpdateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック更新テスト - タイトルが空の場合")
    void testUpdateMaintopicWithEmptyTitle() throws Exception {
        when(maintopicUpdateServiceImpl.service(eq(1L), any(MaintopicUpdateRequest.class))).thenThrow(new BadRequestException("タイトルは必須です", "BAD_REQUEST"));
        MaintopicUpdateRequest invalidRequest = new MaintopicUpdateRequest("", testUpdateRequestDescription);

        String requestJson = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(
            put("/maintopics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(maintopicUpdateServiceImpl, times(1)).service(eq(1L), any(MaintopicUpdateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック更新テスト - 説明が空の場合")
    void testUpdateMaintopicWithEmptyDescription() throws Exception {
        when(maintopicUpdateServiceImpl.service(eq(1L), any(MaintopicUpdateRequest.class))).thenThrow(new BadRequestException("説明は必須です", "BAD_REQUEST"));
        MaintopicUpdateRequest invalidRequest = new MaintopicUpdateRequest(testUpdateRequestTitle, "");

        String requestJson = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(
            put("/maintopics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(maintopicUpdateServiceImpl, times(1)).service(eq(1L), any(MaintopicUpdateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック更新テスト - 存在しないID")
    void testUpdateMaintopicNotFound() throws Exception {
		when(maintopicUpdateServiceImpl.service(eq(1000L), any(MaintopicUpdateRequest.class))).thenThrow(new ResourceNotFoundException("メイントピックが見つかりません", "NOT_FOUND"));

        String requestJson = objectMapper.writeValueAsString(testMaintopicUpdateRequest);

        mockMvc.perform(
            put("/maintopics/1000")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(maintopicUpdateServiceImpl, times(1)).service(eq(1000L), any(MaintopicUpdateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック更新テスト - サービス層で例外発生")
    void testUpdateMaintopicServiceException() throws Exception {
        when(maintopicUpdateServiceImpl.service(eq(1L), any(MaintopicUpdateRequest.class)))
            .thenThrow(new InternalServerErrorException("更新処理でエラーが発生しました", "INTERNAL_SERVER_ERROR"));

        String requestJson = objectMapper.writeValueAsString(testMaintopicUpdateRequest);
        mockMvc.perform(
            put("/maintopics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(maintopicUpdateServiceImpl, times(1)).service(eq(1L), any(MaintopicUpdateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック更新テスト - 不正なJSON形式")
    void testUpdateMaintopicWithInvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(
            put("/maintopics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(maintopicUpdateServiceImpl, never()).service(anyLong(), any(MaintopicUpdateRequest.class));
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック削除テスト")
    void testDeleteMaintopic() throws Exception {
        when(maintopicDeleteServiceImpl.service(1L)).thenReturn(testMaintopicDeleteResponse);

        mockMvc.perform(delete("/maintopics/1"))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value(DELETE_RESPONSE_MESSAGE));

        verify(maintopicDeleteServiceImpl, times(1)).service(1L);
    }

    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック削除テスト - 存在しないID")
    void testDeleteMaintopicNotFound() throws Exception {
        when(maintopicDeleteServiceImpl.service(999L)).thenThrow(
            new ApplicationLayerException(
                DELETE_RESPONSE_MESSAGE,
                HttpStatus.NOT_FOUND,
                HttpStatusCode.valueOf(404)
            )
        );

        mockMvc.perform(delete("/maintopics/999"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(maintopicDeleteServiceImpl, times(1)).service(999L);
    }

    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック削除テスト - 無効なID（負の値）")
    void testDeleteMaintopicInvalidId() throws Exception {
        mockMvc.perform(delete("/maintopics/-1"))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(maintopicDeleteServiceImpl, never()).service(-1L);
    }
    @WithMockUser
    @Test
    @DisplayName("MaintopicControllerのメイントピック削除テスト - 大きなID値")
    void testDeleteMaintopicLargeId() throws Exception {
        Long largeId = Long.MAX_VALUE;
        MaintopicDeleteResponse response = new MaintopicDeleteResponse();
        when(maintopicDeleteServiceImpl.service(largeId)).thenReturn(response);

        mockMvc.perform(delete("/maintopics/" + largeId))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value(DELETE_RESPONSE_MESSAGE));

        verify(maintopicDeleteServiceImpl, times(1)).service(largeId);
    }
}
