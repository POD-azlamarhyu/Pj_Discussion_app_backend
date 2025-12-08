package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Email値オブジェクトのテスト")
class EmailTests {

    @Nested
    @DisplayName("正常系テスト")
    class ValidEmailTests {

        @Test
        @DisplayName("有効なメールアドレス（.jp）でEmail値オブジェクトが生成できる")
        void createEmailWithValidJpDomain() {
            String expectedEmail = "test@example.jp";

            Email actualEmail = Email.of(expectedEmail);

            assertThat(actualEmail.value()).isEqualTo(expectedEmail);
        }

        @Test
        @DisplayName("有効なメールアドレス（.com）でEmail値オブジェクトが生成できる")
        void createEmailWithValidComDomain() {
            String expectedEmail = "user@example.com";

            Email actualEmail = Email.of(expectedEmail);

            assertThat(actualEmail.value()).isEqualTo(expectedEmail);
        }

        @Test
        @DisplayName("特殊文字を含む有効なメールアドレスでEmail値オブジェクトが生成できる")
        void createEmailWithSpecialCharacters() {
            String expectedEmail = "test.user+tag@example.co.jp";

            Email actualEmail = Email.of(expectedEmail);

            assertThat(actualEmail.value()).isEqualTo(expectedEmail);
        }

        @Test
        @DisplayName("サブドメインを含む有効なメールアドレスでEmail値オブジェクトが生成できる")
        void createEmailWithSubdomain() {
            String expectedEmail = "user@mail.example.com";

            Email actualEmail = Email.of(expectedEmail);

            assertThat(actualEmail.value()).isEqualTo(expectedEmail);
        }
    }

    @Nested
    @DisplayName("異常系テスト - null・空文字")
    class InvalidNullOrBlankTests {

        @Test
        @DisplayName("nullのメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenEmailIsNull() {
            String invalidEmail = null;

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("空文字のメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenEmailIsEmpty() {
            String invalidEmail = "";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("空白のみのメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenEmailIsBlank() {
            String invalidEmail = "   ";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }
    }

    @Nested
    @DisplayName("異常系テスト - 形式不正")
    class InvalidFormatTests {

        @Test
        @DisplayName("@が含まれないメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenEmailHasNoAtSign() {
            String invalidEmail = "testexample.com";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("ローカル部が空のメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenLocalPartIsEmpty() {
            String invalidEmail = "@example.com";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("ドメイン部が空のメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenDomainPartIsEmpty() {
            String invalidEmail = "test@";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("複数の@を含むメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenEmailHasMultipleAtSigns() {
            String invalidEmail = "test@@example.com";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }
    }

    @Nested
    @DisplayName("異常系テスト - ドメイン制限")
    class InvalidDomainTests {

        @Test
        @DisplayName(".jpでも.comでもないドメインでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenDomainIsNotJpOrCom() {
            String invalidEmail = "test@example.net";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("トップレベルドメインがないメールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenTopLevelDomainIsMissing() {
            String invalidEmail = "test@example";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class BoundaryTests {

        @Test
        @DisplayName("最大長（255文字）のメールアドレスでEmail値オブジェクトが生成できる")
        void createEmailWithMaxLength() {
            String localPart = "a".repeat(64);
            String domainPart = "example.com";
            String expectedEmail = localPart + "@" + "b".repeat(255 - localPart.length() - domainPart.length() - 2) + "." + domainPart;

            Email actualEmail = Email.of(expectedEmail);

            assertThat(actualEmail.value()).isEqualTo(expectedEmail);
            assertThat(actualEmail.isBelowMaxLength()).isTrue();
        }

        @Test
        @DisplayName("最大長を超える（256文字）メールアドレスでDomainLayerErrorExceptionがスローされる")
        void throwExceptionWhenEmailExceedsMaxLength() {
            String localPart = "a".repeat(245);
            String invalidEmail = localPart + "@example.com";

            assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(DomainLayerErrorException.class)
                .hasMessage("メールアドレスの形式が正しくありません");
        }

        @Test
        @DisplayName("最小長の有効なメールアドレスでEmail値オブジェクトが生成できる")
        void createEmailWithMinLength() {
            String expectedEmail = "a@b.jp";

            Email actualEmail = Email.of(expectedEmail);

            assertThat(actualEmail.value()).isEqualTo(expectedEmail);
        }
    }

    @Nested
    @DisplayName("isBelowMaxLengthメソッドのテスト")
    class IsBelowMaxLengthTests {

        @Test
        @DisplayName("最大長以下のメールアドレスでtrueを返す")
        void returnTrueWhenBelowMaxLength() {
            Email email = Email.of("test@example.com");

            boolean actual = email.isBelowMaxLength();

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("最大長ちょうどのメールアドレスでtrueを返す")
        void returnTrueWhenExactlyMaxLength() {
            String localPart = "a".repeat(64);
            String domainPart = "example.com";
            String emailString = localPart + "@" + "b".repeat(255 - localPart.length() - domainPart.length() - 2) + "." + domainPart;
            Email email = Email.of(emailString);

            boolean actual = email.isBelowMaxLength();

            assertThat(actual).isTrue();
        }
    }
}
