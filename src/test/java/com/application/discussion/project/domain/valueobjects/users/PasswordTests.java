package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Password 値オブジェクトのテスト")
class PasswordTests {

    private static final String VALID_PASSWORD = "Password123";
    private static final String MIN_LENGTH_PASSWORD = "Abcdefg123";
    private static final String MAX_LENGTH_PASSWORD = "A" + "a".repeat(252) + "1";
    private static final String ERROR_MESSAGE_REQUIRED = "パスワードは必須項目です";
    private static final String ERROR_MESSAGE_LENGTH = "パスワードは10文字以上255文字以下である必要があります";
    private static final String ERROR_MESSAGE_COMPLEXITY = "パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります";

    @Test
    @DisplayName("of_正常なパスワードでインスタンスが生成される")
    public void createPasswordWithValidPasswordReturnsInstance() {
        String expectedPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_最小長(10文字)のパスワードでインスタンスが生成される")
    public void createPasswordWithMinimumLengthReturnsInstance() {
        String expectedPassword = MIN_LENGTH_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
        assertThat(actualPassword.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_最大長(255文字)のパスワードでインスタンスが生成される")
    public void createPasswordWithMaximumLengthReturnsInstance() {
        String expectedPassword = MAX_LENGTH_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
        assertThat(actualPassword.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("of_null値が渡された場合に例外がスローされる")
    public void createPasswordWithNullThrowsException() {
        assertThatThrownBy(() -> Password.of(null))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_REQUIRED);
    }

    @Test
    @DisplayName("of_空文字列が渡された場合に例外がスローされる")
    public void createPasswordWithEmptyStringThrowsException() {
        String invalidPassword = "";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_REQUIRED);
    }

    @Test
    @DisplayName("of_最小長未満のパスワードの場合に例外がスローされる")
    public void createPasswordBelowMinLengthThrowsException() {
        String invalidPassword = "Pass123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_LENGTH);
    }

    @Test
    @DisplayName("of_最大長を超えるパスワードの場合に例外がスローされる")
    public void createPasswordExceedingMaxLengthThrowsException() {
        String invalidPassword = "A" + "a".repeat(254) + "1";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_LENGTH);
    }

    @Test
    @DisplayName("of_英小文字を含まないパスワードの場合に例外がスローされる")
    public void createPasswordWithoutLowercaseThrowsException() {
        String invalidPassword = "PASSWORD123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_COMPLEXITY);
    }

    @Test
    @DisplayName("of_英大文字を含まないパスワードの場合に例外がスローされる")
    public void createPasswordWithoutUppercaseThrowsException() {
        String invalidPassword = "password123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_COMPLEXITY);
    }

    @Test
    @DisplayName("of_数字を含まないパスワードの場合に例外がスローされる")
    public void createPasswordWithoutDigitThrowsException() {
        String invalidPassword = "PasswordOnly";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_COMPLEXITY);
    }

    @Test
    @DisplayName("of_特殊文字を含むパスワードの場合に例外がスローされる")
    public void createPasswordWithSpecialCharactersThrowsException() {
        String invalidPassword = "Password@123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_COMPLEXITY);
    }

    @Test
    @DisplayName("value_設定されたパスワードが正しく取得される")
    public void valueReturnsCorrectPassword() {
        String expectedPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長以下の場合にtrueを返す")
    public void isBelowMaxLengthReturnsTrueWhenPasswordIsBelowMaxLength() {
        String validPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長ちょうどの場合にtrueを返す")
    public void isBelowMaxLengthReturnsTrueWhenPasswordIsExactlyMaxLength() {
        String validPassword = MAX_LENGTH_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長以上の場合にtrueを返す")
    public void isAboveMinLengthReturnsTrueWhenPasswordIsAboveMinLength() {
        String validPassword = VALID_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長ちょうどの場合にtrueを返す")
    public void isAboveMinLengthReturnsTrueWhenPasswordIsExactlyMinLength() {
        String validPassword = MIN_LENGTH_PASSWORD;

        Password actualPassword = Password.of(validPassword);

        assertThat(actualPassword.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_数字が複数含まれるパスワードでインスタンスが生成される")
    public void createPasswordWithMultipleDigitsReturnsInstance() {
        String expectedPassword = "Password123456";

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_英大文字が複数含まれるパスワードでインスタンスが生成される")
    public void createPasswordWithMultipleUppercaseReturnsInstance() {
        String expectedPassword = "PASSword123";

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_英小文字が複数含まれるパスワードでインスタンスが生成される")
    public void createPasswordWithMultipleLowercaseReturnsInstance() {
        String expectedPassword = "Passwordddd123";

        Password actualPassword = Password.of(expectedPassword);

        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("of_スペースを含むパスワードの場合に例外がスローされる")
    public void createPasswordContainingSpaceThrowsException() {
        String invalidPassword = "Pass word123";

        assertThatThrownBy(() -> Password.of(invalidPassword))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining(ERROR_MESSAGE_COMPLEXITY);
    }
}
