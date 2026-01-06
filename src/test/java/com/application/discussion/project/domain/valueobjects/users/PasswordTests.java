package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Password 値オブジェクトのテスト")
class PasswordTests {

    private static final String VALID_PASSWORD = "Password123";
    private static final String MIN_LENGTH_PASSWORD = "Abcdefg123";
    private static final String MAX_LENGTH_PASSWORD = "A" + "a".repeat(252) + "1";
    private static final String ERROR_MESSAGE_REQUIRED = "パスワードは必須項目です";
    private static final String ERROR_MESSAGE_LENGTH = "パスワードは10文字以上255文字以下である必要があります";
    private static final String ERROR_MESSAGE_COMPLEXITY = "パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります";
    private static final String BCRYPT_HASHED_PASSWORD = "$2a$10$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setUp() {
        mockPasswordEncoder = mock(PasswordEncoder.class);
    }

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

    @Test
    @DisplayName("reBuild_有効なパスワードでバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildWithValidPasswordSkipsValidation() {
        String expectedPassword = VALID_PASSWORD;

        Password actualPassword = Password.reBuild(expectedPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("reBuild_nullでもバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildSkipsValidationWithNullValue() {
        Password actualPassword = Password.reBuild(null);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isNull();
    }

    @Test
    @DisplayName("reBuild_空文字でもバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildSkipsValidationWithEmptyString() {
        String emptyPassword = "";

        Password actualPassword = Password.reBuild(emptyPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(emptyPassword);
    }

    @Test
    @DisplayName("reBuild_最小長未満でもバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildSkipsValidationWithTooShortPassword() {
        String tooShortPassword = "Pass1";

        Password actualPassword = Password.reBuild(tooShortPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(tooShortPassword);
    }

    @Test
    @DisplayName("reBuild_最大長を超えてもバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildSkipsValidationWithTooLongPassword() {
        String tooLongPassword = "A" + "a".repeat(254) + "1";

        Password actualPassword = Password.reBuild(tooLongPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(tooLongPassword);
    }

    @Test
    @DisplayName("reBuild_複雑性要件を満たさなくてもバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildSkipsValidationWithInvalidComplexity() {
        String invalidComplexityPassword = "password";

        Password actualPassword = Password.reBuild(invalidComplexityPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(invalidComplexityPassword);
    }

    @Test
    @DisplayName("reBuild_特殊文字を含んでもバリデーションをスキップしてインスタンスを生成できる")
    public void reBuildSkipsValidationWithSpecialCharacters() {
        String passwordWithSpecialChars = "Password@123!";

        Password actualPassword = Password.reBuild(passwordWithSpecialChars);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(passwordWithSpecialChars);
    }

    @Test
    @DisplayName("reBuildHashed_BCrypt形式のハッシュ化パスワードでインスタンスを生成できる")
    public void reBuildHashedWithBCryptHashReturnsInstance() {
        String expectedHashedPassword = BCRYPT_HASHED_PASSWORD;

        Password actualPassword = Password.reBuildHashed(expectedHashedPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(expectedHashedPassword);
    }

    @Test
    @DisplayName("reBuildHashed_nullでもインスタンスを生成できる")
    public void reBuildHashedWithNullReturnsInstance() {
        Password actualPassword = Password.reBuildHashed(null);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isNull();
    }

    @Test
    @DisplayName("reBuildHashed_空文字でもインスタンスを生成できる")
    public void reBuildHashedWithEmptyStringReturnsInstance() {
        String emptyPassword = "";

        Password actualPassword = Password.reBuildHashed(emptyPassword);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(emptyPassword);
    }

    @Test
    @DisplayName("reBuildHashed_任意の文字列でインスタンスを生成できる")
    public void reBuildHashedWithArbitraryStringReturnsInstance() {
        String arbitraryString = "arbitrary_hashed_password_string";

        Password actualPassword = Password.reBuildHashed(arbitraryString);

        assertThat(actualPassword).isNotNull();
        assertThat(actualPassword.value()).isEqualTo(arbitraryString);
    }

    @Test
    @DisplayName("isHashed_一致するパスワードでtrueを返す")
    public void isHashedReturnsTrueWhenPasswordMatches() {
        String rawPassword = VALID_PASSWORD;
        String hashedPassword = BCRYPT_HASHED_PASSWORD;
        Password actualPassword = Password.reBuildHashed(hashedPassword);
        when(mockPasswordEncoder.matches(rawPassword, hashedPassword)).thenReturn(Boolean.TRUE);

        Boolean actualResult = actualPassword.isHashed(rawPassword, mockPasswordEncoder);

        assertThat(actualResult).isTrue();
        verify(mockPasswordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }

    @Test
    @DisplayName("isHashed_一致しないパスワードでfalseを返す")
    public void isHashedReturnsFalseWhenPasswordDoesNotMatch() {
        String rawPassword = VALID_PASSWORD;
        String hashedPassword = BCRYPT_HASHED_PASSWORD;
        Password actualPassword = Password.reBuildHashed(hashedPassword);
        when(mockPasswordEncoder.matches(rawPassword, hashedPassword)).thenReturn(Boolean.FALSE);

        Boolean actualResult = actualPassword.isHashed(rawPassword, mockPasswordEncoder);

        assertThat(actualResult).isFalse();
        verify(mockPasswordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }

    @Test
    @DisplayName("isHashed_ハッシュ化パスワードがnullの場合にfalseを返す")
    public void isHashedReturnsFalseWhenHashedPasswordIsNull() {
        String rawPassword = VALID_PASSWORD;
        Password actualPassword = Password.reBuildHashed(null);

        Boolean actualResult = actualPassword.isHashed(rawPassword, mockPasswordEncoder);

        assertThat(actualResult).isFalse();
    }

    @Test
    @DisplayName("isHashed_ハッシュ化パスワードが空文字の場合にfalseを返す")
    public void isHashedReturnsFalseWhenHashedPasswordIsEmpty() {
        String rawPassword = VALID_PASSWORD;
        Password actualPassword = Password.reBuildHashed("");

        Boolean actualResult = actualPassword.isHashed(rawPassword, mockPasswordEncoder);

        assertThat(actualResult).isFalse();
    }

    @Test
    @DisplayName("isHashed_平文パスワードがnullの場合にfalseを返す")
    public void isHashedReturnsFalseWhenRawPasswordIsNull() {
        String hashedPassword = BCRYPT_HASHED_PASSWORD;
        Password actualPassword = Password.reBuildHashed(hashedPassword);

        Boolean actualResult = actualPassword.isHashed(null, mockPasswordEncoder);

        assertThat(actualResult).isFalse();
    }

    @Test
    @DisplayName("isHashed_平文パスワードが空文字の場合にfalseを返す")
    public void isHashedReturnsFalseWhenRawPasswordIsEmpty() {
        String hashedPassword = BCRYPT_HASHED_PASSWORD;
        Password actualPassword = Password.reBuildHashed(hashedPassword);

        Boolean actualResult = actualPassword.isHashed("", mockPasswordEncoder);

        assertThat(actualResult).isFalse();
    }

    @Test
    @DisplayName("isHashed_両方のパスワードがnullの場合にfalseを返す")
    public void isHashedReturnsFalseWhenBothPasswordsAreNull() {
        Password actualPassword = Password.reBuildHashed(null);

        Boolean actualResult = actualPassword.isHashed(null, mockPasswordEncoder);

        assertThat(actualResult).isFalse();
    }

    @Test
    @DisplayName("isHashed_PasswordEncoderが正しく呼び出されること")
    public void isHashedCallsPasswordEncoderCorrectly() {
        String rawPassword = "TestPass123";
        String hashedPassword = "$2a$10$test_hashed_password";
        Password actualPassword = Password.reBuildHashed(hashedPassword);
        when(mockPasswordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);

        actualPassword.isHashed(rawPassword, mockPasswordEncoder);

        verify(mockPasswordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }
}
