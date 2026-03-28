package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

/**
 * リフレッシュトークンのドメインエンティティ
 */
public class RefreshToken {

    private final UUID id;
    private final UUID userId;
    private final String tokenHash;
    private final LocalDateTime expiresAt;
    private final Boolean isUsed;
    private final Boolean isRevoked;
    private final LocalDateTime createdAt;

    private static final Logger logger = LoggerFactory.getLogger(RefreshToken.class);

    private RefreshToken(
        UUID id,
        UUID userId,
        String tokenHash,
        LocalDateTime expiresAt,
        Boolean isUsed,
        Boolean isRevoked,
        LocalDateTime createdAt
    ) {
        logger.info("Creating RefreshToken for userId: {}", userId);

        if (Objects.isNull(userId)) {
            logger.error("Failed to create RefreshToken: userId is null");
            throw new DomainLayerErrorException(
                "ユーザーIDが指定されていません",
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatusCode.valueOf(500)
            );
        }
        if (Objects.isNull(tokenHash) || tokenHash.isBlank()) {
            logger.error("Failed to create RefreshToken: tokenHash is null or blank");
            throw new DomainLayerErrorException(
                "トークンハッシュが指定されていません",
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatusCode.valueOf(500)
            );
        }

        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
        this.isRevoked = isRevoked;
        this.createdAt = createdAt;

        logger.info("RefreshToken created successfully for userId: {}", userId);
    }

    /**
     * 新規リフレッシュトークンを作成するファクトリメソッド
     *
     * @param userId ユーザーID
     * @param tokenHash トークンのSHA-256ハッシュ
     * @param expiresAt 有効期限
     * @return 新規RefreshTokenオブジェクト
     */
    public static RefreshToken create(
        final UUID userId,
        final String tokenHash,
        final LocalDateTime expiresAt
    ) {
        return new RefreshToken(
            UUID.randomUUID(),
            userId,
            tokenHash,
            expiresAt,
            false,
            false,
            LocalDateTime.now()
        );
    }

    /**
     * 永続化されたリフレッシュトークン情報からオブジェクトを再構築するファクトリメソッド
     *
     * @param id リフレッシュトークンID
     * @param userId ユーザーID
     * @param tokenHash トークンのSHA-256ハッシュ
     * @param expiresAt 有効期限
     * @param isUsed 使用済みフラグ
     * @param isRevoked 失効フラグ
     * @param createdAt 作成日時
     * @return 再構築されたRefreshTokenオブジェクト
     */
    public static RefreshToken reBuild(
        final UUID id,
        final UUID userId,
        final String tokenHash,
        final LocalDateTime expiresAt,
        final Boolean isUsed,
        final Boolean isRevoked,
        final LocalDateTime createdAt
    ) {
        logger.info("Rebuilding RefreshToken with id: {} for userId: {}", id, userId);
        return new RefreshToken(id, userId, tokenHash, expiresAt, isUsed, isRevoked, createdAt);
    }

    /**
     * トークンが有効かどうかを判定する
     *
     * @return 使用済みでなく、失効しておらず、有効期限内の場合true
     */
    public boolean isValid() {
        return Boolean.FALSE.equals(isUsed)
            && Boolean.FALSE.equals(isRevoked)
            && LocalDateTime.now().isBefore(expiresAt);
    }

    /**
     * トークンが使用済みかどうかを判定する
     *
     * @return 使用済みの場合true
     */
    public boolean isAlreadyUsed() {
        return Boolean.TRUE.equals(isUsed);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public Boolean getIsRevoked() {
        return isRevoked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
            "id=" + id +
            ", userId=" + userId +
            ", tokenHash='****'" +
            ", expiresAt=" + expiresAt +
            ", isUsed=" + isUsed +
            ", isRevoked=" + isRevoked +
            ", createdAt=" + createdAt +
            '}';
    }
}
