package com.application.discussion.project.domain.entities.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.users.RoleAdmin;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

@DisplayName("Roleドメインエンティティのテスト")
class RoleTests {

    private static final String ADMIN_ROLE_VALUE = "ADMIN";
    private static final String USER_ROLE_VALUE = "USER";
    private static final String EMPTY_ROLE_VALUE = "";

    private final RoleType adminRole = RoleAdmin.create();
    private final RoleType normalRole = RoleNormalUser.create();

    @Test
    @DisplayName("create_有効なロール値でRoleインスタンスが生成される")
    void createReturnsRoleInstanceWhenValidRoleValue() {
        Role actualRole = Role.create(ADMIN_ROLE_VALUE, adminRole);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("create_USER ロール値でRoleインスタンスが生成される")
    void createReturnsRoleInstanceWhenRoleValueIsUser() {
        Role actualRole = Role.create(USER_ROLE_VALUE, normalRole);

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("create_空のロール値の場合に例外がスローされる")
    void createThrowsExceptionWhenRoleValueIsEmpty() {
        assertThatThrownBy(() -> Role.create(EMPTY_ROLE_VALUE, normalRole))
                .isInstanceOf(DomainLayerErrorException.class);
    }

    @Test
    @DisplayName("create_nullのロール値の場合に例外がスローされる")
    void createThrowsExceptionWhenRoleValueIsNull() {
        assertThatThrownBy(() -> Role.create(null, normalRole))
                .isInstanceOf(DomainLayerErrorException.class);
    }
}
