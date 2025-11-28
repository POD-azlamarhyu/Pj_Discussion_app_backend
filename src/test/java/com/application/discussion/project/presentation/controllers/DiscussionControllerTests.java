package com.application.discussion.project.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.application.dtos.discussions.DiscussionListResponse;
import com.application.discussion.project.application.dtos.discussions.DiscussionResponse;
import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.services.discussions.DiscussionCreateService;
import com.application.discussion.project.application.services.discussions.DiscussionListService;
import com.application.discussion.project.presentation.config.WebSecurityConfig;
import com.application.discussion.project.presentation.exceptions.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.test.context.support.WithUserDetails;


@SpringBootTest
// @ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@DisplayName("DiscussionController クラスのテスト")
public class DiscussionControllerTests {

    private static final Long TEST_MAINTOPIC_ID = 1L;
    private static final Long TEST_DISCUSSION_ID = 1L;
    private static final Long TEST_DISCUSSION_ID_2 = 2L;
    private static final String VALID_PARAGRAPH = "これは有効なディスカッションの内容です";
    private static final String EMPTY_PARAGRAPH = "";
    private static final String INVALID_JSON = "{ invalid json }";
    private static final String EXCEPTION_MESSAGE = "メイントピックが見つかりません";
    private static final int MAX_CONTENT_LENGTH = 2000;
    private static final int OVER_MAX_CONTENT_LENGTH = 2001;
    private static final int VERIFY_TIMES_ONE = 1;
    private static final Integer STATUS_CODE_INTERNAL_SERVER_ERROR = 500;
    
    private static final Long LIST_DISCUSSION_ID_1 = 10L;
    private static final Long LIST_DISCUSSION_ID_2 = 11L;
    private static final Long LIST_DISCUSSION_ID_3 = 12L;
    private static final String LIST_PARAGRAPH_1 = "議論内容1";
    private static final String LIST_PARAGRAPH_2 = "議論内容2";
    private static final String LIST_PARAGRAPH_3 = "議論内容3";
    private static final LocalDateTime LIST_CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
    private static final LocalDateTime LIST_UPDATED_AT = LocalDateTime.of(2024, 1, 15, 15, 45, 0);
    
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer CUSTOM_PAGE_NUMBER = 1;
    private static final Integer CUSTOM_PAGE_SIZE = 5;
    private static final Integer TOTAL_COUNT = 25;
    private static final Integer TOTAL_PAGES = 3;
    private static final Integer ZERO_COUNT = 0;
    private static final Integer PAGE_SIZE_ONE = 1;
    
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final String UPDATED_AT_SORT = "updatedAt";
    private static final String ASC_DIRECTION = "asc";
    private static final String DESC_DIRECTION = "desc";
    private static final String MIXED_CASE_DIRECTION = "AsC";
    
    private static final String DISCUSSIONS_URL = "/v1/maintopics/{maintopicId}/discussions";
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiscussionCreateService discussionCreateService;

    @MockitoBean
    private DiscussionListService discussionListService;

    @Autowired
    private ObjectMapper objectMapper;

    private DiscussionCreateRequest validRequest;
    private DiscussionCreateResponse mockResponse;

    // @InjectMocks
    // private DiscussionController discussionController;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
        validRequest = new DiscussionCreateRequest(VALID_PARAGRAPH);
        
        mockResponse = new DiscussionCreateResponse(
            TEST_DISCUSSION_ID,
            VALID_PARAGRAPH,
            TEST_MAINTOPIC_ID
        );
    }

    
    @Test
    @WithUserDetails
    @DisplayName("有効なリクエストでディスカッション作成が成功することを確認する")
    void createDiscussionWithValidRequestTest() throws Exception {
        
        when(discussionCreateService.service(eq(TEST_MAINTOPIC_ID), any(DiscussionCreateRequest.class)))
                .thenReturn(mockResponse);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.discussionId").value(TEST_DISCUSSION_ID))
                .andExpect(jsonPath("$.paragraph").value(VALID_PARAGRAPH))
                .andExpect(jsonPath("$.maintopicId").value(TEST_MAINTOPIC_ID));

        verify(discussionCreateService, times(VERIFY_TIMES_ONE)).service(eq(TEST_MAINTOPIC_ID), any(DiscussionCreateRequest.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("空のコンテンツでリクエストした場合にバリデーションエラーが発生することを確認する")
    void createDiscussionWithEmptyContentTest() throws Exception {
        
        DiscussionCreateRequest invalidRequest = new DiscussionCreateRequest(EMPTY_PARAGRAPH);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    
    @Test
    @WithUserDetails
    @DisplayName("nullコンテンツでリクエストした場合にバリデーションエラーが発生することを確認する")
    void createDiscussionWithNullContentTest() throws Exception {
        
        DiscussionCreateRequest invalidRequest = new DiscussionCreateRequest(null);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    
    @Test
    @WithUserDetails
    @DisplayName("1000文字を超えるコンテンツでリクエストした場合にバリデーションエラーが発生することを確認する")
    void createDiscussionWithTooLongContentTest() throws Exception {
        
        String longContent = "あ".repeat(OVER_MAX_CONTENT_LENGTH);
        DiscussionCreateRequest invalidRequest = new DiscussionCreateRequest(longContent);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    
    @Test
    @WithUserDetails
    @DisplayName("サービス層でApplicationLayerExceptionが発生した場合の例外処理を確認する")
    void createDiscussionWithServiceExceptionTest() throws Exception {
        
        when(discussionCreateService.service(eq(TEST_MAINTOPIC_ID), any(DiscussionCreateRequest.class)))
                .thenThrow(new ApplicationLayerException(
                    EXCEPTION_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatusCode.valueOf(STATUS_CODE_INTERNAL_SERVER_ERROR)
                ));

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError());

        verify(discussionCreateService, times(VERIFY_TIMES_ONE)).service(eq(TEST_MAINTOPIC_ID), any(DiscussionCreateRequest.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("無効なパスパラメータでリクエストした場合の動作を確認する")
    void createDiscussionWithInvalidPathParameterTest() throws Exception {
        
        mockMvc.perform(post("/v1/maintopics/invalid/discussions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    
    @Test
    @WithUserDetails
    @DisplayName("JSONフォーマットが不正な場合の動作を確認する")
    void createDiscussionWithInvalidJsonTest() throws Exception {
        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_JSON))
                .andExpect(status().isBadRequest());
    }

    
    @Test
    @WithUserDetails
    @DisplayName("Content-Typeヘッダーが指定されていない場合の動作を確認する")
    void createDiscussionWithoutContentTypeTest() throws Exception {
        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnsupportedMediaType());
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常なコンテンツ長制限内でディスカッション作成が成功することを確認する")
    void createDiscussionWithMaxLengthContentTest() throws Exception {
        
        String maxLengthContent = "あ".repeat(MAX_CONTENT_LENGTH);
        DiscussionCreateRequest maxLengthRequest = new DiscussionCreateRequest(maxLengthContent);

        DiscussionCreateResponse maxLengthResponse = new DiscussionCreateResponse(
            TEST_DISCUSSION_ID_2,
            maxLengthContent,
            TEST_MAINTOPIC_ID
        );

        when(discussionCreateService.service(eq(TEST_MAINTOPIC_ID), any(DiscussionCreateRequest.class)))
                .thenReturn(maxLengthResponse);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(maxLengthRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.discussionId").value(TEST_DISCUSSION_ID_2))
                .andExpect(jsonPath("$.paragraph").value(maxLengthContent))
                .andExpect(jsonPath("$.maintopicId").value(TEST_MAINTOPIC_ID));

        verify(discussionCreateService, times(VERIFY_TIMES_ONE)).service(eq(TEST_MAINTOPIC_ID), any(DiscussionCreateRequest.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: デフォルトパラメータで議論リストを取得できること")
    void getDiscussionsWithDefaultParametersTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            TOTAL_PAGES
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), any(Pageable.class)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.discussions", hasSize(3)))
            .andExpect(jsonPath("$.totalCount", is(TOTAL_COUNT)))
            .andExpect(jsonPath("$.currentPage", is(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.pageSize", is(DEFAULT_PAGE_SIZE)))
            .andExpect(jsonPath("$.totalPages", is(TOTAL_PAGES)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), any(Pageable.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: カスタムページパラメータで議論リストを取得できること")
    void getDiscussionsWithCustomPageParametersTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CUSTOM_PAGE_NUMBER,
            CUSTOM_PAGE_SIZE,
            TOTAL_PAGES
        );

        Pageable expectedPageable = PageRequest.of(
            CUSTOM_PAGE_NUMBER,
            CUSTOM_PAGE_SIZE,
            Sort.by(Sort.Direction.DESC, DEFAULT_SORT_BY)
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("page", String.valueOf(CUSTOM_PAGE_NUMBER))
                .param("size", String.valueOf(CUSTOM_PAGE_SIZE))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discussions", hasSize(3)))
            .andExpect(jsonPath("$.totalCount", is(TOTAL_COUNT)))
            .andExpect(jsonPath("$.currentPage", is(CUSTOM_PAGE_NUMBER)))
            .andExpect(jsonPath("$.pageSize", is(CUSTOM_PAGE_SIZE)))
            .andExpect(jsonPath("$.totalPages", is(TOTAL_PAGES)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: 昇順ソートで議論リストを取得できること")
    void getDiscussionsWithAscendingSortTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            TOTAL_PAGES
        );

        Pageable expectedPageable = PageRequest.of(
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            Sort.by(Sort.Direction.ASC, DEFAULT_SORT_BY)
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("direction", ASC_DIRECTION)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discussions", hasSize(3)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: updatedAtでソートして議論リストを取得できること")
    void getDiscussionsWithUpdatedAtSortTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            TOTAL_PAGES
        );

        Pageable expectedPageable = PageRequest.of(
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            Sort.by(Sort.Direction.DESC, UPDATED_AT_SORT)
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("sortBy", UPDATED_AT_SORT)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discussions", hasSize(3)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: 空のリストが返されること")
    void getDiscussionsReturnsEmptyListTest() throws Exception {
        DiscussionListResponse response = DiscussionListResponse.of(
            new ArrayList<>(),
            ZERO_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            ZERO_COUNT
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), any(Pageable.class)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discussions", hasSize(0)))
            .andExpect(jsonPath("$.totalCount", is(ZERO_COUNT)))
            .andExpect(jsonPath("$.totalPages", is(ZERO_COUNT)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), any(Pageable.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: 全てのクエリパラメータを指定して議論リストを取得できること")
    void getDiscussionsWithAllQueryParametersTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            CUSTOM_PAGE_NUMBER,
            CUSTOM_PAGE_SIZE,
            TOTAL_PAGES
        );

        Pageable expectedPageable = PageRequest.of(
            CUSTOM_PAGE_NUMBER,
            CUSTOM_PAGE_SIZE,
            Sort.by(Sort.Direction.ASC, UPDATED_AT_SORT)
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("page", String.valueOf(CUSTOM_PAGE_NUMBER))
                .param("size", String.valueOf(CUSTOM_PAGE_SIZE))
                .param("sortBy", UPDATED_AT_SORT)
                .param("direction", ASC_DIRECTION)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discussions", hasSize(3)))
            .andExpect(jsonPath("$.currentPage", is(CUSTOM_PAGE_NUMBER)))
            .andExpect(jsonPath("$.pageSize", is(CUSTOM_PAGE_SIZE)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), eq(expectedPageable));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: レスポンスのディスカッション内容が正しいこと")
    void getDiscussionsReturnsCorrectContentTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            TOTAL_PAGES
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), any(Pageable.class)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discussions[0].discussionId", is(LIST_DISCUSSION_ID_1.intValue())))
            .andExpect(jsonPath("$.discussions[0].paragraph", is(LIST_PARAGRAPH_1)))
            .andExpect(jsonPath("$.discussions[0].maintopicId", is(TEST_MAINTOPIC_ID.intValue())))
            .andExpect(jsonPath("$.discussions[1].discussionId", is(LIST_DISCUSSION_ID_2.intValue())))
            .andExpect(jsonPath("$.discussions[1].paragraph", is(LIST_PARAGRAPH_2)))
            .andExpect(jsonPath("$.discussions[2].discussionId", is(LIST_DISCUSSION_ID_3.intValue())))
            .andExpect(jsonPath("$.discussions[2].paragraph", is(LIST_PARAGRAPH_3)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), any(Pageable.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("境界値: ページ番号が0の場合でも正常に動作すること")
    void getDiscussionsWithZeroPageNumberTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            TOTAL_PAGES
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), any(Pageable.class)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentPage", is(DEFAULT_PAGE_NUMBER)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), any(Pageable.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("境界値: ページサイズが1の場合でも正常に動作すること")
    void getDiscussionsWithPageSizeOneTest() throws Exception {
        List<DiscussionResponse> discussions = List.of(
            createDiscussionResponse(LIST_DISCUSSION_ID_1, LIST_PARAGRAPH_1)
        );
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            PAGE_SIZE_ONE,
            TOTAL_COUNT
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), any(Pageable.class)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("size", String.valueOf(PAGE_SIZE_ONE))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageSize", is(PAGE_SIZE_ONE)));

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), any(Pageable.class));
    }

    
    @Test
    @WithUserDetails
    @DisplayName("正常系: directionパラメータが大文字小文字混在でも正常に動作すること")
    void getDiscussionsWithMixedCaseDirectionTest() throws Exception {
        List<DiscussionResponse> discussions = createDiscussionResponseList();
        DiscussionListResponse response = DiscussionListResponse.of(
            discussions,
            TOTAL_COUNT,
            DEFAULT_PAGE_NUMBER,
            DEFAULT_PAGE_SIZE,
            TOTAL_PAGES
        );

        when(discussionListService.service(eq(TEST_MAINTOPIC_ID), any(Pageable.class)))
            .thenReturn(response);

        mockMvc.perform(get(DISCUSSIONS_URL, TEST_MAINTOPIC_ID)
                .param("direction", MIXED_CASE_DIRECTION)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(discussionListService, times(VERIFY_TIMES_ONE))
            .service(eq(TEST_MAINTOPIC_ID), any(Pageable.class));
    }

    private List<DiscussionResponse> createDiscussionResponseList() {
        return List.of(
            createDiscussionResponse(LIST_DISCUSSION_ID_1, LIST_PARAGRAPH_1),
            createDiscussionResponse(LIST_DISCUSSION_ID_2, LIST_PARAGRAPH_2),
            createDiscussionResponse(LIST_DISCUSSION_ID_3, LIST_PARAGRAPH_3)
        );
    }

    private DiscussionResponse createDiscussionResponse(Long discussionId, String paragraph) {
        return DiscussionResponse.of(
            discussionId,
            paragraph,
            TEST_MAINTOPIC_ID,
            LIST_CREATED_AT,
            LIST_UPDATED_AT
        );
    }
}
