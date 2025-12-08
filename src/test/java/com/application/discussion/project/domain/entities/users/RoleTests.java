package com.application.discussion.project.domain.entities.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Roleドメインエンティティのテスト")
class RoleTests {

    private static final String VALID_ROLE_NAME = "ADMIN";

    @Test
    @DisplayName("createメソッドは有効な入力でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenValidRoleName() {
        Role actualRole = Role.create(VALID_ROLE_NAME);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("createメソッドは'USER'のロール名でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenRoleNameIsUser() {
        String roleName = "USER";

        Role actualRole = Role.create(roleName);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("createメソッドは空のロール名でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenRoleNameIsEmpty() {
        String emptyRoleName = "";

        Role actualRole = Role.create(emptyRoleName);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("createメソッドはnullのロール名でRoleインスタンスを返す")
    void create_ShouldReturnRole_WhenRoleNameIsNull() {
        Role actualRole = Role.create(null);

        assertThat(actualRole).isNotNull();
    }
}
