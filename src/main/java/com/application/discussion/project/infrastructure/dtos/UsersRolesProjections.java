package com.application.discussion.project.infrastructure.dtos;

import java.time.LocalDateTime;

/**
 * UsersRolesProjectionsインターフェースは、ユーザーの役割に関するデータを取得するためのプロジェクションを定義します。
 * これにより、必要なフィールドのみを効率的に取得できます。
 */
public interface UsersRolesProjections {
    Integer getRoleId();
    String getRoleName();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
}
