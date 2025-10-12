package com.application.discussion.project.application.services.discussions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.application.dtos.discussions.DiscussionCreateRequest;
import com.application.discussion.project.application.dtos.discussions.DiscussionCreateResponse;
import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import com.application.discussion.project.domain.repositories.MaintopicRepository;
import com.application.discussion.project.infrastructure.models.discussions.Discussions;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.exceptions.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.application.discussion.project.domain.valueobjects.discussions.Paragraph;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiscussionCreateServiceImpl クラスのテスト")
public class DiscussionCreateServiceImplTests {

    @Mock
    private DiscussionRepository discussionRepository;

    @Mock
    private MaintopicRepository maintopicRepository;

    @InjectMocks
    private DiscussionCreateServiceImpl discussionCreateService;

    private Long maintopicId;
    private DiscussionCreateRequest discussionCreateRequest;
    private Maintopics maintopicEntity;
    private Discussion createdDiscussion;
    private Paragraph testParagraph;

    private final String defaultParagraph = "Javaの新機能について議論しましょう。ラムダ式の活用方法を中心に話し合いたいです。";

    @BeforeEach
    void setUp() {
        maintopicId = 1L;
        discussionCreateRequest = new DiscussionCreateRequest(
            defaultParagraph
        );
        
        maintopicEntity = new Maintopics();
        maintopicEntity.setId(maintopicId);
        testParagraph = Paragraph.of(discussionCreateRequest.getParagraph());
        createdDiscussion = Discussion.create(
            testParagraph,
            maintopicId
        );
    }

    @Test
    @DisplayName("正常系：ディスカッション作成が成功すること")
    void serviceSuccessTest() {
        
        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(discussionRepository.createDiscussion(any(Discussions.class))).thenReturn(createdDiscussion);

        DiscussionCreateResponse response = discussionCreateService.service(maintopicId, discussionCreateRequest);

        assertNotNull(response);
        assertEquals(100L, response.getDiscussionId());
        assertEquals(defaultParagraph, response.getParagraph());
        assertEquals(maintopicId, response.getMaintopicId());

        verify(maintopicRepository, times(1)).findModelById(maintopicId);
        verify(discussionRepository, times(1)).createDiscussion(any(Discussions.class));
    }

    @Test
    @DisplayName("異常系：メイントピックが存在しない場合に例外が発生すること")
    void serviceWithNonExistentMaintopicTest() {

        when(maintopicRepository.findModelById(any(Long.class))).thenThrow(
            new ResourceNotFoundException("メイントピックは存在しません", "Not_Found")
        );

        assertThrows(ResourceNotFoundException.class, () -> {
            discussionCreateService.service(maintopicId, discussionCreateRequest);
        });

        verify(maintopicRepository, never()).findModelById(maintopicId);
        verify(discussionRepository, never()).createDiscussion(any(Discussions.class));
    }

    @Test
    @DisplayName("異常系：ディスカッションリポジトリで例外が発生した場合")
    void serviceWithRepositoryExceptionTest() {

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(discussionRepository.createDiscussion(any(Discussions.class)))
            .thenThrow(new RuntimeException("データベース接続エラー"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            discussionCreateService.service(maintopicId, discussionCreateRequest);
        });

        assertEquals("データベース接続エラー", exception.getMessage());
        verify(maintopicRepository, times(1)).findModelById(maintopicId);
        verify(discussionRepository, times(1)).createDiscussion(any(Discussions.class));
    }

    @Test
    @DisplayName("正常系：異なるメイントピックIDでディスカッション作成が成功すること")
    void serviceWithDifferentMaintopicIdTest() {

        Long differentMaintopicId = 999L;
        Maintopics differentMaintopicEntity = new Maintopics();
        differentMaintopicEntity.setId(differentMaintopicId);
        
        Discussion differentCreatedDiscussion = Discussion.create(
            testParagraph,
            differentMaintopicId
        );

        when(maintopicRepository.findModelById(differentMaintopicId)).thenReturn(differentMaintopicEntity);
        when(discussionRepository.createDiscussion(any(Discussions.class))).thenReturn(differentCreatedDiscussion);

        DiscussionCreateResponse response = discussionCreateService.service(differentMaintopicId, discussionCreateRequest);

        assertNotNull(response);
        assertEquals(200L, response.getDiscussionId());
        assertEquals(defaultParagraph, response.getParagraph());
        assertEquals(differentMaintopicId, response.getMaintopicId());

        verify(maintopicRepository, times(1)).findModelById(differentMaintopicId);
        verify(discussionRepository, times(1)).createDiscussion(any(Discussions.class));
    }

    @Test
    @DisplayName("正常系：長い本文でディスカッション作成が成功すること")
    void serviceWithLongParagraphTest() {
        
        final String longParagraph = "これは非常に長いディスカッションの内容です。Spring Bootフレームワークの詳細な機能について詳しく説明し、" +
                              "実際の開発現場での活用方法やベストプラクティスについて議論したいと思います。" +
                              "特にDependency InjectionやAOP機能について深く掘り下げて話し合いましょう。";
        DiscussionCreateRequest longRequest = new DiscussionCreateRequest(longParagraph);
        
        Discussion longCreatedDiscussion = Discussion.create(
            Paragraph.of(longParagraph), 
            maintopicId
        );

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(discussionRepository.createDiscussion(any(Discussions.class))).thenReturn(longCreatedDiscussion);

        DiscussionCreateResponse response = discussionCreateService.service(maintopicId, longRequest);

        assertNotNull(response);
        assertEquals(300L, response.getDiscussionId());
        assertEquals(longParagraph, response.getParagraph());
        assertEquals(maintopicId, response.getMaintopicId());

        verify(maintopicRepository, times(1)).findModelById(maintopicId);
        verify(discussionRepository, times(1)).createDiscussion(any(Discussions.class));
    }

    @Test
    @DisplayName("正常系：Discussionsエンティティが正しく設定されてリポジトリに渡されること")
    void serviceWithCorrectEntitySetupTest() {

        when(maintopicRepository.findModelById(maintopicId)).thenReturn(maintopicEntity);
        when(discussionRepository.createDiscussion(any(Discussions.class))).thenReturn(createdDiscussion);

        discussionCreateService.service(maintopicId, discussionCreateRequest);

        verify(discussionRepository).createDiscussion(argThat(discussions -> {
            return discussions.getParagraph().equals(discussionCreateRequest.getParagraph()) &&
                   discussions.getMaintopic().equals(maintopicEntity);
        }));
    }
}
