package com.application.discussion.project.presentation.validations;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.application.dtos.users.SignUpRequest;
import com.application.discussion.project.presentation.exceptions.PresentationLayerErrorException;

@DisplayName("SignUpRequestValidation クラスのテスト")
class SignUpRequestValidationTests {

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_LOGIN_ID = "testuser123";
    private static final String VALID_PASSWORD = "Password123";
    private static final String VALID_USERNAME = "TestUser";

    private SignUpRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new SignUpRequest();
        validRequest.setEmail(VALID_EMAIL);
        validRequest.setPassword(VALID_PASSWORD);
        validRequest.setUsername(VALID_USERNAME);
    }

    @Test
    @DisplayName("validate_有効なSignUpRequestで例外がスローされないこと")
    void validateWithValidRequestDoesNotThrowException() {
        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_メールアドレスがnullの場合にPresentationLayerErrorExceptionがスローされること")
    void validateWithNullEmailThrowsException() {
        validRequest.setEmail(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .hasMessageContaining("メールアドレスは必須項目です")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("validate_メールアドレスが空文字の場合に検証が通ること")
    void validateWithEmptyEmailPassesValidation() {
        validRequest.setEmail("");

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_パスワードがnullの場合にPresentationLayerErrorExceptionがスローされること")
    void validateWithNullPasswordThrowsException() {
        validRequest.setPassword(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .hasMessageContaining("パスワードは必須項目です")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("validate_パスワードが空文字の場合に検証が通ること")
    void validateWithEmptyPasswordPassesValidation() {
        validRequest.setPassword("");

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_ユーザーネームがnullの場合にPresentationLayerErrorExceptionがスローされること")
    void validateWithNullUsernameThrowsException() {
        validRequest.setUsername(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .hasMessageContaining("ユーザーネームは必須項目です")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("validate_ユーザーネームが空文字の場合に検証が通ること")
    void validateWithEmptyUsernamePassesValidation() {
        validRequest.setUsername("");

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_ログインIDがnullの場合に検証が通ること")
    void validateWithNullLoginIdPassesValidation() {

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_ログインIDが空文字の場合に検証が通ること")
    void validateWithEmptyLoginIdPassesValidation() {

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_全てのフィールドがnullの場合に最初のフィールドで例外がスローされること")
    void validateWithAllNullFieldsThrowsExceptionForFirstField() {
        validRequest.setEmail(null);
        validRequest.setPassword(null);
        validRequest.setUsername(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .hasMessageContaining("メールアドレスは必須項目です");
    }

    @Test
    @DisplayName("validate_メールアドレスのみ有効でその他がnullの場合にパスワードで例外がスローされること")
    void validateWithOnlyEmailValidThrowsExceptionForPassword() {
        validRequest.setEmail(VALID_EMAIL);
        validRequest.setPassword(null);
        validRequest.setUsername(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .hasMessageContaining("パスワードは必須項目です");
    }

    @Test
    @DisplayName("validate_メールアドレスとパスワードのみ有効でユーザーネームがnullの場合に例外がスローされること")
    void validateWithEmailAndPasswordValidThrowsExceptionForUsername() {
        validRequest.setEmail(VALID_EMAIL);
        validRequest.setPassword(VALID_PASSWORD);
        validRequest.setUsername(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .hasMessageContaining("ユーザーネームは必須項目です");
    }

    @Test
    @DisplayName("validate_例外のHttpStatusCodeが400であること")
    void validateExceptionHasCorrectHttpStatusCode() {
        validRequest.setEmail(null);

        assertThatThrownBy(() -> SignUpRequestValidation.validate(validRequest))
                .isInstanceOf(PresentationLayerErrorException.class)
                .extracting("code")
                .isEqualTo(HttpStatus.valueOf(400));
    }

    @Test
    @DisplayName("validate_全てのフィールドが有効な値の場合に例外がスローされないこと")
    void validateWithAllValidFieldsDoesNotThrowException() {
        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_特殊文字を含むメールアドレスで検証が通ること")
    void validateWithSpecialCharactersInEmailPassesValidation() {
        validRequest.setEmail("test.user+tag@example.co.jp");

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_長い文字列の値で検証が通ること")
    void validateWithLongStringsPassesValidation() {
        validRequest.setEmail("a".repeat(250) + "@example.com");
        validRequest.setPassword("P".repeat(250) + "a1");
        validRequest.setUsername("u".repeat(250));

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validate_最小限の有効な値で検証が通ること")
    void validateWithMinimalValidValuesPassesValidation() {
        validRequest.setEmail("a@b.jp");
        validRequest.setPassword("Abcdefg123");
        validRequest.setUsername("a");

        assertThatCode(() -> SignUpRequestValidation.validate(validRequest))
                .doesNotThrowAnyException();
    }
}
