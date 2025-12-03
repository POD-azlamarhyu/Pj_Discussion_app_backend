package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("LoginId値オブジェクトのテスト")
class LoginIdTests {

    @Nested
    @DisplayName("正常系テスト")
    class ValidCases {

        @Test
        @DisplayName("有効なログインIDで値オブジェクトが生成できる")
        void of_ValidLoginId_ReturnsLoginIdInstance() {
            // Arrange
            String expectedValue = "testuser123";

            // Act
            LoginId actualLoginId = LoginId.of(expectedValue);

            // Assert
            assertThat(actualLoginId).isNotNull();
            assertThat(actualLoginId.value()).isEqualTo(expectedValue);
        }

        @ParameterizedTest
        @DisplayName("許可された特殊文字を含むログインIDが生成できる")
        @ValueSource(strings = {"user.name", "user-name", "user_name", "user.name-test_123"})
        void of_LoginIdWithAllowedSpecialChars_ReturnsLoginIdInstance(String expectedValue) {
            // Act
            LoginId actualLoginId = LoginId.of(expectedValue);

            // Assert
            assertThat(actualLoginId.value()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("境界値: 8文字（最小長）のログインIDが生成できる")
        void of_MinLengthLoginId_ReturnsLoginIdInstance() {
            // Arrange
            String expectedValue = "abcdefgh"; // 8文字

            // Act
            LoginId actualLoginId = LoginId.of(expectedValue);

            // Assert
            assertThat(actualLoginId.value()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("境界値: 255文字（最大長）のログインIDが生成できる")
        void of_MaxLengthLoginId_ReturnsLoginIdInstance() {
            // Arrange
            String expectedValue = "a".repeat(255); // 255文字

            // Act
            LoginId actualLoginId = LoginId.of(expectedValue);

            // Assert
            assertThat(actualLoginId.value()).isEqualTo(expectedValue);
        }
    }

    @Nested
    @DisplayName("異常系テスト: 空値・null")
    class BlankCases {

        @ParameterizedTest
        @DisplayName("null/空文字/空白のみの場合は例外がスローされる")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        void of_BlankLoginId_ThrowsException(String invalidValue) {
            // Act & Assert
            assertThatThrownBy(() -> LoginId.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("ログインIDは必須項目です");
        }
    }

    @Nested
    @DisplayName("異常系テスト: 長さ制約違反")
    class LengthConstraintCases {

        @Test
        @DisplayName("7文字（最小長未満）の場合は例外がスローされる")
        void of_TooShortLoginId_ThrowsException() {
            // Arrange
            String invalidValue = "abcdefg"; // 7文字

            // Act & Assert
            assertThatThrownBy(() -> LoginId.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("8文字以上")
                    .hasMessageContaining("255文字以下");
        }

        @Test
        @DisplayName("256文字（最大長超過）の場合は例外がスローされる")
        void of_TooLongLoginId_ThrowsException() {
            // Arrange
            String invalidValue = "a".repeat(256); // 256文字

            // Act & Assert
            assertThatThrownBy(() -> LoginId.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("8文字以上")
                    .hasMessageContaining("255文字以下");
        }
    }

    @Nested
    @DisplayName("異常系テスト: パターン違反")
    class PatternConstraintCases {

        @ParameterizedTest
        @DisplayName("不正な文字を含む場合は例外がスローされる")
        @ValueSource(strings = {
                "user@name",      // @は不正
                "user#name",      // #は不正
                "user name",      // スペースは不正
                "ユーザー名てすと",  // 日本語は不正
                "user!name",      // !は不正
                "user$name123"    // $は不正
        })
        void of_LoginIdWithInvalidChars_ThrowsException(String invalidValue) {
            // Act & Assert
            assertThatThrownBy(() -> LoginId.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("英数字および._-のみ使用可能");
        }
    }

    @Nested
    @DisplayName("メソッドテスト")
    class MethodTests {

        @Test
        @DisplayName("value()はログインIDの文字列を返す")
        void value_ReturnsLoginIdString() {
            // Arrange
            String expectedValue = "testuser123";
            LoginId loginId = LoginId.of(expectedValue);

            // Act
            String actualValue = loginId.value();

            // Assert
            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("isBelowMaxLength()は最大長以下の場合trueを返す")
        void isBelowMaxLength_WhenBelowMax_ReturnsTrue() {
            // Arrange
            LoginId loginId = LoginId.of("testuser123");

            // Act
            boolean actualResult = loginId.isBelowMaxLength();

            // Assert
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isBelowMaxLength()は最大長ちょうどの場合trueを返す")
        void isBelowMaxLength_WhenExactlyMax_ReturnsTrue() {
            // Arrange
            LoginId loginId = LoginId.of("a".repeat(255));

            // Act
            boolean actualResult = loginId.isBelowMaxLength();

            // Assert
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isAboveMinLength()は最小長以上の場合trueを返す")
        void isAboveMinLength_WhenAboveMin_ReturnsTrue() {
            // Arrange
            LoginId loginId = LoginId.of("testuser123");

            // Act
            boolean actualResult = loginId.isAboveMinLength();

            // Assert
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isAboveMinLength()は最小長ちょうどの場合trueを返す")
        void isAboveMinLength_WhenExactlyMin_ReturnsTrue() {
            // Arrange
            LoginId loginId = LoginId.of("abcdefgh"); // 8文字

            // Act
            boolean actualResult = loginId.isAboveMinLength();

            // Assert
            assertThat(actualResult).isTrue();
        }
    }
}
