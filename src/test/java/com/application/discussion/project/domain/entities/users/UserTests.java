package com.application.discussion.project.domain.entities.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Userドメインエンティティのテスト")
class UserTests {

    private static final String VALID_USER_NAME = "testUser";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "securePassword123";

    @Test
    @DisplayName("createメソッドは有効な入力でUserインスタンスを返す")
    void create_ShouldReturnUser_WhenValidInput() {
        User actualUser = User.create(VALID_USER_NAME, VALID_EMAIL, VALID_PASSWORD);

        assertThat(actualUser).isNotNull();
    }

    @Test
    @DisplayName("createメソッドはnullのユーザー名で例外をスローする")
    void create_ShouldThrowException_WhenUserNameIsNull() {
        assertThatThrownBy(() -> User.create(null, VALID_EMAIL, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("createメソッドは空のユーザー名で例外をスローする")
    void create_ShouldThrowException_WhenUserNameIsEmpty() {
        String emptyUserName = "";

        assertThatThrownBy(() -> User.create(emptyUserName, VALID_EMAIL, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("createメソッドはnullのメールアドレスで例外をスローする")
    void create_ShouldThrowException_WhenEmailIsNull() {
        assertThatThrownBy(() -> User.create(VALID_USER_NAME, null, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("メールアドレスの形式が正しくありません");
    }

    @Test
    @DisplayName("createメソッドは無効なメールアドレスで例外をスローする")
    void create_ShouldThrowException_WhenEmailIsInvalid() {
        String invalidEmail = "invalid-email";

        assertThatThrownBy(() -> User.create(VALID_USER_NAME, invalidEmail, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("メールアドレスの形式が正しくありません");
    }

    @Test
    @DisplayName("createメソッドはnullのパスワードで例外をスローする")
    void create_ShouldThrowException_WhenPasswordIsNull() {
        assertThatThrownBy(() -> User.create(VALID_USER_NAME, VALID_EMAIL, null))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("パスワードは必須項目です");
    }

    @Test
    @DisplayName("createメソッドは弱いパスワードで例外をスローする")
    void create_ShouldThrowException_WhenPasswordIsWeak() {
        String weakPassword = "1111111111";

        assertThatThrownBy(() -> User.create(VALID_USER_NAME, VALID_EMAIL, weakPassword))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }
}
