package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.users.RoleName;
import com.application.discussion.project.domain.valueobjects.users.RoleType;


public class Role {
    private final Integer roleId;
    private final RoleName roleName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
    private final RoleType roleType;

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
        RoleName roleName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        RoleType roleType
    ) {
        logger.info("Creating Role with ID: {}, Name: {}", roleId, roleName);
        if (Objects.isNull(roleId)) {
            logger.error("Failed to create Role: roleId is null");
            throw new DomainLayerErrorException("エラーが発生しました",HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.valueOf(500));
        }
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.roleType = roleType;
        logger.info("Role created successfully with ID: {}", roleId);
    }

    /**
     * ファクトリーメソッド：新しいRoleインスタンスを作成する
     *
     * @param roleName ロール名
     * @return 新しいRoleインスタンス
     */
    public static Role create(
        String roleName,
        RoleType roleType
    ) {
        return new Role(0, RoleName.of(roleName), LocalDateTime.now(),null,null, roleType);
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
        LocalDateTime deletedAt,
        RoleType roleType
    ) {
        return new Role(roleId, RoleName.reBuild(roleName), createdAt, updatedAt, deletedAt, roleType);
    }

    /**
    * @return roleId
    */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * Getter
     * 
     * @return roleName
     */
    public RoleName getRoleName() {
        return roleName;
    }

    /**
     * Getter
     * 
     * @return roleName value
     */
    public String getRoleNameValue() {
        return roleName.getValue();
    }

    /**
     * Getter
     * 
     * @return roleType
     */
    public RoleType getRoleType() {
        return roleType;
    }

    /**
     * Roleの文字列表現を取得する
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        String toStringUpdatedAt = Objects.isNull(updatedAt) ? "null" : updatedAt.toString();
        String toStringDeletedAt = Objects.isNull(deletedAt) ? "null" : deletedAt.toString();
        return "Role{" +
                "roleId=" + roleId.toString() +
                ", roleName=" + roleName +
                ", createdAt=" + createdAt.toString() +
                ", updatedAt=" + toStringUpdatedAt +
                ", deletedAt=" + toStringDeletedAt +
                ", roleType=" + roleType.getRoleValue() +
                '}';
    }
}
