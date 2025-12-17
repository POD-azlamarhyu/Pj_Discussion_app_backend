package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Role {
    private final Integer roleId;
    private final String roleName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    private static final Logger logger = LoggerFactory.getLogger(Role.class);
    /**
     * Private constructor to enforce the use of the builder or factory method.
     *
     * @param roleId    The unique identifier for the role.
     * @param roleName  The name of the role.
     * @param createdAt The timestamp when the role was created.
     * @param updatedAt The timestamp when the role was last updated.
     * @param deletedAt The timestamp when the role was deleted, if applicable.
     */

    private Role(
        Integer roleId,
        String roleName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        logger.info("Creating Role with ID: {}, Name: {}", roleId, roleName);
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * ファクトリーメソッド：新しいRoleインスタンスを作成する
     *
     * @param roleName ロール名
     * @return 新しいRoleインスタンス
     */
    public static Role create(
        String roleName
    ) {
        return new Role(0, roleName, LocalDateTime.now(),null,null);
    }

    /**
     * 再構築メソッド：既存のRoleインスタンスを再構築する
     * @param roleId    ロールID
     * @param roleName  ロール名
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 再構築されたRoleインスタンス
     */
    public static Role of(
        Integer roleId,
        String roleName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        return new Role(roleId, roleName, createdAt, updatedAt, deletedAt);
    }

    /**
    * @return roleId
    */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 
     * @return roleName
     */
    public String getRoleName() {
        return roleName;
    }
}
