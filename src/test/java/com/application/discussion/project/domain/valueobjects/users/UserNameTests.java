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

@DisplayName("UserName値オブジェクトのテスト")
class UserNameTests {

    @Nested
    @DisplayName("正常系テスト")
    class ValidCases {

        @Test
        @DisplayName("有効なユーザー名で値オブジェクトが生成できる")
        void of_ValidUserName_ReturnsUserNameInstance() {
            String expectedValue = "テストユーザー";

            UserName actualUserName = UserName.of(expectedValue);

            assertThat(actualUserName).isNotNull();
            assertThat(actualUserName.value()).isEqualTo(expectedValue);
        }

        @ParameterizedTest
        @DisplayName("様々な有効なユーザー名が生成できる")
        @ValueSource(strings = {
                "山田太郎",
                "John Doe",
                "ユーザー123",
                "test_user",
                "User-Name"
        })
        void of_VariousValidUserNames_ReturnsUserNameInstance(String expectedValue) {
            UserName actualUserName = UserName.of(expectedValue);

            assertThat(actualUserName.value()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("境界値: 1文字（最小長）のユーザー名が生成できる")
        void of_MinLengthUserName_ReturnsUserNameInstance() {
            String expectedValue = "あ";

            UserName actualUserName = UserName.of(expectedValue);

            assertThat(actualUserName.value()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("境界値: 255文字（最大長）のユーザー名が生成できる")
        void of_MaxLengthUserName_ReturnsUserNameInstance() {
            String expectedValue = "あ".repeat(255);

            UserName actualUserName = UserName.of(expectedValue);

            assertThat(actualUserName.value()).isEqualTo(expectedValue);
        }
    }

    @Nested
    @DisplayName("異常系テスト: 空値・null")
    class BlankCases {

        @ParameterizedTest
        @DisplayName("null/空文字の場合は例外がスローされる")
        @NullAndEmptySource
        void of_NullOrEmptyUserName_ThrowsException(String invalidValue) {
            assertThatThrownBy(() -> UserName.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("ユーザー名は必須項目です");
        }
    }

    @Nested
    @DisplayName("異常系テスト: 長さ制約違反")
    class LengthConstraintCases {

        @Test
        @DisplayName("256文字（最大長超過）の場合は例外がスローされる")
        void of_TooLongUserName_ThrowsException() {
            String invalidValue = "あ".repeat(256);

            assertThatThrownBy(() -> UserName.of(invalidValue))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessageContaining("1文字以上")
                    .hasMessageContaining("255文字以下");
        }
    }

    @Nested
    @DisplayName("メソッドテスト")
    class MethodTests {

        @Test
        @DisplayName("value()はユーザー名の文字列を返す")
        void value_ReturnsUserNameString() {
            String expectedValue = "テストユーザー";
            UserName userName = UserName.of(expectedValue);

            String actualValue = userName.value();

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("isBelowMaxLength()は最大長以下の場合trueを返す")
        void isBelowMaxLength_WhenBelowMax_ReturnsTrue() {
            UserName userName = UserName.of("テストユーザー");

            boolean actualResult = userName.isBelowMaxLength();

            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isBelowMaxLength()は最大長ちょうどの場合trueを返す")
        void isBelowMaxLength_WhenExactlyMax_ReturnsTrue() {
            UserName userName = UserName.of("a".repeat(255));

            boolean actualResult = userName.isBelowMaxLength();

            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isAboveMinLength()は最小長以上の場合trueを返す")
        void isAboveMinLength_WhenAboveMin_ReturnsTrue() {
            UserName userName = UserName.of("テストユーザー");

            boolean actualResult = userName.isAboveMinLength();

            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("isAboveMinLength()は最小長ちょうどの場合trueを返す")
        void isAboveMinLength_WhenExactlyMin_ReturnsTrue() {
            UserName userName = UserName.of("a");

            boolean actualResult = userName.isAboveMinLength();

            assertThat(actualResult).isTrue();
        }
    }
}
