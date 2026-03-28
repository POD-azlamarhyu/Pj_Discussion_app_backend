package com.application.discussion.project.infrastructure.repositories.users;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.discussion.project.domain.entities.users.RefreshToken;
import com.application.discussion.project.domain.repositories.users.RefreshTokenRepository;
import com.application.discussion.project.infrastructure.models.users.RefreshTokens;
import com.application.discussion.project.infrastructure.models.users.Users;

/**
 * リフレッシュトークンリポジトリの実装クラス
 */
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenRepositoryImpl.class);

    @Autowired
    private JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByTokenHash(final String tokenHash) {
        logger.info("Searching refresh token by hash");
        return jpaRefreshTokenRepository.findByTokenHash(tokenHash)
            .map(this::mapToDomainEntity);
    }

    @Override
    @Transactional
    public RefreshToken save(final RefreshToken refreshToken) {
        logger.info("Saving refresh token for userId: {}", refreshToken.getUserId());
        RefreshTokens model = mapToModelEntity(refreshToken);
        RefreshTokens saved = jpaRefreshTokenRepository.save(model);
        logger.info("Refresh token saved successfully with id: {}", saved.getId());
        return mapToDomainEntity(saved);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(final UUID userId) {
        logger.info("Revoking all refresh tokens for userId: {}", userId);
        jpaRefreshTokenRepository.revokeAllByUserId(userId);
        logger.info("All refresh tokens revoked for userId: {}", userId);
    }

    @Override
    @Transactional
    public void markAsUsed(final UUID tokenId) {
        logger.info("Marking refresh token as used: {}", tokenId);
        jpaRefreshTokenRepository.markAsUsed(tokenId);
        logger.info("Refresh token marked as used: {}", tokenId);
    }

    /**
     * JPAモデルをドメインエンティティにマッピングする
     */
    private RefreshToken mapToDomainEntity(final RefreshTokens model) {
        logger.info("Mapping JPA model to domain entity for tokenId: {}", model.getId());
        return RefreshToken.reBuild(
            model.getId(),
            model.getUser().getUserId(),
            model.getTokenHash(),
            model.getExpiresAt(),
            model.getIsUsed(),
            model.getIsRevoked(),
            model.getCreatedAt()
        );
    }

    /**
     * ドメインエンティティをJPAモデルにマッピングする
     */
    private RefreshTokens mapToModelEntity(final RefreshToken domainEntity) {
        logger.info("Mapping domain entity to JPA model for tokenId: {}", domainEntity.getId());
        RefreshTokens model = new RefreshTokens();
        Users user = new Users();
        user.setUserId(domainEntity.getUserId());

        model.setUser(user);
        model.setTokenHash(domainEntity.getTokenHash());
        model.setExpiresAt(domainEntity.getExpiresAt());
        model.setIsUsed(domainEntity.getIsUsed());
        model.setIsRevoked(domainEntity.getIsRevoked());
        model.setCreatedAt(domainEntity.getCreatedAt());
        return model;
    }
}
