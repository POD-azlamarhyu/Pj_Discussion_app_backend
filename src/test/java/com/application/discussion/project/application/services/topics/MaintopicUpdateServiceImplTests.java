package com.application.discussion.project.application.services.topics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicUpdateResponse;
import com.application.discussion.project.domain.entities.topics.Maintopic;

import com.application.discussion.project.domain.valueobjects.topics.Description;
import com.application.discussion.project.domain.valueobjects.topics.Title;
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
    private Maintopic originalMaintopic;
    private Maintopic updatedMaintopic;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private final String UPDATE_TITLE = "更新されたタイトル";
    private final String UPDATE_DESCRIPTION = "リクエストで更新された説明";

    @BeforeEach
    void setUp() {
        maintopicId = 1L;
        createdDateTime = LocalDateTime.of(2026, 1, 3, 10, 0, 0);
        updatedDateTime = createdDateTime.plusMinutes(30);
        
        updateRequest = new MaintopicUpdateRequest();
        updateRequest.setTitle(UPDATE_TITLE);
        updateRequest.setDescription(UPDATE_DESCRIPTION);

        maintopicEntity = new Maintopics();
        maintopicEntity.setId(maintopicId);
        maintopicEntity.setTitle("元のタイトル");
        maintopicEntity.setDescription("元の説明");

        originalMaintopic = mock(Maintopic.class);
        updatedMaintopic = mock(Maintopic.class);
        
        when(updatedMaintopic.getMaintopicId()).thenReturn(maintopicId);
        when(updatedMaintopic.getTitle()).thenReturn(UPDATE_TITLE);
        when(updatedMaintopic.getDescription()).thenReturn(UPDATE_DESCRIPTION);
        when(updatedMaintopic.getCreatedAt()).thenReturn(createdDateTime);
        when(updatedMaintopic.getUpdatedAt()).thenReturn(updatedDateTime);
    }

    @Test
    void testUpdatedMaintopicIsSuccessful() {
        Maintopic updateMaintopic = mock(Maintopic.class);
        when(updateMaintopic.getTitle()).thenReturn(UPDATE_TITLE);
        when(updateMaintopic.getDescription()).thenReturn(UPDATE_DESCRIPTION);

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(originalMaintopic);
        when(originalMaintopic.update(any(Title.class), any(Description.class))).thenReturn(updateMaintopic);
        when(maintopicRepository.updateMaintopic(maintopicEntity)).thenReturn(updatedMaintopic);

        MaintopicUpdateResponse response = maintopicUpdateService.service(maintopicId, updateRequest);

        assertNotNull(response);
        assertEquals(maintopicId, response.getId());
        assertEquals("更新されたタイトル", response.getTitle());
        assertEquals("更新された説明", response.getDescription());
        assertEquals(createdDateTime.toString(), response.getCreatedAt());
        assertEquals(createdDateTime.plusMinutes(30).toString(), response.getUpdatedAt());

        // メソッドの呼び出し回数を検証
        verify(maintopicRepository, times(1)).findModelById(maintopicId);
        verify(maintopicRepository, times(1)).findMaintopicById(maintopicId);
        verify(originalMaintopic, times(1)).update(any(Title.class), any(Description.class));
        verify(maintopicRepository, times(1)).updateMaintopic(maintopicEntity);
    }

    @Test
    void testThrowInternalServerErrorExceptionWhenFindModelById() {
        // Given
        when(maintopicRepository.findModelById(maintopicId))
            .thenThrow(new RuntimeException("データベースエラー"));

        // When & Then
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testThrowInternalServerErrorExceptionWhenFindMaintopicByIdThrowsException() {
        // Given
        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId))
            .thenThrow(new RuntimeException("ドメインオブジェクト取得エラー"));

        // When & Then
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testThrowInternalServerErrorExceptionWhenUpdateMaintopicThrowsException() {
        // Given
        Maintopic updateMaintopic = mock(Maintopic.class);
        when(updateMaintopic.getTitle()).thenReturn("更新されたタイトル");
        when(updateMaintopic.getDescription()).thenReturn("更新された説明");

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(originalMaintopic);
        when(originalMaintopic.update(any(Title.class), any(Description.class))).thenReturn(updateMaintopic);
        when(maintopicRepository.updateMaintopic(maintopicEntity))
            .thenThrow(new RuntimeException("データベース更新エラー"));

        // When & Then
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testThrowInternalServerErrorExceptionWhenDomainUpdateThrowsException() {
        // Given
        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(originalMaintopic);
        when(originalMaintopic.update(any(Title.class), any(Description.class)))
            .thenThrow(new IllegalArgumentException("不正な値です"));

        // When & Then
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(maintopicId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testHandleEmptyTitleUpdateWhenTitleIsEmpty() {
        // Given
        updateRequest.setTitle("");
        updateRequest.setDescription("説明のみ更新");

        Maintopic updateMaintopic = mock(Maintopic.class);
        when(updateMaintopic.getTitle()).thenReturn("");
        when(updateMaintopic.getDescription()).thenReturn("説明のみ更新");

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(originalMaintopic);
        when(originalMaintopic.update(any(Title.class), any(Description.class))).thenReturn(updateMaintopic);
        when(maintopicRepository.updateMaintopic(maintopicEntity)).thenReturn(updatedMaintopic);
        when(updatedMaintopic.getTitle()).thenReturn("");
        when(updatedMaintopic.getDescription()).thenReturn("説明のみ更新");

        // When
        MaintopicUpdateResponse response = maintopicUpdateService.service(maintopicId, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals("", response.getTitle());
        assertEquals("説明のみ更新", response.getDescription());
    }

    @Test
    void testThrowInternalServerErrorExceptionWhenIdIsNull() {
        // Given
        Long nullId = null;

        // When & Then
        InternalServerErrorException exception = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicUpdateService.service(nullId, updateRequest)
        );

        assertEquals("アップデートに失敗しました．", exception.getMessage());
        assertEquals("Internal_Server_Error", exception.getType());
    }

    @Test
    void testSetEntityFieldsCorrectlyUpdateMaintopic() {
        // Given
        Maintopic updateMaintopic = mock(Maintopic.class);
        when(updateMaintopic.getTitle()).thenReturn("設定確認タイトル");
        when(updateMaintopic.getDescription()).thenReturn("設定確認説明");

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(maintopicRepository.findMaintopicById(maintopicId)).thenReturn(originalMaintopic);
        when(originalMaintopic.update(any(Title.class), any(Description.class))).thenReturn(updateMaintopic);
        when(maintopicRepository.updateMaintopic(maintopicEntity)).thenReturn(updatedMaintopic);

        maintopicUpdateService.service(maintopicId, updateRequest);

        assertEquals("設定確認タイトル", maintopicEntity.getTitle());
        assertEquals("設定確認説明", maintopicEntity.getDescription());
    }
}
