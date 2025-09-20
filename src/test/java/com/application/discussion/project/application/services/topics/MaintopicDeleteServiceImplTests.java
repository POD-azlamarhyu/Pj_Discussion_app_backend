package com.application.discussion.project.application.services.topics;

import java.time.LocalDateTime;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.topics.MaintopicDeleteResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.application.discussion.project.infrastructure.repositories.topics.MaintopicRepositoryImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("メイントピック削除サービス実装クラスのテスト")
public class MaintopicDeleteServiceImplTests {

    private static final Long VALID_TOPIC_ID = 1L;
    private static final Long DOES_NOT_EXIST_TOPIC_ID = 1000L;
    private static final Long INVALID_TOPIC_ID = -1L;
    private static final Long LARGE_TOPIC_ID = Long.MAX_VALUE;
    private static final String EXISTING_TOPIC_TITLE = "削除されるトピック";
    private static final String EXISTING_TOPIC_DESCRIPTION = "このトピックはテストのために存在します";
    private static final String NON_EXISTENT_TOPIC_MESSAGE = "メイントピックは存在しません";
    private static final String CANNOT_DELETE_MESSAGE = "このメイントピックは削除できません";
    private static final String SUCCESS_DELETE_MESSAGE = "メイントピックが正常に削除されました";
    private static final String DELETE_RESPONSE_MESSAGE = "このリソースは存在しません";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now().minusDays(10);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();
    private static final Boolean IS_DELETED = false;
    private static final Boolean IS_CLOSED = false;

    @Mock
    private MaintopicRepositoryImpl maintopicRepository;

    @InjectMocks
    private MaintopicDeleteServiceImpl maintopicDeleteService;

    private Maintopic existingMaintopic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingMaintopic = Maintopic.of(
            VALID_TOPIC_ID, 
            EXISTING_TOPIC_TITLE, 
            EXISTING_TOPIC_DESCRIPTION, 
            CREATED_AT, 
            UPDATED_AT, 
            IS_DELETED, 
            IS_CLOSED
        );
    }

    @Test
    @DisplayName("有効なIDでメイントピックを正常に削除する")
    void testDeleteMaintopicSuccess() {
        when(maintopicRepository.existsMaintopic(VALID_TOPIC_ID)).thenReturn(true);
        doNothing().when(maintopicRepository).deleteMaintopic(VALID_TOPIC_ID);

        MaintopicDeleteResponse response = maintopicDeleteService.service(VALID_TOPIC_ID);

        assertNotNull(response);
        assertEquals(DELETE_RESPONSE_MESSAGE, response.getMessage());
    }

    @Test
    @DisplayName("削除可能なメイントピックの削除処理が完了する")
    void testDeleteMaintopicDeletableTopic() {
        when(maintopicRepository.existsMaintopic(VALID_TOPIC_ID)).thenReturn(true);
        doNothing().when(maintopicRepository).deleteMaintopic(VALID_TOPIC_ID);
        

        MaintopicDeleteResponse response = maintopicDeleteService.service(VALID_TOPIC_ID);

        assertNotNull(response);
        verify(maintopicRepository, times(1)).existsMaintopic(VALID_TOPIC_ID);
        verify(maintopicRepository, times(1)).deleteMaintopic(VALID_TOPIC_ID);
    }

    @Test
    @DisplayName("存在しないIDのメイントピック削除でエラーレスポンスを返す")
    void testDeleteMaintopicNotFound() {
        when(maintopicRepository.existsMaintopic(DOES_NOT_EXIST_TOPIC_ID)).thenReturn(false);

        ApplicationLayerException response =  assertThrows(ApplicationLayerException.class, () -> {
            maintopicDeleteService.service(DOES_NOT_EXIST_TOPIC_ID);
        });

        assertNotNull(response);
        assertEquals(NON_EXISTENT_TOPIC_MESSAGE, response.getMessage());
        verify(maintopicRepository, never()).deleteMaintopic(DOES_NOT_EXIST_TOPIC_ID);
    }

    @Test
    @DisplayName("削除不可能なメイントピックでエラーレスポンスを返す")
    void testDeleteMaintopicCannotDelete() {
        when(maintopicRepository.existsMaintopic(INVALID_TOPIC_ID)).thenReturn(false);

        ApplicationLayerException response =  assertThrows(ApplicationLayerException.class, () -> {
            maintopicDeleteService.service(INVALID_TOPIC_ID);
        });

        assertNotNull(response);
        assertEquals(NON_EXISTENT_TOPIC_MESSAGE, response.getMessage());
        verify(maintopicRepository, never()).deleteMaintopic(INVALID_TOPIC_ID);
    }

    @Test
    @DisplayName("nullIDで例外が発生する")
    void testDeleteMaintopicNullId() {
        assertThrows(ApplicationLayerException.class, () -> {
            maintopicDeleteService.service(null);
        });
    }

    @Test
    @DisplayName("無効なID（負の値）で例外が発生する")
    void testDeleteMaintopicNegativeId() {
        assertThrows(ApplicationLayerException.class, () -> {
            maintopicDeleteService.service(INVALID_TOPIC_ID);
        });
    }

    @Test
    @DisplayName("ゼロIDで例外が発生する")
    void testDeleteMaintopicZeroId() {
        assertThrows(ApplicationLayerException.class, () -> {
            maintopicDeleteService.service(0L);
        });
    }


    @Test
    @DisplayName("削除可能な場合のみリポジトリのdeleteが呼び出される")
    void testRepositoryDeleteCalledOnlyWhenDeletable() {
        when(maintopicRepository.existsMaintopic(VALID_TOPIC_ID)).thenReturn(true);

        maintopicDeleteService.service(VALID_TOPIC_ID);

        verify(maintopicRepository, times(1)).deleteMaintopic(VALID_TOPIC_ID);
    }

    @Test
    @DisplayName("大きなID値での削除処理")
    void testDeleteMaintopicLargeId() {
        
        when(maintopicRepository.existsMaintopic(LARGE_TOPIC_ID)).thenReturn(true);
        doNothing().when(maintopicRepository).deleteMaintopic(LARGE_TOPIC_ID);
        MaintopicDeleteResponse response = maintopicDeleteService.service(LARGE_TOPIC_ID);

        assertNotNull(response);
        assertEquals(DELETE_RESPONSE_MESSAGE, response.getMessage());
    }

    @Test
    @DisplayName("リポジトリで例外が発生した場合の処理")
    void testDeleteMaintopicRepositoryException() {
        when(maintopicRepository.existsMaintopic(VALID_TOPIC_ID)).thenThrow(new RuntimeException("データベースエラー"));

        assertThrows(RuntimeException.class, () -> {
            maintopicDeleteService.service(VALID_TOPIC_ID);
        });
    }
}
