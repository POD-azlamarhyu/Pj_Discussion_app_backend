package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RoleNormalUser クラスのテスト")
class RoleNormalUserTests {

    private static final String EXPECTED_ROLE_VALUE = "ROLE_NORMAL";
    
    private RoleNormalUser actualRoleNormalUser;

    @BeforeEach
    void setUp() {
        actualRoleNormalUser = RoleNormalUser.create();
    }

    @Test
    @DisplayName("create()でインスタンスを生成できること")
    void createShouldReturnNonNullInstance() {
        RoleNormalUser actualCreated = RoleNormalUser.create();

        assertThat(actualCreated).isNotNull();
        assertThat(actualCreated.getRoleValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("reBuild()でインスタンスを再構築できること")
    void reBuildShouldReturnNonNullInstance() {
        RoleNormalUser actualRebuilt = RoleNormalUser.reBuild();

        assertThat(actualRebuilt).isNotNull();
        assertThat(actualRebuilt.getRoleValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("defaultNormalUserRole()でデフォルトインスタンスを生成できること")
    void defaultNormalUserRoleShouldReturnNonNullInstance() {
        RoleNormalUser actualDefault = RoleNormalUser.defaultNormalUserRole();

        assertThat(actualDefault).isNotNull();
        assertThat(actualDefault.getRoleValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("create()とreBuild()で同じ値を持つインスタンスが生成されること")
    void createAndReBuildShouldProduceSameRoleValue() {
        RoleNormalUser actualCreated = RoleNormalUser.create();
        RoleNormalUser actualRebuilt = RoleNormalUser.reBuild();

        assertThat(actualCreated.getRoleValue()).isEqualTo(actualRebuilt.getRoleValue());
    }

    @Test
    @DisplayName("create()とdefaultNormalUserRole()で同じ値を持つインスタンスが生成されること")
    void createAndDefaultShouldProduceSameRoleValue() {
        RoleNormalUser actualCreated = RoleNormalUser.create();
        RoleNormalUser actualDefault = RoleNormalUser.defaultNormalUserRole();

        assertThat(actualCreated.getRoleValue()).isEqualTo(actualDefault.getRoleValue());
    }

    @Test
    @DisplayName("getRoleName()でRoleNameインスタンスを取得できること")
    void getRoleNameShouldReturnValidRoleNameInstance() {
        RoleName actualRoleName = actualRoleNormalUser.getRoleName();

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("getRoleValue()でROLE_NORMALを取得できること")
    void getRoleValueShouldReturnExpectedValue() {
        String actualRoleValue = actualRoleNormalUser.getRoleValue();

        assertThat(actualRoleValue).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("同じ値を持つインスタンスがequalsでtrueを返すこと")
    void equalsWithSameValueShouldReturnTrue() {
        RoleNormalUser actualFirst = RoleNormalUser.create();
        RoleNormalUser actualSecond = RoleNormalUser.create();

        assertThat(actualFirst).isEqualTo(actualSecond);
    }

    @Test
    @DisplayName("同一インスタンスがequalsでtrueを返すこと")
    void equalsWithSameInstanceShouldReturnTrue() {
        assertThat(actualRoleNormalUser).isEqualTo(actualRoleNormalUser);
    }

    @Test
    @DisplayName("nullとの比較でfalseを返すこと")
    void equalsWithNullShouldReturnFalse() {
        assertThat(actualRoleNormalUser).isNotEqualTo(null);
    }

    @Test
    @DisplayName("異なる型のオブジェクトとの比較でfalseを返すこと")
    void equalsWithDifferentTypeShouldReturnFalse() {
        String differentTypeObject = "ROLE_NORMAL";

        assertThat(actualRoleNormalUser).isNotEqualTo(differentTypeObject);
    }

    @Test
    @DisplayName("異なるRoleTypeとの比較でfalseを返すこと")
    void equalsWithDifferentRoleTypeShouldReturnFalse() {
        RoleAdmin differentRoleType = RoleAdmin.create();

        assertThat(actualRoleNormalUser).isNotEqualTo(differentRoleType);
    }

    @Test
    @DisplayName("同じ値を持つインスタンスが同じhashCodeを返すこと")
    void hashCodeWithSameValueShouldBeEqual() {
        RoleNormalUser actualFirst = RoleNormalUser.create();
        RoleNormalUser actualSecond = RoleNormalUser.create();

        assertThat(actualFirst.hashCode()).isEqualTo(actualSecond.hashCode());
    }

    @Test
    @DisplayName("同一インスタンスが同じhashCodeを返すこと")
    void hashCodeWithSameInstanceShouldBeConsistent() {
        int expectedHashCode = actualRoleNormalUser.hashCode();

        assertThat(actualRoleNormalUser.hashCode()).isEqualTo(expectedHashCode);
    }

    @Test
    @DisplayName("toString()が期待される文字列形式を返すこと")
    void toStringShouldContainExpectedFields() {
        String actualToString = actualRoleNormalUser.toString();

        assertThat(actualToString)
                .isNotNull()
                .contains("RoleNormalUser")
                .contains("roleName");
    }

    @Test
    @DisplayName("toString()がnullを返さないこと")
    void toStringShouldNotReturnNull() {
        String actualToString = actualRoleNormalUser.toString();

        assertThat(actualToString).isNotNull();
    }
}
