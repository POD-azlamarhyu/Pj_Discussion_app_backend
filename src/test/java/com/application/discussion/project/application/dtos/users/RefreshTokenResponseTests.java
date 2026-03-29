package com.application.discussion.project.application.dtos.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RefreshTokenResponseのテスト")
public class RefreshTokenResponseTests {

    private static final String TEST_ACCESS_TOKEN = "access-token-value";

    @Test
    @DisplayName("ofメソッドでアクセストークンを保持したオブジェクトを生成できる")
    public void ofReturnsRefreshTokenResponseWithAccessToken() {
        RefreshTokenResponse actualResponse = RefreshTokenResponse.of(TEST_ACCESS_TOKEN);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
    }

    @Test
    @DisplayName("toStringメソッドはアクセストークンをマスクする")
    public void toStringMasksAccessToken() {
        RefreshTokenResponse targetResponse = RefreshTokenResponse.of(TEST_ACCESS_TOKEN);

        String actualToString = targetResponse.toString();

        assertThat(actualToString).contains("RefreshTokenResponse{accessToken='****'}");
        assertThat(actualToString).doesNotContain(TEST_ACCESS_TOKEN);
    }

    @Test
    @DisplayName("デフォルトコンストラクタでインスタンスを生成できる")
    public void defaultConstructorCreatesInstance() {
        RefreshTokenResponse actualResponse = new RefreshTokenResponse();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getAccessToken()).isNull();
    }
}
