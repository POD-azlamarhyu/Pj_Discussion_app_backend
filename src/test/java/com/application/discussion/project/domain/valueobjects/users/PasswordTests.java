package com.application.discussion.project.domain.valueobjects.users;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Password 値オブジェクトのテスト")
class PasswordTests {

    private static final String VALID_PASSWORD = "Password123";
    private static final String MIN_LENGTH_PASSWORD = "Abcdefg123";
    private static final String MAX_LENGTH_PASSWORD = "A" + "a".repeat(252) + "1";

    @Test
    @DisplayName("of_正常なパスワードでインスタンスが生成される")
    void ofCreatesInstanceWithValidPassword() {
        String expectedPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_最小長(10文字)のパスワードでインスタンスが生成される")
    void ofCreatesInstanceWithMinimumLengthPassword() {
        String expectedPassword = MIN_LENGTH_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
        assertThat(actualPassword.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_最大長(255文字)のパスワードでインスタンスが生成される")
    void ofCreatesInstanceWithMaximumLengthPassword() {
        String expectedPassword = MAX_LENGTH_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
        assertThat(actualPassword.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("of_null値が渡された場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordIsNull() {
        assertThatThrownBy(() -> Password.of(null))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは必須項目です");
    }

    @Test
    @DisplayName("of_空文字列が渡された場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordIsEmpty() {
        String invalidPassword = "";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは必須項目です");
    }

    @Test
    @DisplayName("of_最小長未満のパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordBelowMinLength() {
        String invalidPassword = "Pass123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは10文字以上255文字以下である必要があります");
    }

    @Test
    @DisplayName("of_最大長を超えるパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordExceedsMaxLength() {
        String invalidPassword = "A" + "a".repeat(254) + "1";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは10文字以上255文字以下である必要があります");
    }

    @Test
    @DisplayName("of_英小文字を含まないパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordWithoutLowercase() {
        String invalidPassword = "PASSWORD123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }

    @Test
    @DisplayName("of_英大文字を含まないパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordWithoutUppercase() {
        String invalidPassword = "password123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }

    @Test
    @DisplayName("of_数字を含まないパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordWithoutDigit() {
        String invalidPassword = "PasswordOnly";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }

    @Test
    @DisplayName("of_特殊文字を含むパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordWithSpecialCharacters() {
        String invalidPassword = "Password@123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }

    @Test
    @DisplayName("value_設定されたパスワードが正しく取得される")
    void valueReturnsCorrectPassword() {
        String expectedPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長以下の場合にtrueを返す")
    void isBelowMaxLengthReturnsTrueWhenPasswordIsBelowMaxLength() {
        String validPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長ちょうどの場合にtrueを返す")
    void isBelowMaxLengthReturnsTrueWhenPasswordIsExactlyMaxLength() {
        String validPassword = MAX_LENGTH_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長以上の場合にtrueを返す")
    void isAboveMinLengthReturnsTrueWhenPasswordIsAboveMinLength() {
        String validPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長ちょうどの場合にtrueを返す")
    void isAboveMinLengthReturnsTrueWhenPasswordIsExactlyMinLength() {
        String validPassword = MIN_LENGTH_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_数字が複数含まれるパスワードでインスタンスが生成される")
    void ofCreatesInstanceWithMultipleDigits() {
        String expectedPassword = "Password123456";

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_英大文字が複数含まれるパスワードでインスタンスが生成される")
    void ofCreatesInstanceWithMultipleUppercase() {
        String expectedPassword = "PASSword123";

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_英小文字が複数含まれるパスワードでインスタンスが生成される")
    void ofCreatesInstanceWithMultipleLowercase() {
        String expectedPassword = "Passwordddd123";

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_スペースを含むパスワードの場合に例外がスローされる")
    void ofThrowsExceptionWhenPasswordContainsSpace() {
        String invalidPassword = "Pass word123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }
}
