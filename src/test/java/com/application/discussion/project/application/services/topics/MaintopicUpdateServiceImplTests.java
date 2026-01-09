package com.application.discussion.project.application.services.topics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.exceptions.BadRequestException;

import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.repositories.topics.MaintopicRepositoryImpl;

@ExtendWith(MockitoExtension.class)
public class MaintopicUpdateServiceImplTests {

    @Mock
    private MaintopicRepositoryImpl maintopicRepository;

    @InjectMocks
    private MaintopicUpdateServiceImpl maintopicUpdateService;

    private Long maintopicId;
    private MaintopicUpdateRequest updateRequest;
    private Maintopics maintopicEntity;
    private Maintopic existingMaintopicMock;
    private Maintopic updatedMaintopicMock;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private Title titleMock;
    private Description descriptionMock;
    private final String EXISTING_TITLE = "元のタイトル";
    private final String EXISTING_DESCRIPTION = "元の説明";
    private final String UPDATE_TITLE = "更新されたタイトル";
    private final String UPDATE_DESCRIPTION = "リクエストで更新された説明";
    private final String InternalErrorMessage = "アップデートに失敗しました．";
    private final String InternalErrorType = "Internal_Server_Error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        maintopicId = 6L;
        createdDateTime = LocalDateTime.of(2026, 1, 3, 10, 0, 0);
        updatedDateTime = createdDateTime.plusMinutes(60);
        
        updateRequest = new MaintopicUpdateRequest(
            UPDATE_TITLE,
            UPDATE_DESCRIPTION
        );

        maintopicEntity = new Maintopics(
            maintopicId,
            EXISTING_TITLE,
            EXISTING_DESCRIPTION,
            null,
            createdDateTime,
            null,
            false,
            false
        );

        existingMaintopicMock = mock(Maintopic.class);
        updatedMaintopicMock = mock(Maintopic.class);
        titleMock = mock(Title.class);
        descriptionMock = mock(Description.class);
    }

    @Test
    void testupdatedMaintopicMockIsSuccessful() {
        Maintopic updateMaintopic = Maintopic.of(
            maintopicId,
            UPDATE_TITLE,
            UPDATE_DESCRIPTION,
            UUID.randomUUID(),
            createdDateTime,
            updatedDateTime,
            false,
            false
        );
        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(existingMaintopicMock);
        
        when(existingMaintopicMock.update(
            any(Title.class), 
            any(Description.class)
            )
        ).thenReturn(updatedMaintopicMock);

        when(maintopicRepository.updateMaintopic(maintopicEntity)).thenReturn(updateMaintopic);

        MaintopicUpdateResponse response = maintopicUpdateService.service(maintopicId, updateRequest);

        assertNotNull(response);
        assertEquals(maintopicId, response.getId());
        assertEquals(UPDATE_TITLE, response.getTitle());
        assertEquals(UPDATE_DESCRIPTION, response.getDescription());
        assertEquals(createdDateTime.toString(), response.getCreatedAt());
        assertEquals(updatedDateTime.toString(), response.getUpdatedAt());

        verify(maintopicRepository, times(1)).findModelById(maintopicId);
        verify(maintopicRepository, times(1)).findMaintopicById(maintopicId);
        verify(existingMaintopicMock, times(1)).update(any(Title.class), any(Description.class));
        verify(maintopicRepository, times(1)).updateMaintopic(maintopicEntity);
    }

    @Test
    void testThrowInternalServerErrorFindModelById() {
        
        when(maintopicRepository.findModelById(maintopicId))
            .thenThrow(new ResourceNotFoundException("メイントピックは存在しません", "Not_Found"));
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals(InternalErrorMessage, exception.getMessage());
        assertEquals(InternalErrorType, exception.getType());
    }

    @Test
    void testThrowInternalServerErrorFindMaintopic() {

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId))
            .thenThrow(new ResourceNotFoundException("ドメインオブジェクト取得エラー","Not_Found"));

        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testThrowInternalServerErrorUpdateMaintopic() {
        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(existingMaintopicMock);
        when(existingMaintopicMock.update(
            any(Title.class), 
            any(Description.class)
        )).thenReturn(updatedMaintopicMock);
        when(maintopicRepository.updateMaintopic(maintopicEntity))
            .thenThrow(new RuntimeException("データベース更新エラー"));

        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testThrowInternalServerErrorDomainUpdate() {
        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(existingMaintopicMock);
        when(existingMaintopicMock.update(
            any(Title.class), 
            any(Description.class)
        )).thenThrow(new BadRequestException("既存の説明と同じ内容です", "Bad_Request"));

        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
        verify(maintopicRepository, times(1)).findModelById(maintopicId);
        verify(maintopicRepository, times(1)).findMaintopicById(maintopicId);
        verify(existingMaintopicMock, times(0)).update(titleMock, descriptionMock);
    }
    // TODO:このテストは現時点で実装されている機能の実態に即していないテストのため，コメントアウトした．サービスは両方とも正常な文字列が入っていなければ500エラーとなる． 
    // @Test
    // void testEmptyTitleUpdateTitleIsEmpty() {
    //     updateRequest.setTitle("");
    //     updateRequest.setDescription(UPDATE_DESCRIPTION);

    //     Maintopic updateMaintopic = mock(Maintopic.class);
    //     when(updateMaintopic.getTitle()).thenReturn("");
    //     when(updateMaintopic.getDescription()).thenReturn("説明のみ更新");

    //     when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
    //     when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(existingMaintopicMock);
    //     when(existingMaintopicMock.update(any(Title.class), any(Description.class))).thenReturn(updateMaintopic);
    //     when(maintopicRepository.updateMaintopic(maintopicEntity)).thenReturn(updatedMaintopicMock);

    //     MaintopicUpdateResponse response = maintopicUpdateService.service(maintopicId, updateRequest);

    //     assertNotNull(response);
    //     assertEquals(EXISTING_TITLE, response.getTitle());
    //     assertEquals(UPDATE_DESCRIPTION, response.getDescription());
    // }

    @Test
    void testThrowInternalServerErrorExceptionWhenIdIsNull() {
        Long nullId = null;
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(nullId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testEntityFieldsCorrectlyUpdateMaintopic() {
        Maintopic updateMaintopic = Maintopic.of(
            maintopicId,
            UPDATE_TITLE+"設定確認タイトル",
            UPDATE_DESCRIPTION+"設定確認説明",
            UUID.randomUUID(),
            createdDateTime,
            updatedDateTime,
            false,
            false
        );

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(existingMaintopicMock);
        when(existingMaintopicMock.update(any(Title.class), any(Description.class))).thenReturn(updatedMaintopicMock);
        when(maintopicRepository.updateMaintopic(maintopicEntity)).thenReturn(updateMaintopic);

        MaintopicUpdateResponse maintopicUpdateResponse =  maintopicUpdateService.service(maintopicId, updateRequest);

        assertEquals(UPDATE_TITLE+"設定確認タイトル", maintopicUpdateResponse.getTitle());
        assertEquals(UPDATE_DESCRIPTION+"設定確認説明", maintopicUpdateResponse.getDescription());
        assertEquals(createdDateTime.toString(), maintopicUpdateResponse.getCreatedAt());
        assertEquals(updatedDateTime.toString(), maintopicUpdateResponse.getUpdatedAt());
    }
}
