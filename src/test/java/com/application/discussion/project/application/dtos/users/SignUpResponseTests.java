package com.application.discussion.project.application.dtos.users;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SignUpResponse DTOのテスト")
class SignUpResponseTests {

    private static final String TEST_USER_ID = "user-123";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    private static final String TEST_IS_ACTIVE = "ACTIVE";

    @Test
    @DisplayName("デフォルトコンストラクタで生成時、全フィールドがnullであること")
    void createWithDefaultConstructor_returnsInstanceWithNullFields() {
        SignUpResponse actual = new SignUpResponse();

        assertThat(actual.getUserId()).isNull();
        assertThat(actual.getUsername()).isNull();
        assertThat(actual.getEmail()).isNull();
        assertThat(actual.getCreatedAt()).isNull();
        assertThat(actual.getisActive()).isNull();
    }

    @Test
    @DisplayName("ファクトリメソッドofで生成時、全フィールドが正しく設定されること")
    void createWithFactoryMethod_returnsInstanceWithSetFields() {
        SignUpResponse actual = SignUpResponse.of(
            TEST_USER_ID, TEST_USERNAME, TEST_EMAIL, TEST_CREATED_AT, TEST_IS_ACTIVE
        );

        assertThat(actual.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(actual.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(actual.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(actual.getCreatedAt()).isEqualTo(TEST_CREATED_AT);
        assertThat(actual.getisActive()).isEqualTo(TEST_IS_ACTIVE);
    }

    @Test
    @DisplayName("setUserIdでユーザーIDが正しく設定されること")
    void setUserId_updatesUserIdField() {
        SignUpResponse actual = new SignUpResponse();

        actual.setUserId(TEST_USER_ID);

        assertThat(actual.getUserId()).isEqualTo(TEST_USER_ID);
    }

    @Test
    @DisplayName("setUsernameでユーザー名が正しく設定されること")
    void setUsername_updatesUsernameField() {
        SignUpResponse actual = new SignUpResponse();

        actual.setUsername(TEST_USERNAME);

        assertThat(actual.getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    @DisplayName("setEmailでメールアドレスが正しく設定されること")
    void setEmail_updatesEmailField() {
        SignUpResponse actual = new SignUpResponse();

        actual.setEmail(TEST_EMAIL);

        assertThat(actual.getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("setCreatedAtで作成日時が正しく設定されること")
    void setCreatedAt_updatesCreatedAtField() {
        SignUpResponse actual = new SignUpResponse();

        actual.setCreatedAt(TEST_CREATED_AT);

        assertThat(actual.getCreatedAt()).isEqualTo(TEST_CREATED_AT);
    }

    @Test
    @DisplayName("setIsActiveでアカウントステータスが正しく設定されること")
    void setIsActive_updatesIsActiveField() {
        SignUpResponse actual = new SignUpResponse();

        actual.setIsActive(TEST_IS_ACTIVE);

        assertThat(actual.getisActive()).isEqualTo(TEST_IS_ACTIVE);
    }
}
