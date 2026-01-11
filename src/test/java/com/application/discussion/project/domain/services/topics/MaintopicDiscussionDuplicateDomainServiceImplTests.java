package com.application.discussion.project.domain.services.topics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.MaintopicRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MaintopicDiscussionDuplicateDomainServiceImpl クラスのテスト")
public class MaintopicDiscussionDuplicateDomainServiceImplTests {

    @Mock
    private MaintopicRepository maintopicRepository;

    @InjectMocks
    private MaintopicDiscussionDuplicateDomainServiceImpl maintopicDiscussionDuplicateDomainService;

    private static final Long VALID_MAINTOPIC_ID = 1L;
    private static final Long MAINTOPIC_ID = 3L;
    private static final Long NON_EXISTENT_MAINTOPIC_ID = 999L;
    private static final Integer MOCK_WANTED_NUMBER = 1;

    @Test
    @DisplayName("正常系：メイントピックが存在する場合にtrueを返すこと")
    void isDuplicateDiscussionExistsReturnsTrueTest() {
        when(maintopicRepository.existsMaintopic(MAINTOPIC_ID)).thenReturn(true);

        final Boolean result = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID);

        assertTrue(result);
        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(MAINTOPIC_ID);
    }

    @Test
    @DisplayName("正常系：メイントピックが存在しない場合にfalseを返すこと")
    void isDuplicateDiscussionExistsReturnsFalseTest() {
        when(maintopicRepository.existsMaintopic(MAINTOPIC_ID)).thenReturn(false);

        final Boolean result = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID);

        assertFalse(result);
        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(MAINTOPIC_ID);
    }

    @Test
    @DisplayName("正常系：異なるメイントピックIDで存在確認が成功すること")
    void isDuplicateDiscussionExistsWithDifferentIdTest() {
        final Long differentMaintopicId = 999L;

        when(maintopicRepository.existsMaintopic(differentMaintopicId)).thenReturn(true);

        final Boolean result = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(differentMaintopicId);

        assertTrue(result);
        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(differentMaintopicId);
    }

    @Test
    @DisplayName("正常系：複数回呼び出しても正しく動作すること")
    void isDuplicateDiscussionExistsWithMultipleCallsTest() {
        when(maintopicRepository.existsMaintopic(MAINTOPIC_ID)).thenReturn(true);

        final Boolean firstResult = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID);
        final Boolean secondResult = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID);

        assertTrue(firstResult);
        assertTrue(secondResult);
        verify(maintopicRepository, times(2)).existsMaintopic(MAINTOPIC_ID);
    }

    @Test
    @DisplayName("正常系：複数のメイントピックIDで連続して存在確認が成功すること")
    void isDuplicateDiscussionExistsWithMultipleIdsTest() {
        final Long firstMaintopicId = 1L;
        final Long secondMaintopicId = 2L;

        when(maintopicRepository.existsMaintopic(firstMaintopicId)).thenReturn(true);
        when(maintopicRepository.existsMaintopic(secondMaintopicId)).thenReturn(false);

        final Boolean firstResult = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(firstMaintopicId);
        final Boolean secondResult = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(secondMaintopicId);

        assertTrue(firstResult);
        assertFalse(secondResult);

        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(firstMaintopicId);
        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(secondMaintopicId);
    }

    @Test
    @DisplayName("正常系：ゼロのメイントピックIDで確認した場合に存在しないと判定されること")
    void isDuplicateDiscussionExistsWithZeroIdTest() {
        final Long zeroId = 0L;

        when(maintopicRepository.existsMaintopic(zeroId)).thenReturn(false);

        final Boolean result = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(zeroId);

        assertFalse(result);
        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(zeroId);
    }

    @Test
    @DisplayName("正常系：メイントピックIDがnullの場合にFalseが返ること")
    void isDuplicateDiscussionExistsWithNullIdThrowsExceptionTest() {
        when(maintopicRepository.existsMaintopic(null)).thenReturn(false);

        final Boolean result = maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(null);

        assertFalse(result);
        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(null);
    }

    @Test
    @DisplayName("異常系：リポジトリで例外が発生した場合に例外が伝播すること")
    void isDuplicateDiscussionExistsWithRepositoryExceptionTest() {
        when(maintopicRepository.existsMaintopic(MAINTOPIC_ID))
            .thenThrow(new RuntimeException("データベース接続エラー"));

        assertThrows(RuntimeException.class, () -> {
            maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(MAINTOPIC_ID);
        });

        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(MAINTOPIC_ID);
    }

    @Test
    @DisplayName("異常系：リポジトリ層でエラーが発生した場合にDomainLayerErrorExceptionが伝播すること")
    void isDuplicateDiscussionExistsWithRepositoryDomainErrorTest() {
        when(maintopicRepository.existsMaintopic(VALID_MAINTOPIC_ID))
            .thenThrow(new DomainLayerErrorException(
                "データベース接続エラー",
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatusCode.valueOf(500)
            ));

        assertThrows(DomainLayerErrorException.class, () -> {
            maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(VALID_MAINTOPIC_ID);
        });

        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(VALID_MAINTOPIC_ID);
    }

    @Test
    @DisplayName("異常系：負のメイントピックIDで確認した場合に例外が発生すること")
    void isDuplicateDiscussionExistsWithNegativeIdTest() {
        final Long negativeId = -1L;

        when(maintopicRepository.existsMaintopic(negativeId))
            .thenThrow(new IllegalArgumentException("メイントピックIDは正の値である必要があります"));

        assertThrows(IllegalArgumentException.class, () -> {
            maintopicDiscussionDuplicateDomainService.isDuplicateDiscussionExists(negativeId);
        });

        verify(maintopicRepository, times(MOCK_WANTED_NUMBER)).existsMaintopic(negativeId);
    }
}
