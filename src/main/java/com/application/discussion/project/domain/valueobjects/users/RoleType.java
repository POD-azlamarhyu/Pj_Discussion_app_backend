package com.application.discussion.project.domain.valueobjects.users;

/**
 * ユーザのロールタイプを表すシールドインターフェース
 * RoleAdminとRoleNormalUserが実装する
 */
public sealed interface RoleType permits RoleAdmin, RoleNormalUser {
    RoleName getRoleName();
    String getRoleValue();
}
