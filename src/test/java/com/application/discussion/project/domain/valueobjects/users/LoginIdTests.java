package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("LoginId値オブジェクトのテスト")
class LoginIdTests {

    private static final String VALID_LOGIN_ID = "testuser123";
    private static final String MIN_LENGTH_LOGIN_ID = "test1234";
    private static final String MAX_LENGTH_LOGIN_ID = "a".repeat(255);
    private static final String ERROR_MESSAGE_REQUIRED = "ログインIDは必須項目です";
    private static final String ERROR_MESSAGE_LENGTH = "ログインIDは8文字以上255文字以下である必要があります";
    private static final String ERROR_MESSAGE_PATTERN = "ログインIDは英数字および._-のみ使用可能で、8文字以上255文字以下である必要があります";

    @Test
    @DisplayName("有効なログインIDで値オブジェクトが生成できる")
    public void createLoginIdWithValidIdReturnsLoginId() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(VALID_LOGIN_ID);
    }

    @Test
    @DisplayName("英数字とハイフン、ドット、アンダースコアを含むログインIDで生成できる")
    public void createLoginIdWithValidCharactersReturnsLoginId() {
        String validLoginIdWithSpecialChars = "test.user_123-abc";

        LoginId actualLoginId = LoginId.of(validLoginIdWithSpecialChars);

        assertThat(actualLoginId.value()).isEqualTo(validLoginIdWithSpecialChars);
    }

    @Test
    @DisplayName("nullのログインIDで例外がスローされる")
    public void createLoginIdWithNullThrowsException() {
        assertThatThrownBy(() -> LoginId.of(null))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_REQUIRED);
    }

    @Test
    @DisplayName("空文字のログインIDで例外がスローされる")
    public void createLoginIdWithEmptyStringThrowsException() {
        assertThatThrownBy(() -> LoginId.of(""))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_REQUIRED);
    }

    @Test
    @DisplayName("空白文字のログインIDで例外がスローされる")
    public void createLoginIdWithBlankStringThrowsException() {
        assertThatThrownBy(() -> LoginId.of("   "))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_REQUIRED);
    }

    @Test
    @DisplayName("7文字のログインIDで例外がスローされる")
    public void createLoginIdWithTooShortLengthThrowsException() {
        String tooShortLoginId = "test123";

        assertThatThrownBy(() -> LoginId.of(tooShortLoginId))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_LENGTH);
    }

    @Test
    @DisplayName("256文字のログインIDで例外がスローされる")
    public void createLoginIdWithTooLongLengthThrowsException() {
        String tooLongLoginId = "a".repeat(256);

        assertThatThrownBy(() -> LoginId.of(tooLongLoginId))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_LENGTH);
    }

    @Test
    @DisplayName("日本語を含むログインIDで例外がスローされる")
    public void createLoginIdWithJapaneseCharactersThrowsException() {
        String loginIdWithJapanese = "testユーザー123";

        assertThatThrownBy(() -> LoginId.of(loginIdWithJapanese))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_PATTERN);
    }

    @Test
    @DisplayName("スペースを含むログインIDで例外がスローされる")
    public void createLoginIdWithSpaceThrowsException() {
        String loginIdWithSpace = "test user123";

        assertThatThrownBy(() -> LoginId.of(loginIdWithSpace))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_PATTERN);
    }

    @Test
    @DisplayName("特殊文字を含むログインIDで例外がスローされる")
    public void createLoginIdWithInvalidSpecialCharacterThrowsException() {
        String loginIdWithSpecialChar = "test@user123";

        assertThatThrownBy(() -> LoginId.of(loginIdWithSpecialChar))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage(ERROR_MESSAGE_PATTERN);
    }

    @Test
    @DisplayName("最小長8文字のログインIDで値オブジェクトが生成できる")
    public void createLoginIdWithMinLengthReturnsLoginId() {
        LoginId actualLoginId = LoginId.of(MIN_LENGTH_LOGIN_ID);

        assertThat(actualLoginId.value()).isEqualTo(MIN_LENGTH_LOGIN_ID);
        assertThat(actualLoginId.value()).hasSize(8);
    }

    @Test
    @DisplayName("最大長255文字のログインIDで値オブジェクトが生成できる")
    public void createLoginIdWithMaxLengthReturnsLoginId() {
        LoginId actualLoginId = LoginId.of(MAX_LENGTH_LOGIN_ID);

        assertThat(actualLoginId.value()).isEqualTo(MAX_LENGTH_LOGIN_ID);
        assertThat(actualLoginId.value()).hasSize(255);
    }

    @Test
    @DisplayName("最大長以下の判定が正しく動作する")
    public void isBelowMaxLengthWithValidLoginIdReturnsTrue() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        assertThat(actualLoginId.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("最大長の場合も最大長以下の判定がtrueを返す")
    public void isBelowMaxLengthWithMaxLengthReturnsTrue() {
        LoginId actualLoginId = LoginId.of(MAX_LENGTH_LOGIN_ID);

        assertThat(actualLoginId.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("最小長以上の判定が正しく動作する")
    public void isAboveMinLengthWithValidLoginIdReturnsTrue() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        assertThat(actualLoginId.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("最小長の場合も最小長以上の判定がtrueを返す")
    public void isAboveMinLengthWithMinLengthReturnsTrue() {
        LoginId actualLoginId = LoginId.of(MIN_LENGTH_LOGIN_ID);

        assertThat(actualLoginId.isAboveMinLength()).isTrue();
    }
}
