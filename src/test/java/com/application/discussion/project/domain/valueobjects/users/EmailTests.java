package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Email値オブジェクトのテスト")
class EmailTests {

    private static final String VALID_JP_EMAIL = "test@example.jp";
    private static final String VALID_COM_EMAIL = "user@example.com";
    private static final String VALID_SPECIAL_CHAR_EMAIL = "test.user+tag@example.co.jp";
    private static final String VALID_SUBDOMAIN_EMAIL = "user@mail.example.com";
    private static final String VALID_MIN_LENGTH_EMAIL = "a@b.jp";
    private static final String ERROR_MESSAGE = "メールアドレスの形式が正しくありません";
    private static final String ERROR_MESSAGE_EMPTY = "メールアドレスが空です";

    @Test
    @DisplayName("有効なメールアドレス（.jp）でEmail値オブジェクトが生成できる")
    public void createEmailWithValidJpDomainReturnsEmail() {
        Email actualEmail = Email.of(VALID_JP_EMAIL);

        assertThat(actualEmail.value()).isEqualTo(VALID_JP_EMAIL);
    }

    @Test
    @DisplayName("有効なメールアドレス（.com）でEmail値オブジェクトが生成できる")
    public void createEmailWithValidComDomainReturnsEmail() {
        Email actualEmail = Email.of(VALID_COM_EMAIL);

        assertThat(actualEmail.value()).isEqualTo(VALID_COM_EMAIL);
    }

    @Test
    @DisplayName("特殊文字を含む有効なメールアドレスでEmail値オブジェクトが生成できる")
    public void createEmailWithSpecialCharactersReturnsEmail() {
        Email actualEmail = Email.of(VALID_SPECIAL_CHAR_EMAIL);

        assertThat(actualEmail.value()).isEqualTo(VALID_SPECIAL_CHAR_EMAIL);
    }

    @Test
    @DisplayName("サブドメインを含む有効なメールアドレスでEmail値オブジェクトが生成できる")
    public void createEmailWithSubdomainReturnsEmail() {
        Email actualEmail = Email.of(VALID_SUBDOMAIN_EMAIL);

        assertThat(actualEmail.value()).isEqualTo(VALID_SUBDOMAIN_EMAIL);
    }

    @Test
    @DisplayName("nullのメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithNullThrowsException() {
        String invalidEmail = null;

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_EMPTY);
    }

    @Test
    @DisplayName("空文字のメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithEmptyStringThrowsException() {
        String invalidEmail = "";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_EMPTY);
    }

    @Test
    @DisplayName("空白のみのメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithBlankStringThrowsException() {
        String invalidEmail = "   ";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE_EMPTY);
    }

    @Test
    @DisplayName("@が含まれないメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithoutAtSignThrowsException() {
        String invalidEmail = "testexample.com";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("ローカル部が空のメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithEmptyLocalPartThrowsException() {
        String invalidEmail = "@example.com";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("ドメイン部が空のメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithEmptyDomainPartThrowsException() {
        String invalidEmail = "test@";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("複数の@を含むメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithMultipleAtSignsThrowsException() {
        String invalidEmail = "test@@example.com";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName(".jpでも.comでもないドメインでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithInvalidDomainThrowsException() {
        String invalidEmail = "test@example.net";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("トップレベルドメインがないメールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailWithoutTopLevelDomainThrowsException() {
        String invalidEmail = "test@example";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("最大長（255文字）のメールアドレスでEmail値オブジェクトが生成できる")
    public void createEmailWithMaxLengthReturnsEmail() {
        String localPart = "a".repeat(64);
        String domainPart = "example.jp";
        String expectedEmail = localPart + "." + "b".repeat(255 - localPart.length() - domainPart.length() - 2) + "@" + domainPart;

        Email actualEmail = Email.of(expectedEmail);

        assertThat(actualEmail.value()).isEqualTo(expectedEmail);
        assertThat(actualEmail.isBelowMaxLength()).isTrue();
    }

    @Test
    @DisplayName("最大長を超える（256文字）メールアドレスでDomainLayerErrorExceptionがスローされる")
    public void createEmailExceedingMaxLengthThrowsException() {
        String localPart = "a".repeat(245);
        String invalidEmail = localPart + "@example.com";

        assertThatThrownBy(() -> Email.of(invalidEmail))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("最小長の有効なメールアドレスでEmail値オブジェクトが生成できる")
    public void createEmailWithMinLengthReturnsEmail() {
        Email actualEmail = Email.of(VALID_MIN_LENGTH_EMAIL);

        assertThat(actualEmail.value()).isEqualTo(VALID_MIN_LENGTH_EMAIL);
    }

    @Test
    @DisplayName("最大長以下のメールアドレスでtrueを返す")
    public void isBelowMaxLengthWithShortEmailReturnsTrue() {
        Email email = Email.of(VALID_COM_EMAIL);

        boolean actual = email.isBelowMaxLength();

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("最大長ちょうどのメールアドレスでtrueを返す")
    public void isBelowMaxLengthWithExactMaxLengthReturnsTrue() {
        String localPart = "a".repeat(64);
        String domainPart = "example.com";
        String emailString = localPart + "." + "b".repeat(255 - localPart.length() - domainPart.length() - 2) + "@" + domainPart;
        Email email = Email.of(emailString);

        boolean actual = email.isBelowMaxLength();

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("reBuild()で有効なメールアドレスからEmail値オブジェクトを再構築できる")
    public void reBuildWithValidEmailReturnsEmail() {
        Email actualEmail = Email.reBuild(VALID_JP_EMAIL);

        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.value()).isEqualTo(VALID_JP_EMAIL);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされnullでもEmail値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithNullValue() {
        Email actualEmail = Email.reBuild(null);

        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.value()).isNull();
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ空文字でもEmail値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithEmptyString() {
        String emptyEmail = "";

        Email actualEmail = Email.reBuild(emptyEmail);

        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.value()).isEqualTo(emptyEmail);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ不正な形式でもEmail値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithInvalidFormat() {
        String invalidEmail = "invalid-email";

        Email actualEmail = Email.reBuild(invalidEmail);

        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.value()).isEqualTo(invalidEmail);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ最大長を超えてもEmail値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithExceedingMaxLength() {
        String tooLongEmail = "a".repeat(256) + "@example.com";

        Email actualEmail = Email.reBuild(tooLongEmail);

        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.value()).isEqualTo(tooLongEmail);
    }

    @Test
    @DisplayName("reBuild()でバリデーションがスキップされ不正なドメインでもEmail値オブジェクトが生成できる")
    public void reBuildSkipsValidationWithInvalidDomain() {
        String invalidDomainEmail = "test@example.net";

        Email actualEmail = Email.reBuild(invalidDomainEmail);

        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.value()).isEqualTo(invalidDomainEmail);
    }

    @Test
    @DisplayName("toString()がメールアドレスの文字列表現を返す")
    public void toStringReturnsEmailString() {
        Email actualEmail = Email.of(VALID_JP_EMAIL);

        String actualToString = actualEmail.toString();

        assertThat(actualToString).isEqualTo(VALID_JP_EMAIL);
    }

    @Test
    @DisplayName("toString()とvalue()が同じ値を返す")
    public void toStringAndValueReturnSameValue() {
        Email actualEmail = Email.of(VALID_COM_EMAIL);

        assertThat(actualEmail.toString()).isEqualTo(actualEmail.value());
    }

    @Test
    @DisplayName("toString()が特殊文字を含むメールアドレスを正しく返す")
    public void toStringReturnsEmailWithSpecialCharacters() {
        Email actualEmail = Email.of(VALID_SPECIAL_CHAR_EMAIL);

        String actualToString = actualEmail.toString();

        assertThat(actualToString).isEqualTo(VALID_SPECIAL_CHAR_EMAIL);
    }

    @Test
    @DisplayName("value()が正しいメールアドレスの値を返す")
    public void valueReturnsCorrectEmailValue() {
        Email actualEmail = Email.of(VALID_SUBDOMAIN_EMAIL);

        String actualValue = actualEmail.value();

        assertThat(actualValue).isEqualTo(VALID_SUBDOMAIN_EMAIL);
    }

    @Test
    @DisplayName("value()とtoString()が一貫性を持つ")
    public void valueAndToStringAreConsistent() {
        Email actualEmail = Email.of(VALID_MIN_LENGTH_EMAIL);

        assertThat(actualEmail.value())
                .isEqualTo(actualEmail.toString())
                .isEqualTo(VALID_MIN_LENGTH_EMAIL);
    }
}
