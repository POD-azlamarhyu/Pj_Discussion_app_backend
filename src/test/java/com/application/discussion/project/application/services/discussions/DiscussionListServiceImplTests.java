package com.application.discussion.project.application.services.discussions;

import com.application.discussion.project.application.dtos.discussions.DiscussionListResponse;
import com.application.discussion.project.application.dtos.discussions.DiscussionResponse;
import com.application.discussion.project.domain.entities.discussions.Discussion;
import com.application.discussion.project.domain.repositories.DiscussionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiscussionListServiceImpl ユニットテスト")
class DiscussionListServiceImplTests {

    @Mock
    private DiscussionRepository discussionRepository;

    @InjectMocks
    private DiscussionListServiceImpl discussionListService;

    private Long maintopicId;
    private Pageable pageable;
    private List<Discussion> discussions;
    private UUID VALID_USER_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        maintopicId = 1L;
        pageable = PageRequest.of(0, 10);

        LocalDateTime now = LocalDateTime.now();
        discussions = List.of(
            createDiscussion(1L, "議論内容1", maintopicId, now),
            createDiscussion(2L, "議論内容2", maintopicId, now),
            createDiscussion(3L, "議論内容3", maintopicId, now)
        );
    }

    @Test
    @DisplayName("正常系: 議論リストを取得できること")
    void testServiceReturnsDiscussionList() {
        Page<Discussion> discussionPage = new PageImpl<>(discussions, pageable, discussions.size());
        when(discussionRepository.findAllDiscussions(eq(maintopicId), any(Pageable.class)))
            .thenReturn(discussionPage);

        DiscussionListResponse response = discussionListService.service(maintopicId, pageable);

        assertThat(response).isNotNull();
        assertThat(response.getDiscussions()).hasSize(3);
        assertThat(response.getTotalCount()).isEqualTo(3);
        assertThat(response.getCurrentPage()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.getTotalPages()).isEqualTo(1);

        verify(discussionRepository, times(1)).findAllDiscussions(eq(maintopicId), any(Pageable.class));
    }

    @Test
    @DisplayName("正常系: 空のリストが返されること")
    void testServiceReturnsEmptyList() {
        Page<Discussion> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(discussionRepository.findAllDiscussions(eq(maintopicId), any(Pageable.class)))
            .thenReturn(emptyPage);

        DiscussionListResponse response = discussionListService.service(maintopicId, pageable);

        assertThat(response).isNotNull();
        assertThat(response.getDiscussions()).isEmpty();
        assertThat(response.getTotalCount()).isEqualTo(0);
        assertThat(response.getCurrentPage()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.getTotalPages()).isEqualTo(0);

        verify(discussionRepository, times(1)).findAllDiscussions(eq(maintopicId), any(Pageable.class));
    }

    @Test
    @DisplayName("正常系: ページネーション情報が正しく設定されること")
    void testServiceReturnsPaginationInfo() {
        Pageable secondPage = PageRequest.of(1, 2);
        Page<Discussion> discussionPage = new PageImpl<>(
            discussions.subList(0, 2), 
            secondPage, 
            5
        );
        when(discussionRepository.findAllDiscussions(eq(maintopicId), any(Pageable.class)))
            .thenReturn(discussionPage);

        DiscussionListResponse response = discussionListService.service(maintopicId, secondPage);

        assertThat(response.getCurrentPage()).isEqualTo(1);
        assertThat(response.getPageSize()).isEqualTo(2);
        assertThat(response.getTotalCount()).isEqualTo(5);
        assertThat(response.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("正常系: DiscussionResponseへの変換が正しく行われること")
    void testDiscussionConvertedToDtoCorrectly() {
        Page<Discussion> discussionPage = new PageImpl<>(discussions, pageable, discussions.size());
        when(discussionRepository.findAllDiscussions(eq(maintopicId), any(Pageable.class)))
            .thenReturn(discussionPage);

        DiscussionListResponse response = discussionListService.service(maintopicId, pageable);

        List<DiscussionResponse> responseDtos = response.getDiscussions();
        assertThat(responseDtos).hasSize(3);

        DiscussionResponse firstDto = responseDtos.get(0);
        Discussion firstDiscussion = discussions.get(0);
        assertThat(firstDto.getDiscussionId()).isEqualTo(firstDiscussion.getDiscussionId());
        assertThat(firstDto.getParagraph()).isEqualTo(firstDiscussion.getParagraph());
        assertThat(firstDto.getMaintopicId()).isEqualTo(firstDiscussion.getMaintopicId());
        assertThat(firstDto.getCreatedAt()).isEqualTo(firstDiscussion.getCreatedAt());
        assertThat(firstDto.getUpdatedAt()).isEqualTo(firstDiscussion.getUpdatedAt());
    }

    private Discussion createDiscussion(Long id, String paragraph, Long maintopicId, LocalDateTime dateTime) {
        Discussion discussion = Discussion.of(maintopicId, paragraph, maintopicId, VALID_USER_ID, dateTime, dateTime, dateTime);
        return discussion;
    }
}
