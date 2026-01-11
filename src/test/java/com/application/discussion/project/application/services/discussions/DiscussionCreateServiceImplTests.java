package com.application.discussion.project.application.services.discussions;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import com.application.discussion.project.domain.services.topics.MaintopicDiscussionDuplicateDomainService;
import com.application.discussion.project.domain.services.users.UserAuthenticationDomainService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiscussionCreateServiceImpl クラスのテスト")
public class DiscussionCreateServiceImplTests {

    @Mock
    private DiscussionRepository discussionRepository;

    @Mock
    private UserAuthenticationDomainService userAuthenticationDomainService;

    @Mock
    private MaintopicDiscussionDuplicateDomainService maintopicDiscussionDuplicateDomainService;

    @InjectMocks
    private DiscussionCreateServiceImpl discussionCreateService;

    private static final Long MAINTOPIC_ID = 8L;
    private static final Long DISCUSSION_ID = 10L;
    private static final UUID VALID_USER_ID = UUID.randomUUID();
    private static final String DEFAULT_PARAGRAPH = "Javaの新機能について議論しましょう。ラムダ式の活用方法を中心に話し合いたいです。";
    private static final String ANOTHER_PARAGRAPH = "Spring Bootのベストプラクティスについて意見を交換しましょう。";
    private static final Integer STRING_REPEAT_COUNT = 10;
    private static final Integer MOCK_WANTED_NUMBER = 1;
    private static final LocalDateTime DISCUSSION_CREATED_AT = LocalDateTime.of(2023, 10, 1, 12, 0);
    private static final LocalDateTime DISCUSSION_UPDATED_AT = LocalDateTime.of(2023, 10, 1, 12, 0);

    private DiscussionCreateRequest discussionCreateRequest;
    private Discussion createdDiscussion;
    private User mockUser;

    @BeforeEach
    void setUp() {
        discussionCreateRequest = new DiscussionCreateRequest(DEFAULT_PARAGRAPH);
        
        mockUser = User.of(
            VALID_USER_ID,
            "testuser",
            "test@example.com",
            "hashedPassword1234",
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT
        );
        
        createdDiscussion = Discussion.of(
            DISCUSSION_ID,
            DEFAULT_PARAGRAPH,
            MAINTOPIC_ID,
            VALID_USER_ID,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );
    }

    @Test
    @DisplayName("正常系：ディスカッション作成が成功すること")
    void serviceSuccessTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class))).thenReturn(createdDiscussion);

        final DiscussionCreateResponse response = discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);

        assertNotNull(response);
        assertEquals(DISCUSSION_ID, response.getDiscussionId());
        assertEquals(DEFAULT_PARAGRAPH, response.getParagraph());
        assertEquals(MAINTOPIC_ID, response.getMaintopicId());

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, times(MOCK_WANTED_NUMBER)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("異常系：メイントピックが存在しない場合に例外が発生すること")
    void serviceWithNonExistentMaintopicTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(false);

        assertThrows(ApplicationLayerException.class, () -> {
            discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);
        });

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, never()).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("異常系：ディスカッションリポジトリで例外が発生した場合")
    void serviceWithRepositoryExceptionTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class)))
            .thenThrow(new RuntimeException("データベース接続エラー"));

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);
        });

        assertEquals("データベース接続エラー", exception.getMessage());
        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, times(MOCK_WANTED_NUMBER)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("正常系：異なるメイントピックIDでディスカッション作成が成功すること")
    void serviceWithDifferentMaintopicIdTest() {
        final Long differentMaintopicId = 999L;
        
        final Discussion differentCreatedDiscussion = Discussion.of(
            DISCUSSION_ID,
            ANOTHER_PARAGRAPH,
            differentMaintopicId,
            VALID_USER_ID,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );

        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(differentMaintopicId)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class))).thenReturn(differentCreatedDiscussion);

        final DiscussionCreateResponse response = discussionCreateService.service(differentMaintopicId, discussionCreateRequest);

        assertNotNull(response);
        assertEquals(DISCUSSION_ID, response.getDiscussionId());
        assertEquals(ANOTHER_PARAGRAPH, response.getParagraph());
        assertEquals(differentMaintopicId, response.getMaintopicId());

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(differentMaintopicId);
        verify(discussionRepository, times(MOCK_WANTED_NUMBER)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("正常系：長い本文でディスカッション作成が成功すること")
    void serviceWithLongParagraphTest() {
        final String longParagraph = "これは非常に長いディスカッションの内容です。Spring Bootフレームワークの詳細な機能について詳しく説明し、実際の開発現場での活用方法やベストプラクティスについて議論したいと思います。特にDependency InjectionやAOP機能について深く掘り下げて話し合いましょう。".repeat(STRING_REPEAT_COUNT);
        final DiscussionCreateRequest longRequest = new DiscussionCreateRequest(longParagraph);
        
        final Discussion longCreatedDiscussion = Discussion.of(
            DISCUSSION_ID,
            longParagraph,
            MAINTOPIC_ID,
            VALID_USER_ID,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );

        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class))).thenReturn(longCreatedDiscussion);

        final DiscussionCreateResponse response = discussionCreateService.service(MAINTOPIC_ID, longRequest);

        assertNotNull(response);
        assertEquals(DISCUSSION_ID, response.getDiscussionId());
        assertEquals(longParagraph, response.getParagraph());
        assertEquals(MAINTOPIC_ID, response.getMaintopicId());

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, times(MOCK_WANTED_NUMBER)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("正常系：Discussionエンティティが正しく設定されてリポジトリに渡されること")
    void serviceWithCorrectEntitySetupTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class))).thenReturn(createdDiscussion);

        discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);

        verify(discussionRepository).createDiscussion(argThat(discussion -> {
            return discussion.getParagraph().equals(discussionCreateRequest.getParagraph()) &&
                    discussion.getMaintopicId().equals(MAINTOPIC_ID) &&
                    discussion.getUserId().equals(VALID_USER_ID);
        }));
    }

    @Test
    @DisplayName("異常系：認証ユーザーが取得できない場合に例外が発生すること")
    void serviceWithAuthenticationFailureTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser())
            .thenThrow(new DomainLayerErrorException("認証されたユーザーが見つかりません", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401)));

        assertThrows(DomainLayerErrorException.class, () -> {
            discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);
        });

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, never()).isDuplicateDiscussionExists(any());
        verify(discussionRepository, never()).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("正常系：空白文字を含む本文でディスカッション作成が成功すること")
    void serviceWithWhitespaceInParagraphTest() {
        final String paragraphWithWhitespace = "これは空白を含む文章です。\n改行も含まれています。\tタブ文字も含まれています。";
        final DiscussionCreateRequest whitespaceRequest = new DiscussionCreateRequest(paragraphWithWhitespace);
        
        final Discussion whitespaceCreatedDiscussion = Discussion.of(
            DISCUSSION_ID,
            paragraphWithWhitespace,
            MAINTOPIC_ID,
            VALID_USER_ID,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );

        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class))).thenReturn(whitespaceCreatedDiscussion);

        final DiscussionCreateResponse response = discussionCreateService.service(MAINTOPIC_ID, whitespaceRequest);

        assertNotNull(response);
        assertEquals(DISCUSSION_ID, response.getDiscussionId());
        assertEquals(paragraphWithWhitespace, response.getParagraph());
        assertEquals(MAINTOPIC_ID, response.getMaintopicId());

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, times(MOCK_WANTED_NUMBER)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("正常系：特殊文字を含む本文でディスカッション作成が成功すること")
    void serviceWithSpecialCharactersTest() {
        final String specialCharactersParagraph = "特殊文字のテスト: @#$%^&*()_+-=[]{}|;':\"<>?,./`~！＠＃＄％＾＆＊（）";
        final DiscussionCreateRequest specialRequest = new DiscussionCreateRequest(specialCharactersParagraph);
        
        final Discussion specialCreatedDiscussion = Discussion.of(
            DISCUSSION_ID,
            specialCharactersParagraph,
            MAINTOPIC_ID,
            VALID_USER_ID,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );

        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(discussionRepository.createDiscussion(any(Discussion.class))).thenReturn(specialCreatedDiscussion);

        final DiscussionCreateResponse response = discussionCreateService.service(MAINTOPIC_ID, specialRequest);

        assertNotNull(response);
        assertEquals(DISCUSSION_ID, response.getDiscussionId());
        assertEquals(specialCharactersParagraph, response.getParagraph());
        assertEquals(MAINTOPIC_ID, response.getMaintopicId());

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, times(MOCK_WANTED_NUMBER)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("正常系：複数のユーザーで連続してディスカッション作成が成功すること")
    void serviceWithMultipleUsersTest() {
        final UUID secondUserId = UUID.randomUUID();
        final User secondUser = User.of(
            secondUserId,
            "seconduser",
            "second@example.com",
            "hashedPassword2468",
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT
        );
        
        final Long secondDiscussionId = 20L;
        final Discussion firstDiscussion = Discussion.of(
            DISCUSSION_ID,
            DEFAULT_PARAGRAPH,
            MAINTOPIC_ID,
            VALID_USER_ID,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );
        
        final Discussion secondDiscussion = Discussion.of(
            secondDiscussionId,
            DEFAULT_PARAGRAPH,
            MAINTOPIC_ID,
            secondUserId,
            DISCUSSION_CREATED_AT,
            DISCUSSION_UPDATED_AT,
            null
        );

        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);
        when(userAuthenticationDomainService.getAuthenticatedUser())
            .thenReturn(mockUser)
            .thenReturn(secondUser);
        when(discussionRepository.createDiscussion(any(Discussion.class)))
            .thenReturn(firstDiscussion)
            .thenReturn(secondDiscussion);

        final DiscussionCreateResponse firstResponse = discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);
        final DiscussionCreateResponse secondResponse = discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);

        assertNotNull(firstResponse);
        assertEquals(DISCUSSION_ID, firstResponse.getDiscussionId());
        assertEquals(VALID_USER_ID, firstDiscussion.getUserId());

        assertNotNull(secondResponse);
        assertEquals(secondDiscussionId, secondResponse.getDiscussionId());
        assertEquals(secondUserId, secondDiscussion.getUserId());

        verify(userAuthenticationDomainService, times(2)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(2)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, times(2)).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("異常系：本文がnullの場合に例外が発生すること")
    void serviceWithNullParagraphTest() {
        final DiscussionCreateRequest nullParagraphRequest = new DiscussionCreateRequest(null);

        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);

        assertThrows(DomainLayerErrorException.class, () -> {
            discussionCreateService.service(MAINTOPIC_ID, nullParagraphRequest);
        });

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, never()).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("異常系：本文が空文字列の場合に例外が発生すること")
    void serviceWithEmptyParagraphTest() {
        final DiscussionCreateRequest emptyParagraphRequest = new DiscussionCreateRequest("");

        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID)).thenReturn(true);

        assertThrows(DomainLayerErrorException.class, () -> {
            discussionCreateService.service(MAINTOPIC_ID, emptyParagraphRequest);
        });

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, never()).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("異常系：メイントピックIDがnullの場合に例外が発生すること")
    void serviceWithNullMaintopicIdTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(null))
            .thenReturn(false);

        assertThrows(ApplicationLayerException.class, () -> {
            discussionCreateService.service(null, discussionCreateRequest);
        });

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(any());
        verify(discussionRepository, never()).createDiscussion(any(Discussion.class));
    }

    @Test
    @DisplayName("異常系：ドメインサービスでメイントピック存在確認時に例外が発生した場合")
    void serviceWithDomainServiceExceptionTest() {
        when(userAuthenticationDomainService.getAuthenticatedUser()).thenReturn(mockUser);
        when(maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID))
            .thenThrow(new DomainLayerErrorException(
                "メイントピック存在確認中にエラーが発生しました",
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatusCode.valueOf(500)
            ));

        assertThrows(DomainLayerErrorException.class, () -> {
            discussionCreateService.service(MAINTOPIC_ID, discussionCreateRequest);
        });

        verify(userAuthenticationDomainService, times(MOCK_WANTED_NUMBER)).getAuthenticatedUser();
        verify(maintopicDiscussionDuplicateDomainService, times(MOCK_WANTED_NUMBER)).isDuplicateDiscussionExists(MAINTOPIC_ID);
        verify(discussionRepository, never()).createDiscussion(any(Discussion.class));
    }
}
