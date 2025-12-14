package com.application.discussion.project.application.dtos.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    @DisplayName("デフォルトコンストラクタ_全フィールドがnullで初期化される")
    void defaultConstructorInitializesAllFieldsAsNull() {
        LoginResponse actualResponse = new LoginResponse();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getUserId()).isNull();
        assertThat(actualResponse.getUsername()).isNull();
        assertThat(actualResponse.getRoles()).isNull();
    }

    @Test
    @DisplayName("全引数コンストラクタ_すべてのフィールドが正しく初期化される")
    void allArgsConstructorInitializesAllFieldsCorrectly() {
        UUID expectedUserId = UUID.randomUUID();
        String expectedUsername = "testuser";
        List<String> expectedRoles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        LoginResponse actualResponse = new LoginResponse(expectedUserId, expectedUsername, expectedRoles);

        assertThat(actualResponse.getUserId()).isEqualTo(expectedUserId);
        assertThat(actualResponse.getUsername()).isEqualTo(expectedUsername);
        assertThat(actualResponse.getRoles()).isEqualTo(expectedRoles);
    }

    @Test
    @DisplayName("builder_すべてのフィールドを指定してインスタンスが生成される")
    void builderCreatesInstanceWithAllFields() {
        UUID expectedUserId = UUID.randomUUID();
        String expectedUsername = "testuser";
        List<String> expectedRoles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        LoginResponse actualResponse = LoginResponse.builder()
                .userId(expectedUserId)
                .username(expectedUsername)
                .roles(expectedRoles)
                .build();

        assertThat(actualResponse.getUserId()).isEqualTo(expectedUserId);
        assertThat(actualResponse.getUsername()).isEqualTo(expectedUsername);
        assertThat(actualResponse.getRoles()).isEqualTo(expectedRoles);
    }

    @Test
    @DisplayName("builder_一部のフィールドのみ指定してインスタンスが生成される")
    void builderCreatesInstanceWithPartialFields() {
        String expectedUsername = "testuser";

        LoginResponse actualResponse = LoginResponse.builder()
                .username(expectedUsername)
                .build();

        assertThat(actualResponse.getUserId()).isNull();
        assertThat(actualResponse.getUsername()).isEqualTo(expectedUsername);
        assertThat(actualResponse.getRoles()).isNull();
    }

    @Test
    @DisplayName("setUserId_ユーザーIDが正しく設定される")
    void setUserIdSetsUserIdCorrectly() {
        LoginResponse actualResponse = new LoginResponse();
        UUID expectedUserId = UUID.randomUUID();

        actualResponse.setUserId(expectedUserId);

        assertThat(actualResponse.getUserId()).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("setUsername_ユーザー名が正しく設定される")
    void setUsernameSetsUsernameCorrectly() {
        LoginResponse actualResponse = new LoginResponse();
        String expectedUsername = "newuser";

        actualResponse.setUsername(expectedUsername);

        assertThat(actualResponse.getUsername()).isEqualTo(expectedUsername);
    }

    @Test
    @DisplayName("setRoles_ロールリストが正しく設定される")
    void setRolesSetsRolesCorrectly() {
        LoginResponse actualResponse = new LoginResponse();
        List<String> expectedRoles = Arrays.asList("ROLE_USER");

        actualResponse.setRoles(expectedRoles);

        assertThat(actualResponse.getRoles()).isEqualTo(expectedRoles);
    }

    @Test
    @DisplayName("builder_空のロールリストを設定できる")
    void builderCanSetEmptyRolesList() {
        UUID expectedUserId = UUID.randomUUID();
        String expectedUsername = "testuser";
        List<String> expectedEmptyRoles = Collections.emptyList();

        LoginResponse actualResponse = LoginResponse.builder()
                .userId(expectedUserId)
                .username(expectedUsername)
                .roles(expectedEmptyRoles)
                .build();

        assertThat(actualResponse.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("builder_null値を含むインスタンスが生成される")
    void builderCreatesInstanceWithNullValues() {
        LoginResponse actualResponse = LoginResponse.builder()
                .userId(null)
                .username(null)
                .roles(null)
                .build();

        assertThat(actualResponse.getUserId()).isNull();
        assertThat(actualResponse.getUsername()).isNull();
        assertThat(actualResponse.getRoles()).isNull();
    }

    @Test
    @DisplayName("全引数コンストラクタ_複数のロールが正しく設定される")
    void allArgsConstructorSetsMultipleRolesCorrectly() {
        UUID expectedUserId = UUID.randomUUID();
        String expectedUsername = "admin";
        List<String> expectedRoles = Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR");

        LoginResponse actualResponse = new LoginResponse(expectedUserId, expectedUsername, expectedRoles);

        assertThat(actualResponse.getRoles()).hasSize(3);
        assertThat(actualResponse.getRoles()).containsExactly("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR");
    }

    @Test
    @DisplayName("builder_空文字列のユーザー名を設定できる")
    void builderCanSetEmptyUsername() {
        String expectedEmptyUsername = "";

        LoginResponse actualResponse = LoginResponse.builder()
                .username(expectedEmptyUsername)
                .build();

        assertThat(actualResponse.getUsername()).isEmpty();
    }
}
