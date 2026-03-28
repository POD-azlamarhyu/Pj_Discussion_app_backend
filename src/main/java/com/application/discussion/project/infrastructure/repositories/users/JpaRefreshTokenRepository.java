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
    List<RefreshTokens> findAllByUserIdAndIsRevokedFalse(UUID userId);

    /**
     * 指定ユーザーの全リフレッシュトークンを失効させる
     *
     * @param userId ユーザーID
     */
    @Modifying
    @Query("UPDATE RefreshTokens r SET r.isRevoked = true WHERE r.userId = :userId AND r.isRevoked = false")
    void revokeAllByUserId(@Param("userId") UUID userId);

    /**
     * リフレッシュトークンを使用済みにする
     *
     * @param tokenId リフレッシュトークンID
     */
    @Modifying
    @Query("UPDATE RefreshTokens r SET r.isUsed = true WHERE r.id = :tokenId")
    void markAsUsed(@Param("tokenId") UUID tokenId);
}
