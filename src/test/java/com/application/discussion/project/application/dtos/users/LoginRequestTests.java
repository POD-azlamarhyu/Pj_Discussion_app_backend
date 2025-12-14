package com.application.discussion.project.application.dtos.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginRequest クラスのテスト")
class LoginRequestTests {

    @Test
    @DisplayName("デフォルトコンストラクタ_全フィールドがnullで初期化される")
    void defaultConstructorInitializesAllFieldsAsNull() {
        LoginRequest actualRequest = new LoginRequest();

        assertThat(actualRequest).isNotNull();
        assertThat(actualRequest.getEmailOrLoginId()).isNull();
        assertThat(actualRequest.getPassword()).isNull();
    }

    @Test
    @DisplayName("全引数コンストラクタ_すべてのフィールドが正しく初期化される")
    void allArgsConstructorInitializesAllFieldsCorrectly() {
        String expectedEmailOrLoginId = "test@example.com";
        String expectedPassword = "password123";

        LoginRequest actualRequest = new LoginRequest(expectedEmailOrLoginId, expectedPassword);

        assertThat(actualRequest.getEmailOrLoginId()).isEqualTo(expectedEmailOrLoginId);
        assertThat(actualRequest.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("全引数コンストラクタ_メールアドレスを指定してインスタンスが生成される")
    void allArgsConstructorWithEmailAddress() {
        String expectedEmail = "user@example.com";
        String expectedPassword = "securePass456";

        LoginRequest actualRequest = new LoginRequest(expectedEmail, expectedPassword);

        assertThat(actualRequest.getEmailOrLoginId()).isEqualTo(expectedEmail);
        assertThat(actualRequest.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("全引数コンストラクタ_ログインIDを指定してインスタンスが生成される")
    void allArgsConstructorWithLoginId() {
        String expectedLoginId = "testuser123";
        String expectedPassword = "myPassword789";

        LoginRequest actualRequest = new LoginRequest(expectedLoginId, expectedPassword);

        assertThat(actualRequest.getEmailOrLoginId()).isEqualTo(expectedLoginId);
        assertThat(actualRequest.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("setEmailOrLoginId_メールアドレスが正しく設定される")
    void setEmailOrLoginIdSetsEmailCorrectly() {
        LoginRequest actualRequest = new LoginRequest();
        String expectedEmail = "newuser@example.com";

        actualRequest.setEmailOrLoginId(expectedEmail);

        assertThat(actualRequest.getEmailOrLoginId()).isEqualTo(expectedEmail);
    }

    @Test
    @DisplayName("setEmailOrLoginId_ログインIDが正しく設定される")
    void setEmailOrLoginIdSetsLoginIdCorrectly() {
        LoginRequest actualRequest = new LoginRequest();
        String expectedLoginId = "newloginid";

        actualRequest.setEmailOrLoginId(expectedLoginId);

        assertThat(actualRequest.getEmailOrLoginId()).isEqualTo(expectedLoginId);
    }

    @Test
    @DisplayName("setPassword_パスワードが正しく設定される")
    void setPasswordSetsPasswordCorrectly() {
        LoginRequest actualRequest = new LoginRequest();
        String expectedPassword = "newPassword123";

        actualRequest.setPassword(expectedPassword);

        assertThat(actualRequest.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    @DisplayName("全引数コンストラクタ_null値を含むインスタンスが生成される")
    void allArgsConstructorWithNullValues() {
        LoginRequest actualRequest = new LoginRequest(null, null);

        assertThat(actualRequest.getEmailOrLoginId()).isNull();
        assertThat(actualRequest.getPassword()).isNull();
    }

    @Test
    @DisplayName("setEmailOrLoginId_null値を設定できる")
    void setEmailOrLoginIdCanSetNull() {
        LoginRequest actualRequest = new LoginRequest("test@example.com", "password");

        actualRequest.setEmailOrLoginId(null);

        assertThat(actualRequest.getEmailOrLoginId()).isNull();
    }

    @Test
    @DisplayName("setPassword_null値を設定できる")
    void setPasswordCanSetNull() {
        LoginRequest actualRequest = new LoginRequest("test@example.com", "password");

        actualRequest.setPassword(null);

        assertThat(actualRequest.getPassword()).isNull();
    }

    @Test
    @DisplayName("全引数コンストラクタ_空文字列を設定できる")
    void allArgsConstructorWithEmptyStrings() {
        String expectedEmptyString = "";

        LoginRequest actualRequest = new LoginRequest(expectedEmptyString, expectedEmptyString);

        assertThat(actualRequest.getEmailOrLoginId()).isEmpty();
        assertThat(actualRequest.getPassword()).isEmpty();
    }

    @Test
    @DisplayName("setEmailOrLoginId_空文字列を設定できる")
    void setEmailOrLoginIdCanSetEmptyString() {
        LoginRequest actualRequest = new LoginRequest();
        String expectedEmptyString = "";

        actualRequest.setEmailOrLoginId(expectedEmptyString);

        assertThat(actualRequest.getEmailOrLoginId()).isEmpty();
    }

    @Test
    @DisplayName("setPassword_空文字列を設定できる")
    void setPasswordCanSetEmptyString() {
        LoginRequest actualRequest = new LoginRequest();
        String expectedEmptyString = "";

        actualRequest.setPassword(expectedEmptyString);

        assertThat(actualRequest.getPassword()).isEmpty();
    }
}
