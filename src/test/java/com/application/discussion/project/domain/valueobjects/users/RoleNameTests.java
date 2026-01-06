package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("RoleName クラスのテスト")
class RoleNameTests {

    private static final String VALID_ROLE_NAME = "ROLE_ADMIN";
    private static final String FIFTY_CHAR_ROLE_NAME = "A".repeat(50);
    private static final String FIFTY_ONE_CHAR_ROLE_NAME = "A".repeat(51);
    private static final int MAX_LENGTH = 50;

    @Test
    @DisplayName("of()で有効なロール名からインスタンスを生成できること")
    void ofWithValidValueShouldCreateInstance() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).isEqualTo(VALID_ROLE_NAME);
    }

    @Test
    @DisplayName("reBuild()でバリデーションをスキップしてインスタンスを生成できること")
    void reBuildShouldSkipValidationAndCreateInstance() {
        RoleName actualRoleName = RoleName.reBuild(VALID_ROLE_NAME);

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).isEqualTo(VALID_ROLE_NAME);
    }

    @Test
    @DisplayName("reBuild()で不正な値でもバリデーションをスキップしてインスタンスを生成できること")
    void reBuildWithInvalidValueShouldSkipValidation() {
        String invalidValue = FIFTY_ONE_CHAR_ROLE_NAME;

        RoleName actualRoleName = RoleName.reBuild(invalidValue);

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).isEqualTo(invalidValue);
    }

    @Test
    @DisplayName("getValue()でロール名の値を取得できること")
    void getValueShouldReturnRoleNameValue() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);

        String actualValue = actualRoleName.getValue();

        assertThat(actualValue).isEqualTo(VALID_ROLE_NAME);
    }

    @Test
    @DisplayName("of()でnullを渡すとDomainLayerErrorExceptionが発生すること")
    void ofWithNullShouldThrowException() {
        assertThatThrownBy(() -> RoleName.of(null))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("ロール名は必須項目です")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("of()で空文字を渡すとDomainLayerErrorExceptionが発生すること")
    void ofWithEmptyStringShouldThrowException() {
        assertThatThrownBy(() -> RoleName.of(""))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("ロール名は必須項目です")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("of()で空白文字を渡すとDomainLayerErrorExceptionが発生すること")
    void ofWithBlankStringShouldThrowException() {
        assertThatThrownBy(() -> RoleName.of("   "))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("ロール名は必須項目です")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("of()で50文字のロール名でインスタンスを生成できること")
    void ofWithFiftyCharactersShouldCreateInstance() {
        RoleName actualRoleName = RoleName.of(FIFTY_CHAR_ROLE_NAME);

        assertThat(actualRoleName).isNotNull();
        assertThat(actualRoleName.getValue()).hasSize(MAX_LENGTH);
    }

    @Test
    @DisplayName("of()で51文字以上のロール名を渡すとDomainLayerErrorExceptionが発生すること")
    void ofWithOverFiftyCharactersShouldThrowException() {
        assertThatThrownBy(() -> RoleName.of(FIFTY_ONE_CHAR_ROLE_NAME))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("ロール名は最大 50 文字以内で指定してください")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("同じ値を持つインスタンスがequalsでtrueを返すこと")
    void equalsWithSameValueShouldReturnTrue() {
        RoleName actualFirst = RoleName.of(VALID_ROLE_NAME);
        RoleName actualSecond = RoleName.of(VALID_ROLE_NAME);

        assertThat(actualFirst).isEqualTo(actualSecond);
    }

    @Test
    @DisplayName("同一インスタンスがequalsでtrueを返すこと")
    void equalsWithSameInstanceShouldReturnTrue() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);

        assertThat(actualRoleName).isEqualTo(actualRoleName);
    }

    @Test
    @DisplayName("異なる値を持つインスタンスがequalsでfalseを返すこと")
    void equalsWithDifferentValueShouldReturnFalse() {
        RoleName actualFirst = RoleName.of("ROLE_ADMIN");
        RoleName actualSecond = RoleName.of("ROLE_USER");

        assertThat(actualFirst).isNotEqualTo(actualSecond);
    }

    @Test
    @DisplayName("nullとの比較でfalseを返すこと")
    void equalsWithNullShouldReturnFalse() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);

        assertThat(actualRoleName).isNotEqualTo(null);
    }

    @Test
    @DisplayName("異なる型のオブジェクトとの比較でfalseを返すこと")
    void equalsWithDifferentTypeShouldReturnFalse() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);
        String differentTypeObject = VALID_ROLE_NAME;

        assertThat(actualRoleName).isNotEqualTo(differentTypeObject);
    }

    @Test
    @DisplayName("同じ値を持つインスタンスが同じhashCodeを返すこと")
    void hashCodeWithSameValueShouldBeEqual() {
        RoleName actualFirst = RoleName.of(VALID_ROLE_NAME);
        RoleName actualSecond = RoleName.of(VALID_ROLE_NAME);

        assertThat(actualFirst.hashCode()).isEqualTo(actualSecond.hashCode());
    }

    @Test
    @DisplayName("同一インスタンスが同じhashCodeを返すこと")
    void hashCodeWithSameInstanceShouldBeConsistent() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);
        int expectedHashCode = actualRoleName.hashCode();

        assertThat(actualRoleName.hashCode()).isEqualTo(expectedHashCode);
    }

    @Test
    @DisplayName("異なる値を持つインスタンスが異なるhashCodeを返すこと")
    void hashCodeWithDifferentValueShouldBeDifferent() {
        RoleName actualFirst = RoleName.of("ROLE_ADMIN");
        RoleName actualSecond = RoleName.of("ROLE_USER");

        assertThat(actualFirst.hashCode()).isNotEqualTo(actualSecond.hashCode());
    }

    @Test
    @DisplayName("toString()が期待される文字列形式を返すこと")
    void toStringShouldContainExpectedFields() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);

        String actualToString = actualRoleName.toString();

        assertThat(actualToString)
                .isNotNull()
                .contains("RoleName")
                .contains("value")
                .contains(VALID_ROLE_NAME);
    }

    @Test
    @DisplayName("toString()がnullを返さないこと")
    void toStringShouldNotReturnNull() {
        RoleName actualRoleName = RoleName.of(VALID_ROLE_NAME);

        String actualToString = actualRoleName.toString();

        assertThat(actualToString).isNotNull();
    }
}
