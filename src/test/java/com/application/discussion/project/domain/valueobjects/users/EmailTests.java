package com.application.discussion.project.domain.valueobjects.users;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

@DisplayName("Email値オブジェクトのテスト")
class EmailTests {

    @Nested
    @DisplayName("正常系テスト")
    class ValidCases {

        @Test
        @DisplayName("有効な.jpドメインのメールアドレスで値オブジェクトが生成できる")
        void shouldCreateEmailWithValidJpDomain() {
            String validEmail = "test@example.jp";
            Email email = Email.of(validEmail);
            
            assertNotNull(email);
            assertEquals(validEmail, email.value());
        }

        @Test
        @DisplayName("有効な.comドメインのメールアドレスで値オブジェクトが生成できる")
        void shouldCreateEmailWithValidComDomain() {
            String validEmail = "user@domain.com";
            Email email = Email.of(validEmail);
            
            assertNotNull(email);
            assertEquals(validEmail, email.value());
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "simple@example.jp",
            "user.name@example.com",
            "user+tag@example.jp",
            "user_name@example.com",
            "123@example.jp",
            "user@sub.domain.com",
            "a@example.jp"
        })
        @DisplayName("様々な有効なメールアドレス形式で値オブジェクトが生成できる")
        void shouldCreateEmailWithVariousValidFormats(String validEmail) {
            Email email = Email.of(validEmail);
            
            assertNotNull(email);
            assertEquals(validEmail, email.value());
        }

        @Test
        @DisplayName("isBelowMaxLength()がtrueを返す")
        void shouldReturnTrueForBelowMaxLength() {
            String validEmail = "test@example.jp";
            Email email = Email.of(validEmail);
            
            assertTrue(email.isBelowMaxLength());
        }

        @Test
        @DisplayName("最大長ちょうどのメールアドレスで値オブジェクトが生成できる")
        void shouldCreateEmailWithMaxLength() {
            String longLocalPart = "a".repeat(243);
            String validEmail = longLocalPart + "@example.jp";
            
            Email email = Email.of(validEmail);
            
            assertNotNull(email);
            assertTrue(email.isBelowMaxLength());
        }
    }

    @Nested
    @DisplayName("異常系テスト - 空値・null")
    class EmptyAndNullCases {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        @DisplayName("空文字・null・空白のみの場合に例外がスローされる")
        void shouldThrowExceptionForBlankEmail(String invalidEmail) {
            DomainLayerErrorException exception = assertThrows(
                DomainLayerErrorException.class,
                () -> Email.of(invalidEmail)
            );
            
            assertEquals("メールアドレスの形式が正しくありません", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("異常系テスト - 最大長超過")
    class MaxLengthExceededCases {

        @Test
        @DisplayName("最大長を超えるメールアドレスで例外がスローされる")
        void shouldThrowExceptionForTooLongEmail() {
            String longLocalPart = "a".repeat(244);
            String invalidEmail = longLocalPart + "@example.jp";
            
            DomainLayerErrorException exception = assertThrows(
                DomainLayerErrorException.class,
                () -> Email.of(invalidEmail)
            );
            
            assertEquals("メールアドレスの形式が正しくありません", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("異常系テスト - 不正な形式")
    class InvalidFormatCases {

        @ParameterizedTest
        @ValueSource(strings = {
            "invalid",
            "invalid@",
            "@example.jp",
            "user@",
            "user@@example.jp",
            "user@example",
            "user name@example.jp",
            "user@exam ple.jp",
            "user..name@example.jp",
            ".user@example.jp",
            "user.@example.jp",
            "user@.example.jp",
            "user@example..jp"
        })
        @DisplayName("不正なメールアドレス形式で例外がスローされる")
        void shouldThrowExceptionForInvalidFormat(String invalidEmail) {
            DomainLayerErrorException exception = assertThrows(
                DomainLayerErrorException.class,
                () -> Email.of(invalidEmail)
            );
            
            assertEquals("メールアドレスの形式が正しくありません", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("異常系テスト - 不正なドメイン")
    class InvalidDomainCases {

        @ParameterizedTest
        @ValueSource(strings = {
            "user@example.net",
            "user@example.org",
            "user@example.co.uk",
            "user@example.fr",
            "user@example.de",
            "user@example",
            "user@example.c",
            "user@example.jpn"
        })
        @DisplayName("許可されていないドメインで例外がスローされる")
        void shouldThrowExceptionForInvalidDomain(String invalidEmail) {
            DomainLayerErrorException exception = assertThrows(
                DomainLayerErrorException.class,
                () -> Email.of(invalidEmail)
            );
            
            assertEquals("メールアドレスの形式が正しくありません", exception.getMessage());
        }
    }
}
