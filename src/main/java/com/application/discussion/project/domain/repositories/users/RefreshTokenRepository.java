package com.application.discussion.project.domain.repositories.users;

import java.util.Optional;
import java.util.UUID;

import com.application.discussion.project.domain.entities.users.RefreshToken;

/**
 * リフレッシュトークンリポジトリのドメインインターフェース
 */
public interface RefreshTokenRepository {

    /**
     * トークンハッシュでリフレッシュトークンを検索する
     *
     * @param tokenHash SHA-256ハッシュ化されたトークン文字列
     * @return 該当するリフレッシュトークンのOptional
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * リフレッシュトークンを保存する
     *
     * @param refreshToken 保存対象のRefreshTokenドメインエンティティ
     * @return 保存されたRefreshTokenドメインエンティティ
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * 指定ユーザーの有効な全リフレッシュトークンを失効させる
     *
     * @param userId 対象ユーザーのUUID
     */
    void revokeAllByUserId(UUID userId);

    /**
     * リフレッシュトークンを使用済みにする
     *
     * @param tokenId リフレッシュトークンのUUID
     */
    void markAsUsed(UUID tokenId);
}
