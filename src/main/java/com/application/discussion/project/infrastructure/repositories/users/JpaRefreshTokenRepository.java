package com.application.discussion.project.infrastructure.repositories.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.models.users.RefreshTokens;

/**
 * リフレッシュトークンのSpring Data JPAリポジトリ
 */
@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokens, UUID> {

    /**
     * トークンハッシュでリフレッシュトークンを検索する
     *
     * @param tokenHash SHA-256ハッシュ化されたトークン文字列
     * @return 該当するリフレッシュトークンのOptional
     */
    Optional<RefreshTokens> findByTokenHash(String tokenHash);

    /**
     * 指定ユーザーの有効な全リフレッシュトークンを取得する
     *
     * @param userId ユーザーID
     * @return 未失効のリフレッシュトークンリスト
     */
    List<RefreshTokens> findAllByUserUserIdAndIsRevokedFalse(UUID userId);

    /**
     * 指定ユーザーの全リフレッシュトークンを失効させる
     *
     * @param userId ユーザーID
     */
    @Modifying
    @Query(value = "UPDATE refresh_tokens SET is_revoked = true WHERE user_id = :userId AND is_revoked = false", nativeQuery = true)
    void revokeAllByUserId(@Param("userId") UUID userId);

    /**
     * リフレッシュトークンを使用済みにする
     *
     * @param tokenId リフレッシュトークンID
     */
    @Modifying
    @Query(value = "UPDATE refresh_tokens SET is_used = true WHERE id = :tokenId", nativeQuery = true)
    void markAsUsed(@Param("tokenId") UUID tokenId);
}
