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

@DisplayName("Password値オブジェクトのテスト")
class PasswordTests {

    @Nested
    @DisplayName("正常系テスト")
    class ValidCases {

        @Test
        @DisplayName("有効なパスワードで値オブジェクトが生成できる")
        void of_ValidPassword_ReturnsPasswordInstance() {
            
            String expectedValue = "Password123";

            
            Password actualPassword = Password.of(expectedValue);

            
            assertThat(actualPassword).isNotNull();
            assertThat(actualPassword.value()).isEqualTo(expectedValue);
        }

        @ParameterizedTest
        @DisplayName("大文字・小文字・数字を含む様々なパスワードが生成できる")
        @ValueSource(strings = {
                "Abcdefgh12",
                "UPPERCASE1lowercase",
                "Test123456",
                "aB1cD2eF3g"
        })
        void of_VariousValidPasswords_ReturnsPasswordInstance(String expectedValue) {
            
            Password actualPassword = Password.of(expectedValue);

            
            assertThat(actualPassword.value()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("境界値: 10文字（最小長）のパスワードが生成できる")
        void of_MinLengthPassword_ReturnsPasswordInstance() {
            
            String expectedValue = "Abcdefgh12"; // 10文字

            
            Password actualPassword = Password.of(expectedValue);

            
            assertThat(actualPassword.value()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("境界値: 255文字（最大長）のパスワードが生成できる")
        void of_MaxLengthPassword_ReturnsPasswordInstance() {
            
            String expectedValue = "Aa1" + "a".repeat(252); // 255文字（大文字・小文字・数字を含む）

            
            Password actualPassword = Password.of(expectedValue);

            
            assertThat(actualPassword.value()).isEqualTo(expectedValue);
        }
    }

    @Nested
    @DisplayName("異常系テスト: 空値・null")
    class BlankCases {

        @ParameterizedTest
        @DisplayName("null/空文字の場合は例外がスローされる")
        @NullAndEmptySource
        void of_NullOrEmptyPassword_ThrowsException(String invalidValue) {
            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("パスワードは必須項目です");
        }
    }

    @Nested
    @DisplayName("異常系テスト: 長さ制約違反")
    class LengthConstraintCases {

        @Test
        @DisplayName("9文字（最小長未満）の場合は例外がスローされる")
        void of_TooShortPassword_ThrowsException() {
            
            String invalidValue = "Abcdefg12"; // 9文字

            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("10文字以上")
                    .hasMessageContaining("255文字以下");
        }

        @Test
        @DisplayName("256文字（最大長超過）の場合は例外がスローされる")
        void of_TooLongPassword_ThrowsException() {
            
            String invalidValue = "Aa1" + "a".repeat(253); // 256文字

            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("10文字以上")
                    .hasMessageContaining("255文字以下");
        }
    }

    @Nested
    @DisplayName("異常系テスト: パターン違反")
    class PatternConstraintCases {

        @Test
        @DisplayName("大文字を含まない場合は例外がスローされる")
        void of_PasswordWithoutUppercase_ThrowsException() {
            
            String invalidValue = "abcdefgh12"; // 大文字なし

            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("英大文字、英小文字、数字をそれぞれ1文字以上");
        }

        @Test
        @DisplayName("小文字を含まない場合は例外がスローされる")
        void of_PasswordWithoutLowercase_ThrowsException() {
            
            String invalidValue = "ABCDEFGH12"; // 小文字なし

            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("英大文字、英小文字、数字をそれぞれ1文字以上");
        }

        @Test
        @DisplayName("数字を含まない場合は例外がスローされる")
        void of_PasswordWithoutDigit_ThrowsException() {
            
            String invalidValue = "Abcdefghij"; // 数字なし

            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("英大文字、英小文字、数字をそれぞれ1文字以上");
        }

        @ParameterizedTest
        @DisplayName("特殊文字を含む場合は例外がスローされる")
        @ValueSource(strings = {
                "Password1!",
                "Password1@",
                "Password1#",
                "Password1$",
                "Password1%"
        })
        void of_PasswordWithSpecialChars_ThrowsException(String invalidValue) {
            
            assertThatThrownBy(() -> Password.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("英大文字、英小文字、数字をそれぞれ1文字以上");
        }
    }

    @Nested
    @DisplayName("メソッドテスト")
    class MethodTests {

        @Test
        @DisplayName("value()はパスワードの文字列を返す")
        void value_ReturnsPasswordString() {
            
            String expectedValue = "Password123";
            Password password = Password.of(expectedValue);

            
            String actualValue = password.value();

            
            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("isBelowMaxLength()は最大長以下の場合trueを返す")
        void isBelowMaxLength_WhenBelowMax_ReturnsTrue() {
            
            Password password = Password.of("Password123");

            
            boolean actualResult = password.isBelowMaxLength();

            
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isBelowMaxLength()は最大長ちょうどの場合trueを返す")
        void isBelowMaxLength_WhenExactlyMax_ReturnsTrue() {
            
            Password password = Password.of("Aa1" + "a".repeat(252)); // 255文字

            
            boolean actualResult = password.isBelowMaxLength();

            
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isAboveMinLength()は最小長以上の場合trueを返す")
        void isAboveMinLength_WhenAboveMin_ReturnsTrue() {
            
            Password password = Password.of("Password123");

            
            boolean actualResult = password.isAboveMinLength();

            
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isAboveMinLength()は最小長ちょうどの場合trueを返す")
        void isAboveMinLength_WhenExactlyMin_ReturnsTrue() {
            
            Password password = Password.of("Abcdefgh12"); // 10文字

            
            boolean actualResult = password.isAboveMinLength();

            
            assertThat(actualResult).isTrue();
        }
    }
}
