package com.application.discussion.project.infrastructure.repositories.users;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.domain.entities.users.RefreshToken;
import com.application.discussion.project.infrastructure.models.users.RefreshTokens;
import com.application.discussion.project.infrastructure.models.users.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenRepositoryImplのテスト")
public class RefreshTokenRepositoryImplTests {

    private static final UUID TEST_TOKEN_ID = UUID.randomUUID();
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_TOKEN_HASH = "abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789";

    @Mock
    private JpaRefreshTokenRepository mockJpaRefreshTokenRepository;

    @InjectMocks
    private RefreshTokenRepositoryImpl refreshTokenRepository;

    @Test
    @DisplayName("findByTokenHashは見つかったモデルをドメインに変換して返す")
    public void findByTokenHashReturnsDomainEntityWhenFound() {
        Users mockUser = new Users();
        mockUser.setUserId(TEST_USER_ID);
        RefreshTokens mockModel = new RefreshTokens(
            TEST_TOKEN_ID,
            mockUser,
            TEST_TOKEN_HASH,
            LocalDateTime.now().plusHours(1),
            false,
            false,
            LocalDateTime.now().minusMinutes(1)
        );
        when(mockJpaRefreshTokenRepository.findByTokenHash(TEST_TOKEN_HASH)).thenReturn(Optional.of(mockModel));

        Optional<RefreshToken> actualToken = refreshTokenRepository.findByTokenHash(TEST_TOKEN_HASH);

        assertThat(actualToken).isPresent();
        assertThat(actualToken.get().getId()).isEqualTo(TEST_TOKEN_ID);
        assertThat(actualToken.get().getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(actualToken.get().getTokenHash()).isEqualTo(TEST_TOKEN_HASH);
    }

    @Test
    @DisplayName("findByTokenHashは見つからない場合にEmptyを返す")
    public void findByTokenHashReturnsEmptyWhenNotFound() {
        when(mockJpaRefreshTokenRepository.findByTokenHash(TEST_TOKEN_HASH)).thenReturn(Optional.empty());

        Optional<RefreshToken> actualToken = refreshTokenRepository.findByTokenHash(TEST_TOKEN_HASH);

        assertThat(actualToken).isEmpty();
    }

    @Test
    @DisplayName("saveはドメインエンティティを保存してドメインとして返す")
    public void saveReturnsPersistedDomainEntity() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now().minusMinutes(1);
        LocalDateTime expectedExpiresAt = LocalDateTime.now().plusHours(1);
        RefreshToken inputToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_TOKEN_HASH,
            expectedExpiresAt,
            false,
            false,
            expectedCreatedAt
        );
        Users expectedUser = new Users();
        expectedUser.setUserId(TEST_USER_ID);
        RefreshTokens savedModel = new RefreshTokens(
            TEST_TOKEN_ID,
            expectedUser,
            TEST_TOKEN_HASH,
            expectedExpiresAt,
            false,
            false,
            expectedCreatedAt
        );

        when(mockJpaRefreshTokenRepository.save(org.mockito.ArgumentMatchers.any(RefreshTokens.class))).thenReturn(savedModel);

        RefreshToken actualSaved = refreshTokenRepository.save(inputToken);

        ArgumentCaptor<RefreshTokens> modelCaptor = ArgumentCaptor.forClass(RefreshTokens.class);
        verify(mockJpaRefreshTokenRepository).save(modelCaptor.capture());

        assertThat(modelCaptor.getValue().getUser().getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(modelCaptor.getValue().getTokenHash()).isEqualTo(TEST_TOKEN_HASH);
        assertThat(actualSaved.getId()).isEqualTo(TEST_TOKEN_ID);
        assertThat(actualSaved.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(actualSaved.getTokenHash()).isEqualTo(TEST_TOKEN_HASH);
    }

    @Test
    @DisplayName("revokeAllByUserIdはJPAリポジトリに処理を委譲する")
    public void revokeAllByUserIdDelegatesToJpaRepository() {
        refreshTokenRepository.revokeAllByUserId(TEST_USER_ID);

        verify(mockJpaRefreshTokenRepository).revokeAllByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("markAsUsedはJPAリポジトリに処理を委譲する")
    public void markAsUsedDelegatesToJpaRepository() {
        refreshTokenRepository.markAsUsed(TEST_TOKEN_ID);

        verify(mockJpaRefreshTokenRepository).markAsUsed(TEST_TOKEN_ID);
    }
}
