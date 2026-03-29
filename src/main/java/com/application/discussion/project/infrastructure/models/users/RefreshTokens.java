package com.application.discussion.project.infrastructure.models.users;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * リフレッシュトークンのJPAエンティティ
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_used", nullable = false, columnDefinition = "boolean default false")
    private Boolean isUsed;

    @Column(name = "is_revoked", nullable = false, columnDefinition = "boolean default false")
    private Boolean isRevoked;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public RefreshTokens() {
        this.isUsed = false;
        this.isRevoked = false;
    }

    public RefreshTokens(
        UUID id,
        Users users,
        String tokenHash,
        LocalDateTime expiresAt,
        Boolean isUsed,
        Boolean isRevoked,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.user = users;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
        this.isRevoked = isRevoked;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean getIsRevoked() {
        return isRevoked;
    }

    public void setIsRevoked(Boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
