package com.application.discussion.project.domain.valueobjects.users;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserName 値オブジェクトのテスト")
class UserNameTests {

    @Test
    @DisplayName("of_正常なユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithValidUserName() {
        String expectedUserName = "testuser";

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName).isNotNull();
        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
    }

    @Test
    @DisplayName("of_最小長(1文字)のユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithMinimumLengthUserName() {
        String expectedUserName = "a";

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
        assertThat(actualUserName.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_最大長(255文字)のユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithMaximumLengthUserName() {
        String expectedUserName = "a".repeat(255);

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
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
        String invalidUserName = "";

        assertThatThrownBy(() -> UserName.of(invalidUserName))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("of_最大長を超えるユーザー名の場合に例外がスローされる")
    void ofThrowsExceptionWhenUserNameExceedsMaxLength() {
        String invalidUserName = "a".repeat(256);

        assertThatThrownBy(() -> UserName.of(invalidUserName))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessageContaining("ユーザー名は1文字以上255文字以下である必要があります");
    }

    @Test
    @DisplayName("value_設定されたユーザー名が正しく取得される")
    void valueReturnsCorrectUserName() {
        String expectedUserName = "testuser123";

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長以下の場合にtrueを返す")
    void isBelowMaxLengthReturnsTrueWhenUserNameIsBelowMaxLength() {
        String validUserName = "testuser";

        UserName actualUserName = UserName.of(validUserName);

        assertThat(actualUserName.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isBelowMaxLength_最大長ちょうどの場合にtrueを返す")
    void isBelowMaxLengthReturnsTrueWhenUserNameIsExactlyMaxLength() {
        String validUserName = "a".repeat(255);

        UserName actualUserName = UserName.of(validUserName);

        assertThat(actualUserName.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長以上の場合にtrueを返す")
    void isAboveMinLengthReturnsTrueWhenUserNameIsAboveMinLength() {
        String validUserName = "testuser";

        UserName actualUserName = UserName.of(validUserName);

        assertThat(actualUserName.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("isAboveMinLength_最小長ちょうどの場合にtrueを返す")
    void isAboveMinLengthReturnsTrueWhenUserNameIsExactlyMinLength() {
        String validUserName = "a";

        UserName actualUserName = UserName.of(validUserName);

        assertThat(actualUserName.isAboveMinLength()).isTrue();
    }

    @Test
    @DisplayName("of_日本語のユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithJapaneseUserName() {
        String expectedUserName = "テストユーザー";

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
    }

    @Test
    @DisplayName("of_特殊文字を含むユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithSpecialCharactersUserName() {
        String expectedUserName = "test_user-123";

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
    }

    @Test
    @DisplayName("of_スペースを含むユーザー名でインスタンスが生成される")
    void ofCreatesInstanceWithSpacesUserName() {
        String expectedUserName = "test user";

        UserName actualUserName = UserName.of(expectedUserName);

        assertThat(actualUserName.value()).isEqualTo(expectedUserName);
    }
}
