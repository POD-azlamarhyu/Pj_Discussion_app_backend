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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.services.discussions.DiscussionCreateService;
import com.application.discussion.project.application.services.discussions.DiscussionCreateServiceImpl;
import com.application.discussion.project.presentation.exceptions.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DiscussionController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
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
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiscussionCreateServiceImpl discussionCreateService;

    @Autowired
    private ObjectMapper objectMapper;

    private DiscussionCreateRequest validRequest;
    private DiscussionCreateResponse mockResponse;

    @InjectMocks
    private DiscussionController discussionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validRequest = new DiscussionCreateRequest(VALID_PARAGRAPH);
        
        mockResponse = new DiscussionCreateResponse(
            TEST_DISCUSSION_ID,
            VALID_PARAGRAPH,
            TEST_MAINTOPIC_ID
        );
    }

    @Test
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
    @DisplayName("空のコンテンツでリクエストした場合にバリデーションエラーが発生することを確認する")
    void createDiscussionWithEmptyContentTest() throws Exception {
        
        DiscussionCreateRequest invalidRequest = new DiscussionCreateRequest(EMPTY_PARAGRAPH);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("nullコンテンツでリクエストした場合にバリデーションエラーが発生することを確認する")
    void createDiscussionWithNullContentTest() throws Exception {
        
        DiscussionCreateRequest invalidRequest = new DiscussionCreateRequest(null);

        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
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
    @DisplayName("無効なパスパラメータでリクエストした場合の動作を確認する")
    void createDiscussionWithInvalidPathParameterTest() throws Exception {
        
        mockMvc.perform(post("/v1/maintopics/invalid/discussions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("JSONフォーマットが不正な場合の動作を確認する")
    void createDiscussionWithInvalidJsonTest() throws Exception {
        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Content-Typeヘッダーが指定されていない場合の動作を確認する")
    void createDiscussionWithoutContentTypeTest() throws Exception {
        
        mockMvc.perform(post("/v1/maintopics/{maintopicId}/discussions", TEST_MAINTOPIC_ID)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
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
}
       