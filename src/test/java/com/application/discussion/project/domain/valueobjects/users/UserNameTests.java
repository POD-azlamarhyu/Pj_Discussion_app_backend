package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("UserName 値オブジェクトのテスト")
class UserNameTests {

    private static final String VALID_USER_NAME = "testuser";
    private static final String MIN_LENGTH_USER_NAME = "a";
    private static final String MAX_LENGTH_USER_NAME = "a".repeat(255);
    private static final String OVER_MAX_LENGTH_USER_NAME = "a".repeat(256);
    private static final String JAPANESE_USER_NAME = "テストユーザー";
    private static final String SPECIAL_CHARACTERS_USER_NAME = "test_user-123";
    private static final String WITH_SPACES_USER_NAME = "test user";

    @Test
    @DisplayName("of_正常なユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithValidUserName() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(VALID_USER_NAME);
    }

    @Test
    @DisplayName("of_最小長(1文字)のユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithMinimumLengthUserName() {
        UserName actualUserName = UserName.of(MIN_LENGTH_USER_NAME);

        assertThat(actualUserName.value()).isEqualTo(MIN_LENGTH_USER_NAME);
        assertThat(actualUserName.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_最大長(255文字)のユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithMaximumLengthUserName() {
        UserName actualUserName = UserName.of(MAX_LENGTH_USER_NAME);

        assertThat(actualUserName.value()).isEqualTo(MAX_LENGTH_USER_NAME);
        assertThat(actualUserName.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("of_null値が渡された場合に例外がスローされる")
    void ofThrowsExceptionWhenUserNameIsNull() {
        assertThatThrownBy(() -> UserName.of(null))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("of_空文字列が渡された場合に例外がスローされる")
    void ofThrowsExceptionWhenUserNameIsEmpty() {
        assertThatThrownBy(() -> UserName.of(""))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("of_最大長を超えるユーザー名の場合に例外がスローされる")
    void ofThrowsExceptionWhenUserNameExceedsMaxLength() {
        assertThatThrownBy(() -> UserName.of(OVER_MAX_LENGTH_USER_NAME))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("ユーザー名は1文字以上255文字以下である必要があります");
    }

    @Test
    @DisplayName("value_設定されたユーザー名が正しく取得される")
    void valueReturnsCorrectUserName() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        assertThat(actualUserName.value()).isEqualTo(VALID_USER_NAME);
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長以下の場合にtrueを返す")
    void isBelowMaxLengthReturnsTrueWhenUserNameIsBelowMaxLength() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        assertThat(actualUserName.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長ちょうどの場合にtrueを返す")
    void isBelowMaxLengthReturnsTrueWhenUserNameIsExactlyMaxLength() {
        UserName actualUserName = UserName.of(MAX_LENGTH_USER_NAME);

        assertThat(actualUserName.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長以上の場合にtrueを返す")
    void isAboveMinLengthReturnsTrueWhenUserNameIsAboveMinLength() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        assertThat(actualUserName.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長ちょうどの場合にtrueを返す")
    void isAboveMinLengthReturnsTrueWhenUserNameIsExactlyMinLength() {
        UserName actualUserName = UserName.of(MIN_LENGTH_USER_NAME);

        assertThat(actualUserName.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_日本語のユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithJapaneseUserName() {
        UserName actualUserName = UserName.of(JAPANESE_USER_NAME);

        assertThat(actualUserName.value()).isEqualTo(JAPANESE_USER_NAME);
    }

    @Test
    @DisplayName("of_特殊文字を含むユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithSpecialCharactersUserName() {
        UserName actualUserName = UserName.of(SPECIAL_CHARACTERS_USER_NAME);

        assertThat(actualUserName.value()).isEqualTo(SPECIAL_CHARACTERS_USER_NAME);
    }

    @Test
    @DisplayName("of_スペースを含むユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithSpacesUserName() {
        UserName actualUserName = UserName.of(WITH_SPACES_USER_NAME);

        assertThat(actualUserName.value()).isEqualTo(WITH_SPACES_USER_NAME);
    }

    @Test
    @DisplayName("reBuild_有効なユーザー名でバリデーションをスキップしてインスタンスを生成できる")
    void reBuildWithValidUserNameSkipsValidation() {
        UserName actualUserName = UserName.reBuild(VALID_USER_NAME);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(VALID_USER_NAME);
    }

    @Test
    @DisplayName("reBuild_nullでもバリデーションをスキップしてインスタンスを生成できる")
    void reBuildSkipsValidationWithNullValue() {
        UserName actualUserName = UserName.reBuild(null);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isNull();
    }

    @Test
    @DisplayName("reBuild_空文字でもバリデーションをスキップしてインスタンスを生成できる")
    void reBuildSkipsValidationWithEmptyString() {
        String emptyUserName = "";

        UserName actualUserName = UserName.reBuild(emptyUserName);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(emptyUserName);
    }

    @Test
    @DisplayName("reBuild_最大長を超えてもバリデーションをスキップしてインスタンスを生成できる")
    void reBuildSkipsValidationWithOverMaxLength() {
        UserName actualUserName = UserName.reBuild(OVER_MAX_LENGTH_USER_NAME);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(OVER_MAX_LENGTH_USER_NAME);
    }

    @Test
    @DisplayName("reBuild_日本語でもバリデーションをスキップしてインスタンスを生成できる")
    void reBuildSkipsValidationWithJapaneseCharacters() {
        UserName actualUserName = UserName.reBuild(JAPANESE_USER_NAME);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(JAPANESE_USER_NAME);
    }

    @Test
    @DisplayName("reBuild_特殊文字を含んでもバリデーションをスキップしてインスタンスを生成できる")
    void reBuildSkipsValidationWithSpecialCharacters() {
        UserName actualUserName = UserName.reBuild(SPECIAL_CHARACTERS_USER_NAME);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(SPECIAL_CHARACTERS_USER_NAME);
    }

    @Test
    @DisplayName("toString_ユーザー名の文字列表現を返す")
    void toStringReturnsUserNameString() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        String actualToString = actualUserName.toString();

        assertThat(actualToString).isEqualTo(VALID_USER_NAME);
    }

    @Test
    @DisplayName("toString_とvalue()が同じ値を返す")
    void toStringAndValueReturnSameValue() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        assertThat(actualUserName.toString()).isEqualTo(actualUserName.value());
    }

    @Test
    @DisplayName("toString_日本語のユーザー名を正しく返す")
    void toStringReturnsJapaneseUserName() {
        UserName actualUserName = UserName.of(JAPANESE_USER_NAME);

        String actualToString = actualUserName.toString();

        assertThat(actualToString).isEqualTo(JAPANESE_USER_NAME);
    }

    @Test
    @DisplayName("toString_特殊文字を含むユーザー名を正しく返す")
    void toStringReturnsUserNameWithSpecialCharacters() {
        UserName actualUserName = UserName.of(SPECIAL_CHARACTERS_USER_NAME);

        String actualToString = actualUserName.toString();

        assertThat(actualToString).isEqualTo(SPECIAL_CHARACTERS_USER_NAME);
    }

    @Test
    @DisplayName("toString_スペースを含むユーザー名を正しく返す")
    void toStringReturnsUserNameWithSpaces() {
        UserName actualUserName = UserName.of(WITH_SPACES_USER_NAME);

        String actualToString = actualUserName.toString();

        assertThat(actualToString).isEqualTo(WITH_SPACES_USER_NAME);
    }

    @Test
    @DisplayName("toString_最小長のユーザー名を正しく返す")
    void toStringReturnsMinLengthUserName() {
        UserName actualUserName = UserName.of(MIN_LENGTH_USER_NAME);

        String actualToString = actualUserName.toString();

        assertThat(actualToString)
                .isEqualTo(MIN_LENGTH_USER_NAME)
                .hasSize(1);
    }

    @Test
    @DisplayName("toString_最大長のユーザー名を正しく返す")
    void toStringReturnsMaxLengthUserName() {
        UserName actualUserName = UserName.of(MAX_LENGTH_USER_NAME);

        String actualToString = actualUserName.toString();

        assertThat(actualToString)
                .isEqualTo(MAX_LENGTH_USER_NAME)
                .hasSize(255);
    }

    @Test
    @DisplayName("value_とtoString()が一貫性を持つ")
    void valueAndToStringAreConsistent() {
        UserName actualUserName = UserName.of(VALID_USER_NAME);

        assertThat(actualUserName.value())
                .isEqualTo(actualUserName.toString())
                .isEqualTo(VALID_USER_NAME);
    }
}
