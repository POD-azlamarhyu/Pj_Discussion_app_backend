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

    @Test
    @DisplayName("reBuild()で有効なログインIDから値オブジェクトを再構築できる")
    public void reBuildWithValidLoginIdReturnsLoginId() {
        LoginId actualLoginId = LoginId.reBuild(VALID_LOGIN_ID);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(VALID_LOGIN_ID);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされnullでも値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithNullValue() {
        LoginId actualLoginId = LoginId.reBuild(null);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isNull();
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ空文字でも値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithEmptyString() {
        String emptyLoginId = "";

        LoginId actualLoginId = LoginId.reBuild(emptyLoginId);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(emptyLoginId);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ7文字以下でも値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithTooShortLength() {
        String tooShortLoginId = "test123";

        LoginId actualLoginId = LoginId.reBuild(tooShortLoginId);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(tooShortLoginId);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ256文字以上でも値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithTooLongLength() {
        String tooLongLoginId = "a".repeat(256);

        LoginId actualLoginId = LoginId.reBuild(tooLongLoginId);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(tooLongLoginId);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ不正な文字でも値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithInvalidCharacters() {
        String invalidLoginId = "test@user#123";

        LoginId actualLoginId = LoginId.reBuild(invalidLoginId);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(invalidLoginId);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ日本語でも値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithJapaneseCharacters() {
        String japaneseLoginId = "テストユーザー";

        LoginId actualLoginId = LoginId.reBuild(japaneseLoginId);

        assertThat(actualLoginId).isNotNull();
        assertThat(actualLoginId.value()).isEqualTo(japaneseLoginId);
    }

    @Test
    @DisplayName("toString()がログインIDの文字列表現を返す")
    public void toStringReturnsLoginIdString() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        String actualToString = actualLoginId.toString();

        assertThat(actualToString).isEqualTo(VALID_LOGIN_ID);
    }

    @Test
    @DisplayName("toString()とvalue()が同じ値を返す")
    public void toStringAndValueReturnSameValue() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        assertThat(actualLoginId.toString()).isEqualTo(actualLoginId.value());
    }

    @Test
    @DisplayName("toString()が特殊文字を含むログインIDを正しく返す")
    public void toStringReturnsLoginIdWithSpecialCharacters() {
        String loginIdWithSpecialChars = "test.user_123-abc";
        LoginId actualLoginId = LoginId.of(loginIdWithSpecialChars);

        String actualToString = actualLoginId.toString();

        assertThat(actualToString).isEqualTo(loginIdWithSpecialChars);
    }

    @Test
    @DisplayName("toString()が最小長のログインIDを正しく返す")
    public void toStringReturnsMinLengthLoginId() {
        LoginId actualLoginId = LoginId.of(MIN_LENGTH_LOGIN_ID);

        String actualToString = actualLoginId.toString();

        assertThat(actualToString)
                .isEqualTo(MIN_LENGTH_LOGIN_ID)
                .hasSize(8);
    }

    @Test
    @DisplayName("toString()が最大長のログインIDを正しく返す")
    public void toStringReturnsMaxLengthLoginId() {
        LoginId actualLoginId = LoginId.of(MAX_LENGTH_LOGIN_ID);

        String actualToString = actualLoginId.toString();

        assertThat(actualToString)
                .isEqualTo(MAX_LENGTH_LOGIN_ID)
                .hasSize(255);
    }

    @Test
    @DisplayName("value()が正しいログインIDの値を返す")
    public void valueReturnsCorrectLoginIdValue() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        String actualValue = actualLoginId.value();

        assertThat(actualValue).isEqualTo(VALID_LOGIN_ID);
    }

    @Test
    @DisplayName("value()とtoString()が一貫性を持つ")
    public void valueAndToStringAreConsistent() {
        LoginId actualLoginId = LoginId.of(VALID_LOGIN_ID);

        assertThat(actualLoginId.value())
                .isEqualTo(actualLoginId.toString())
                .isEqualTo(VALID_LOGIN_ID);
    }
}
