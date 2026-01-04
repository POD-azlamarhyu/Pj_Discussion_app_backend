package com.application.discussion.project.domain.entities.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.application.discussion.project.domain.valueobjects.users.RoleAdmin;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

@DisplayName("Roleドメインエンティティのテスト")
class RoleTests {

    private static final String VALID_ROLE_NAME = "ADMIN";
    private final RoleType adminRole = RoleAdmin.create();
    private final RoleType NormalRole = RoleNormalUser.create();

    @Test
    @DisplayName("createメソッドは有効な入力でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenValidRoleName() {
        Role actualRole = Role.create(adminRole.getRoleValue(),adminRole);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("createメソッドは'USER'のロール名でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenRoleNameIsUser() {

        Role actualRole = Role.create(NormalRole.getRoleValue(),NormalRole);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("createメソッドは空のロール名でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenRoleNameIsEmpty() {
        String emptyRoleName = "";

        Role actualRole = Role.create(emptyRoleName,NormalRole);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("createメソッドはnullのロール名でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenRoleNameIsNull() {
        Role actualRole = Role.create(null,NormalRole);

        assertThat(actualRole).isNotNull();
    }
}
