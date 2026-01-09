package com.application.discussion.project.application.services.topics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.application.dtos.topics.MaintopicCreateRequest;
import com.application.discussion.project.application.dtos.topics.MaintopicCreateResponse;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.domain.entities.topics.Maintopic;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.services.users.UserAuthenticationDomainService;
import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.exceptions.InternalServerErrorException;
import com.application.discussion.project.domain.exceptions.BadRequestException;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;


@ExtendWith(MockitoExtension.class)
@DisplayName("メイントピック作成サービスのテスト")
public class MaintopicCreateServiceImplTest {

    @Mock
    private MaintopicRepository maintopicRepository;

    @Mock
    private UserAuthenticationDomainService userAuthenticationDomainService;

    @InjectMocks
    private MaintopicCreateServiceImpl maintopicCreateServiceImpl;

    private static final String TEST_TITLE = "FugeHoge";
    private static final String TEST_DESCRIPTION = "FugeHoge Description";
    private static final Long TEST_USER_ID = 1L;
    private static final UUID TEST_UUID = UUID.randomUUID();
    
    private Maintopic mockMaintopic;
    private User mockAuthenticatedUser;
    private MaintopicCreateRequest validRequest;
    private LocalDateTime testLocalDateTime;

    @BeforeEach
    void setUp() {
        testLocalDateTime = LocalDateTime.now();
        
        mockAuthenticatedUser = User.of(
            TEST_UUID,
            "testuser",
            "test@example.com",
            "pAssword12345",
            testLocalDateTime,
            testLocalDateTime
        );

        mockMaintopic = Maintopic.of(
            1L,
            TEST_TITLE,
            TEST_DESCRIPTION,
            TEST_UUID,
            testLocalDateTime,
            testLocalDateTime,
            false,
            false
        );

        validRequest = new MaintopicCreateRequest(TEST_TITLE, TEST_DESCRIPTION);
    }

    @Test
    @DisplayName("正常にメイントピックが作成されること")
    void testCreateMaintopicSuccessfully() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockAuthenticatedUser);
        when(maintopicRepository.createMaintopic(any(Maintopic.class), eq(TEST_UUID)))
            .thenReturn(mockMaintopic);

        MaintopicCreateResponse actualResult = maintopicCreateServiceImpl.service(validRequest);

        assertNotNull(actualResult);
        assertEquals(mockMaintopic.getMaintopicId(), actualResult.getId());
        assertEquals(mockMaintopic.getTitle(), actualResult.getTitle());
        assertEquals(mockMaintopic.getDescription(), actualResult.getDescription());
        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        verify(maintopicRepository, times(1)).createMaintopic(any(Maintopic.class), eq(TEST_UUID));
    }

    @Test
    @DisplayName("リクエストユーザが認証されていること")
    void testUserIsAuthenticated() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockAuthenticatedUser);
        when(maintopicRepository.createMaintopic(any(Maintopic.class), eq(TEST_UUID)))
            .thenReturn(mockMaintopic);

        maintopicCreateServiceImpl.service(validRequest);

        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        assertNotNull(mockAuthenticatedUser.getUserId());
    }

    @Test
    @DisplayName("リクエストユーザが認証されていない場合にエラーが発生すること")
    void testThrowsExceptionWhenUserNotAuthenticated() {
        when(userAuthenticationDomainService.getAuthenticatedUser())
            .thenThrow(new DomainLayerErrorException("認証されたユーザーが見つかりません", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401)));

        DomainLayerErrorException actualException = assertThrows(
            DomainLayerErrorException.class,
            () -> maintopicCreateServiceImpl.service(validRequest)
        );

        assertEquals("認証されたユーザーが見つかりません", actualException.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatus());
        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        verify(maintopicRepository, never()).createMaintopic(any(Maintopic.class), eq(TEST_UUID));
    }

    @Test
    @DisplayName("存在しないユーザの場合にエラーが発生すること")
    void testThrowsExceptionWhenUserNotFound() {
        when(userAuthenticationDomainService.getAuthenticatedUser())
            .thenThrow(new DomainLayerErrorException("認証されたユーザーが見つかりません", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401)));

        DomainLayerErrorException actualException = assertThrows(
            DomainLayerErrorException.class,
            () -> maintopicCreateServiceImpl.service(validRequest)
        );

        assertEquals("認証されたユーザーが見つかりません", actualException.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatus());
        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        verify(maintopicRepository, never()).createMaintopic(any(Maintopic.class), eq(TEST_UUID));
    }

    @Test
    @DisplayName("タイトルが不正な値の場合にエラーが発生すること")
    void testThrowsExceptionWhenTitleIsInvalid() {
        MaintopicCreateRequest invalidRequest = new MaintopicCreateRequest("", TEST_DESCRIPTION);
        
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockAuthenticatedUser);

        DomainLayerErrorException actualException = assertThrows(
            DomainLayerErrorException.class,
            () -> maintopicCreateServiceImpl.service(invalidRequest)
        );

        assertTrue(actualException.getMessage().contains("タイトル"));
        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        verify(maintopicRepository, never()).createMaintopic(any(Maintopic.class), any());
    }

    @Test
    @DisplayName("説明が不正な値の場合にエラーが発生すること")
    void testThrowsExceptionWhenDescriptionIsInvalid() {
        MaintopicCreateRequest invalidRequest = new MaintopicCreateRequest(TEST_TITLE, "");
        
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockAuthenticatedUser);

        DomainLayerErrorException actualException = assertThrows(
            DomainLayerErrorException.class,
            () -> maintopicCreateServiceImpl.service(invalidRequest)
        );

        assertTrue(actualException.getMessage().contains("説明"));
        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        verify(maintopicRepository, never()).createMaintopic(any(Maintopic.class), any());
    }

    @Test
    @DisplayName("不明なエラーが発生した場合に500エラーが発生すること")
    void testThrowsInternalServerErrorWhenUnexpectedExceptionOccurs() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockAuthenticatedUser);
        when(maintopicRepository.createMaintopic(any(Maintopic.class), eq(TEST_UUID)))
            .thenThrow(new ApplicationLayerException("登録に失敗しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500)));

        InternalServerErrorException actualException = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicCreateServiceImpl.service(validRequest)
        );

        assertEquals("登録に失敗しました", actualException.getMessage());
        assertEquals("INTERNAL_SERVER_ERROR", actualException.getType());
        verify(userAuthenticationDomainService, times(1)).getAuthenticatedUser();
        verify(maintopicRepository, times(1)).createMaintopic(any(Maintopic.class), eq(TEST_UUID));
    }

    @Test
    @DisplayName("リポジトリ保存時に例外が発生した場合にエラーハンドリングされること")
    void testHandlesRepositoryException() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockAuthenticatedUser);
        when(maintopicRepository.createMaintopic(any(Maintopic.class), eq(TEST_UUID)))
            .thenThrow(new ApplicationLayerException("登録に失敗しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500)));

        InternalServerErrorException actualException = assertThrows(
            InternalServerErrorException.class,
            () -> maintopicCreateServiceImpl.service(validRequest)
        );

        assertEquals("登録に失敗しました", actualException.getMessage());
        assertEquals("INTERNAL_SERVER_ERROR", actualException.getType());
        verify(maintopicRepository, times(1)).createMaintopic(any(Maintopic.class), eq(TEST_UUID));
    }
}
