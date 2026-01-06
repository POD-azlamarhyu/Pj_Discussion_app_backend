package com.application.discussion.project.application.dtos.users;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SignUpRequest DTOのテスト")
class SignUpRequestTests {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "testuser";

    @Test
    @DisplayName("デフォルトコンストラクタで生成時、全フィールドがnullであること")
    void createWithDefaultConstructor_returnsInstanceWithNullFields() {
        SignUpRequest actual = new SignUpRequest();

        assertThat(actual.getEmail()).isNull();
        assertThat(actual.getPassword()).isNull();
        assertThat(actual.getUsername()).isNull();
    }

    @Test
    @DisplayName("引数付きコンストラクタで生成時、全フィールドが設定されること")
    void createWithAllArgsConstructor_returnsInstanceWithSetFields() {
        SignUpRequest actual = new SignUpRequest(TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME);

        assertThat(actual.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(actual.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(actual.getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("setEmailでメールアドレスが正しく設定されること")
    void setEmail_updatesEmailField() {
        SignUpRequest actual = new SignUpRequest();

        actual.setEmail(TEST_EMAIL);

        assertThat(actual.getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("setPasswordでパスワードが正しく設定されること")
    void setPassword_updatesPasswordField() {
        SignUpRequest actual = new SignUpRequest();

        actual.setPassword(TEST_PASSWORD);

        assertThat(actual.getPassword()).isEqualTo(TEST_PASSWORD);
    }

    @Test
    @DisplayName("setUsernameでユーザー名が正しく設定されること")
    void setUsername_updatesUsernameField() {
        SignUpRequest actual = new SignUpRequest();

        actual.setUsername(TEST_USERNAME);

        assertThat(actual.getUsername()).isEqualTo(TEST_USERNAME);
    }
}
