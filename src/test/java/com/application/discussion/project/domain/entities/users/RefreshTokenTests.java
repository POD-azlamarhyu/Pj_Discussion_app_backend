package com.application.discussion.project.domain.entities.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("RefreshTokenドメインエンティティのテスト")
public class RefreshTokenTests {

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final UUID TEST_TOKEN_ID = UUID.randomUUID();
    private static final String TEST_TOKEN_HASH = "abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789";

    @Test
    @DisplayName("createメソッドは有効な入力でRefreshTokenを返す")
    public void createReturnsRefreshTokenWithValidInput() {
        LocalDateTime expectedExpiresAt = LocalDateTime.now().plusHours(1);

        RefreshToken actualToken = RefreshToken.create(TEST_USER_ID, TEST_TOKEN_HASH, expectedExpiresAt);

        assertThat(actualToken).isNotNull();
        assertThat(actualToken.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(actualToken.getTokenHash()).isEqualTo(TEST_TOKEN_HASH);
        assertThat(actualToken.getExpiresAt()).isEqualTo(expectedExpiresAt);
        assertThat(actualToken.getIsUsed()).isFalse();
        assertThat(actualToken.getIsRevoked()).isFalse();
    }

    @Test
    @DisplayName("createメソッドはuserIdがnullの場合に例外をスローする")
    public void createThrowsExceptionWhenUserIdIsNull() {
        LocalDateTime expectedExpiresAt = LocalDateTime.now().plusHours(1);

        assertThatThrownBy(() -> RefreshToken.create(null, TEST_TOKEN_HASH, expectedExpiresAt))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("ユーザーIDが指定されていません");
    }

    @Test
    @DisplayName("createメソッドはtokenHashが空の場合に例外をスローする")
    public void createThrowsExceptionWhenTokenHashIsBlank() {
        LocalDateTime expectedExpiresAt = LocalDateTime.now().plusHours(1);

        assertThatThrownBy(() -> RefreshToken.create(TEST_USER_ID, "", expectedExpiresAt))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("トークンハッシュが指定されていません");
    }

    @Test
    @DisplayName("isValidメソッドは有効なトークンでtrueを返す")
    public void isValidReturnsTrueForValidToken() {
        RefreshToken targetToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_TOKEN_HASH,
            LocalDateTime.now().plusMinutes(10),
            false,
            false,
            LocalDateTime.now().minusMinutes(1)
        );

        boolean actualIsValid = targetToken.isValid();

        assertThat(actualIsValid).isTrue();
    }

    @Test
    @DisplayName("isValidメソッドは使用済みトークンでfalseを返す")
    public void isValidReturnsFalseWhenTokenIsUsed() {
        RefreshToken targetToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_TOKEN_HASH,
            LocalDateTime.now().plusMinutes(10),
            true,
            false,
            LocalDateTime.now().minusMinutes(1)
        );

        boolean actualIsValid = targetToken.isValid();

        assertThat(actualIsValid).isFalse();
    }

    @Test
    @DisplayName("isValidメソッドは期限切れトークンでfalseを返す")
    public void isValidReturnsFalseWhenTokenIsExpired() {
        RefreshToken targetToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_TOKEN_HASH,
            LocalDateTime.now().minusMinutes(1),
            false,
            false,
            LocalDateTime.now().minusHours(1)
        );

        boolean actualIsValid = targetToken.isValid();

        assertThat(actualIsValid).isFalse();
    }

    @Test
    @DisplayName("isAlreadyUsedメソッドは使用済みトークンでtrueを返す")
    public void isAlreadyUsedReturnsTrueForUsedToken() {
        RefreshToken targetToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_TOKEN_HASH,
            LocalDateTime.now().plusMinutes(10),
            true,
            false,
            LocalDateTime.now().minusMinutes(1)
        );

        boolean actualIsAlreadyUsed = targetToken.isAlreadyUsed();

        assertThat(actualIsAlreadyUsed).isTrue();
    }

    @Test
    @DisplayName("toStringメソッドはトークンハッシュをマスクして返す")
    public void toStringMasksTokenHash() {
        RefreshToken targetToken = RefreshToken.reBuild(
            TEST_TOKEN_ID,
            TEST_USER_ID,
            TEST_TOKEN_HASH,
            LocalDateTime.now().plusMinutes(10),
            false,
            false,
            LocalDateTime.now().minusMinutes(1)
        );

        String actualToString = targetToken.toString();

        assertThat(actualToString).contains("RefreshToken{");
        assertThat(actualToString).contains("tokenHash='****'");
        assertThat(actualToString).doesNotContain(TEST_TOKEN_HASH);
    }
}
