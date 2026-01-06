package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RoleAdmin クラスのテスト")
class RoleAdminTests {

    private static final String EXPECTED_ROLE_VALUE = "ROLE_ADMIN";
    
    private RoleAdmin actualRoleAdmin;

    @BeforeEach
    void setUp() {
        actualRoleAdmin = RoleAdmin.create();
    }

    @Test
    @DisplayName("create()でインスタンスを生成できること")
    void createShouldReturnNonNullInstance() {
        RoleAdmin actualCreated = RoleAdmin.create();

        assertThat(actualCreated).isNotNull();
        assertThat(actualCreated.getRoleValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("reBuild()でインスタンスを再構築できること")
    void reBuildShouldReturnNonNullInstance() {
        RoleAdmin actualRebuilt = RoleAdmin.reBuild();

        assertThat(actualRebuilt).isNotNull();
        assertThat(actualRebuilt.getRoleValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("create()とreBuild()で同じ値を持つインスタンスが生成されること")
    void createAndReBuildShouldProduceSameRoleValue() {
        RoleAdmin actualCreated = RoleAdmin.create();
        RoleAdmin actualRebuilt = RoleAdmin.reBuild();

        assertThat(actualCreated.getRoleValue()).isEqualTo(actualRebuilt.getRoleValue());
    }

    @Test
    @DisplayName("getRoleName()でRoleNameインスタンスを取得できること")
    void getRoleNameShouldReturnValidRoleNameInstance() {
        RoleName actualRoleName = actualRoleAdmin.getRoleName();

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("getRoleValue()でROLE_ADMINを取得できること")
    void getRoleValueShouldReturnExpectedValue() {
        String actualRoleValue = actualRoleAdmin.getRoleValue();

        assertThat(actualRoleValue).isEqualTo(EXPECTED_ROLE_VALUE);
    }

    @Test
    @DisplayName("同じ値を持つインスタンスがequalsでtrueを返すこと")
    void equalsWithSameValueShouldReturnTrue() {
        RoleAdmin actualFirst = RoleAdmin.create();
        RoleAdmin actualSecond = RoleAdmin.create();

        assertThat(actualFirst).isEqualTo(actualSecond);
    }

    @Test
    @DisplayName("同一インスタンスがequalsでtrueを返すこと")
    void equalsWithSameInstanceShouldReturnTrue() {
        assertThat(actualRoleAdmin).isEqualTo(actualRoleAdmin);
    }

    @Test
    @DisplayName("nullとの比較でfalseを返すこと")
    void equalsWithNullShouldReturnFalse() {
        assertThat(actualRoleAdmin).isNotEqualTo(null);
    }

    @Test
    @DisplayName("異なる型のオブジェクトとの比較でfalseを返すこと")
    void equalsWithDifferentTypeShouldReturnFalse() {
        String differentTypeObject = "ROLE_ADMIN";

        assertThat(actualRoleAdmin).isNotEqualTo(differentTypeObject);
    }

    @Test
    @DisplayName("同じ値を持つインスタンスが同じhashCodeを返すこと")
    void hashCodeWithSameValueShouldBeEqual() {
        RoleAdmin actualFirst = RoleAdmin.create();
        RoleAdmin actualSecond = RoleAdmin.create();

        assertThat(actualFirst.hashCode()).isEqualTo(actualSecond.hashCode());
    }

    @Test
    @DisplayName("同一インスタンスが同じhashCodeを返すこと")
    void hashCodeWithSameInstanceShouldBeConsistent() {
        int expectedHashCode = actualRoleAdmin.hashCode();

        assertThat(actualRoleAdmin.hashCode()).isEqualTo(expectedHashCode);
    }

    @Test
    @DisplayName("toString()が期待される文字列形式を返すこと")
    void toStringShouldContainExpectedFields() {
        String actualToString = actualRoleAdmin.toString();

        assertThat(actualToString)
                .isNotNull()
                .contains("RoleAdmin")
                .contains("roleName");
    }

    @Test
    @DisplayName("toString()がnullを返さないこと")
    void toStringShouldNotReturnNull() {
        String actualToString = actualRoleAdmin.toString();

        assertThat(actualToString).isNotNull();
    }
}
