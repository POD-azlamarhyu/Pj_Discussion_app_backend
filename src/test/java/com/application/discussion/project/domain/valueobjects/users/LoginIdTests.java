package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("LoginId値オブジェクトのテスト")
class LoginIdTests {

    private static final String VALID_LOGIN_ID = "testuser123";
    private static final String MIN_LENGTH_LOGIN_ID = "test1234";
    private static final String MAX_LENGTH_LOGIN_ID = "a".repeat(255);

    @Nested
    @DisplayName("正常系テスト")
    class ValidCases {

        @Test
        @DisplayName("有効なログインIDで値オブジェクトが生成できる")
        void createWithValidLoginId() {
            LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

            assertThat(actualLoginId).isNotNull();
            assertThat(actualLoginId.value()).isEqualTo(VALID_LOGIN_ID);
        }

        @Test
        @DisplayName("英数字とハイフン、ドット、アンダースコアを含むログインIDで生成できる")
        void createWithValidCharacters() {
            String validLoginIdWithSpecialChars = "test.user_123-abc";

            LoginId actualLoginId = LoginId.of(validLoginIdWithSpecialChars);

            assertThat(actualLoginId.value()).isEqualTo(validLoginIdWithSpecialChars);
        }
    }

    @Nested
    @DisplayName("異常系テスト - null・空文字")
    class NullAndEmptyCases {

        @Test
        @DisplayName("nullのログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdIsNull() {
            assertThatThrownBy(() -> LoginId.of(null))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは必須項目です");
        }

        @Test
        @DisplayName("空文字のログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdIsEmpty() {
            assertThatThrownBy(() -> LoginId.of(""))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは必須項目です");
        }

        @Test
        @DisplayName("空白文字のログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdIsBlank() {
            assertThatThrownBy(() -> LoginId.of("   "))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは必須項目です");
        }
    }

    @Nested
    @DisplayName("異常系テスト - 長さ不正")
    class LengthValidationCases {

        @Test
        @DisplayName("7文字のログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdIsTooShort() {
            String tooShortLoginId = "test123";

            assertThatThrownBy(() -> LoginId.of(tooShortLoginId))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは8文字以上255文字以下である必要があります");
        }

        @Test
        @DisplayName("256文字のログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdIsTooLong() {
            String tooLongLoginId = "a".repeat(256);

            assertThatThrownBy(() -> LoginId.of(tooLongLoginId))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは8文字以上255文字以下である必要があります");
        }
    }

    @Nested
    @DisplayName("異常系テスト - パターン不正")
    class PatternValidationCases {

        @Test
        @DisplayName("日本語を含むログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdContainsJapanese() {
            String loginIdWithJapanese = "testユーザー123";

            assertThatThrownBy(() -> LoginId.of(loginIdWithJapanese))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります");
        }

        @Test
        @DisplayName("スペースを含むログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdContainsSpace() {
            String loginIdWithSpace = "test user123";

            assertThatThrownBy(() -> LoginId.of(loginIdWithSpace))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります");
        }

        @Test
        @DisplayName("特殊文字を含むログインIDで例外がスローされる")
        void throwExceptionWhenLoginIdContainsInvalidSpecialChar() {
            String loginIdWithSpecialChar = "test@user123";

            assertThatThrownBy(() -> LoginId.of(loginIdWithSpecialChar))
                    .isInstanceOf(DomainLayerErrorException.class)
                    .hasMessage("ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります");
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class BoundaryValueCases {

        @Test
        @DisplayName("最小長8文字のログインIDで値オブジェクトが生成できる")
        void createWithMinLengthLoginId() {
            LoginId actualLoginId = LoginId.of(MIN_LENGTH_LOGIN_ID);

            assertThat(actualLoginId.value()).isEqualTo(MIN_LENGTH_LOGIN_ID);
            assertThat(actualLoginId.value()).hasSize(8);
        }

        @Test
        @DisplayName("最大長255文字のログインIDで値オブジェクトが生成できる")
        void createWithMaxLengthLoginId() {
            LoginId actualLoginId = LoginId.of(MAX_LENGTH_LOGIN_ID);

            assertThat(actualLoginId.value()).isEqualTo(MAX_LENGTH_LOGIN_ID);
            assertThat(actualLoginId.value()).hasSize(255);
        }
    }

    @Nested
    @DisplayName("ヘルパーメソッドのテスト")
    class HelperMethodCases {

        @Test
        @DisplayName("最大長以下の判定が正しく動作する")
        void isBelowMaxLengthReturnsTrue() {
            LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

            assertThat(actualLoginId.isBelowMaxLength()).isTrue();
        }

        @Test
        @DisplayName("最大長の場合も最大長以下の判定がtrueを返す")
        void isBelowMaxLengthReturnsTrueForMaxLength() {
            LoginId actualLoginId = LoginId.of(MAX_LENGTH_LOGIN_ID);

            assertThat(actualLoginId.isBelowMaxLength()).isTrue();
        }

        @Test
        @DisplayName("最小長以上の判定が正しく動作する")
        void isAboveMinLengthReturnsTrue() {
            LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

            assertThat(actualLoginId.isAboveMinLength()).isTrue();
        }

        @Test
        @DisplayName("最小長の場合も最小長以上の判定がtrueを返す")
        void isAboveMinLengthReturnsTrueForMinLength() {
            LoginId actualLoginId = LoginId.of(MIN_LENGTH_LOGIN_ID);

            assertThat(actualLoginId.isAboveMinLength()).isTrue();
        }
    }
}
