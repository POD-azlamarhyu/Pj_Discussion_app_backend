package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.users.RoleAdmin;
import com.application.discussion.project.domain.valueobjects.users.RoleName;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

@DisplayName("Roleドメインエンティティのテスト")
class RoleTests {

    private static final String ADMIN_ROLE_VALUE = "ADMIN";
    private static final String USER_ROLE_VALUE = "USER";
    private static final String EMPTY_ROLE_VALUE = "";
    private static final Integer VALID_ROLE_ID = 1;
    private static final String ROLE_ADMIN_VALUE = "ROLE_ADMIN";

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

    @Test
    @DisplayName("of_有効なパラメータでRoleインスタンスを再構築できる")
    void ofReturnsRoleInstanceWhenValidParameters() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now();
        LocalDateTime expectedUpdatedAt = LocalDateTime.now();

        Role actualRole = Role.of(
            VALID_ROLE_ID,
            ROLE_ADMIN_VALUE,
            expectedCreatedAt,
            expectedUpdatedAt,
            null,
            adminRole
        );

        assertThat(actualRole).isNotNull();
        assertThat(actualRole.getRoleId()).isEqualTo(VALID_ROLE_ID);
    }

    @Test
    @DisplayName("of_削除日時がnullでもRoleインスタンスを再構築できる")
    void ofReturnsRoleInstanceWhenDeletedAtIsNull() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now();

        Role actualRole = Role.of(
            VALID_ROLE_ID,
            ROLE_ADMIN_VALUE,
            expectedCreatedAt,
            null,
            null,
            adminRole
        );

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("of_全ての日時フィールドが設定されたRoleインスタンスを再構築できる")
    void ofReturnsRoleInstanceWhenAllDateFieldsSet() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now();
        LocalDateTime expectedUpdatedAt = LocalDateTime.now();
        LocalDateTime expectedDeletedAt = LocalDateTime.now();

        Role actualRole = Role.of(
            VALID_ROLE_ID,
            ROLE_ADMIN_VALUE,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeletedAt,
            adminRole
        );

        assertThat(actualRole).isNotNull();
    }

    @Test
    @DisplayName("getRoleId_ロールIDを正しく取得できる")
    void getRoleIdReturnsCorrectValue() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now();

        Role actualRole = Role.of(
            VALID_ROLE_ID,
            ROLE_ADMIN_VALUE,
            expectedCreatedAt,
            null,
            null,
            adminRole
        );

        Integer actualRoleId = actualRole.getRoleId();

        assertThat(actualRoleId).isEqualTo(VALID_ROLE_ID);
    }

    @Test
    @DisplayName("getRoleName_RoleNameインスタンスを正しく取得できる")
    void getRoleNameReturnsCorrectInstance() {
        Role actualRole = Role.create(ADMIN_ROLE_VALUE, adminRole);

        RoleName actualRoleName = actualRole.getRoleName();

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).isEqualTo(ADMIN_ROLE_VALUE);
    }

    @Test
    @DisplayName("getRoleNameValue_ロール名の文字列値を正しく取得できる")
    void getRoleNameValueReturnsCorrectStringValue() {
        Role actualRole = Role.create(ADMIN_ROLE_VALUE, adminRole);

        String actualRoleNameValue = actualRole.getRoleNameValue();

        assertThat(actualRoleNameValue).isEqualTo(ADMIN_ROLE_VALUE);
    }

    @Test
    @DisplayName("getRoleType_RoleTypeインスタンスを正しく取得できる")
    void getRoleTypeReturnsCorrectInstance() {
        Role actualRole = Role.create(ADMIN_ROLE_VALUE, adminRole);

        RoleType actualRoleType = actualRole.getRoleType();

        assertThat(actualRoleType).isNotNull();
        assertThat(actualRoleType).isEqualTo(adminRole);
    }

    @Test
    @DisplayName("toString_updatedAtとdeletedAtがnullの場合に正しい文字列表現を返す")
    void toStringReturnsCorrectFormatWhenDatesAreNull() {
        Role actualRole = Role.create(ADMIN_ROLE_VALUE, adminRole);

        String actualToString = actualRole.toString();

        assertThat(actualToString)
                .isNotNull()
                .contains("Role{")
                .contains("roleId=")
                .contains("roleName=")
                .contains("createdAt=")
                .contains("updatedAt=null")
                .contains("deletedAt=null")
                .contains("roleType=");
    }

    @Test
    @DisplayName("toString_全ての日時フィールドが設定されている場合に正しい文字列表現を返す")
    void toStringReturnsCorrectFormatWhenAllDatesSet() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now();
        LocalDateTime expectedUpdatedAt = LocalDateTime.now();
        LocalDateTime expectedDeletedAt = LocalDateTime.now();

        Role actualRole = Role.of(
            VALID_ROLE_ID,
            ROLE_ADMIN_VALUE,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeletedAt,
            adminRole
        );

        String actualToString = actualRole.toString();

        assertThat(actualToString)
                .isNotNull()
                .contains("Role{")
                .contains("roleId=")
                .contains("roleName=")
                .contains("createdAt=")
                .contains("updatedAt=")
                .contains("deletedAt=")
                .contains("roleType=")
                .doesNotContain("null");
    }

    @Test
    @DisplayName("toString_ロールタイプの値が含まれていること")
    void toStringContainsRoleTypeValue() {
        Role actualRole = Role.create(ADMIN_ROLE_VALUE, adminRole);

        String actualToString = actualRole.toString();

        assertThat(actualToString).contains(adminRole.getRoleValue());
    }
}
