package com.application.discussion.project.domain.valueobjects.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;


@DisplayName("EmailOrLoginIdのテスト")
class EmailOrLoginIdTest {

    @Nested
    @DisplayName("validateEmailのテスト")
    class ValidateEmailTest {

        @Nested
        @DisplayName("正常系")
        class Success {

            @Test
            @DisplayName("有効な.jpドメインのメールアドレスで生成できる")
            void shouldCreateWithValidJpDomainEmail() {
                // Arrange
                String validEmail = "test@example.jp";

                // Act
                EmailOrLoginId actual = EmailOrLoginId.of(validEmail);

                // Assert
                assertThat(actual.value()).isEqualTo(validEmail);
            }

            @Test
            @DisplayName("有効な.comドメインのメールアドレスで生成できる")
            void shouldCreateWithValidComDomainEmail() {
                // Arrange
                String validEmail = "test@example.com";

                // Act
                EmailOrLoginId actual = EmailOrLoginId.of(validEmail);

                // Assert
                assertThat(actual.value()).isEqualTo(validEmail);
            }

            @Test
            @DisplayName("特殊文字を含む有効なメールアドレスで生成できる")
            void shouldCreateWithSpecialCharactersEmail() {
                // Arrange
                String validEmail = "test.user+tag@example.jp";

                // Act
                EmailOrLoginId actual = EmailOrLoginId.of(validEmail);

                // Assert
                assertThat(actual.value()).isEqualTo(validEmail);
            }

            @Test
            @DisplayName("サブドメインを含む有効なメールアドレスで生成できる")
            void shouldCreateWithSubdomainEmail() {
                // Arrange
                String validEmail = "user@mail.example.com";

                // Act
                EmailOrLoginId actual = EmailOrLoginId.of(validEmail);

                // Assert
                assertThat(actual.value()).isEqualTo(validEmail);
            }
        }

        @Nested
        @DisplayName("異常系")
        class Failure {

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"   ", "\t", "\n"})
            @DisplayName("null、空文字、空白のみの場合は例外をスローする")
            void shouldThrowExceptionWhenBlank(String invalidInput) {
                // Act & Assert
                assertThatThrownBy(() -> EmailOrLoginId.of(invalidInput))
                        .isInstanceOf(DomainLayerErrorException.class)
                        .hasMessageContaining("メールアドレスまたはログインIDは必須項目です");
            }

            @Test
            @DisplayName("255文字を超えるメールアドレスは例外をスローする")
            void shouldThrowExceptionWhenExceedsMaxLength() {
                // Arrange
                String longLocalPart = "a".repeat(250);
                String invalidEmail = longLocalPart + "@example.jp";

                // Act & Assert
                assertThatThrownBy(() -> EmailOrLoginId.of(invalidEmail))
                        .isInstanceOf(DomainLayerErrorException.class)
                        .hasMessageContaining("メールアドレスは最大255文字までです");
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "invalid-email",
                    "missing@domain",
                    "@nodomain.com",
                    "spaces in@email.jp",
                    "double@@at.com"
            })
            @DisplayName("無効なメールアドレス形式の場合は例外をスローする")
            void shouldThrowExceptionWhenInvalidEmailFormat(String invalidEmail) {
                // Act & Assert
                assertThatThrownBy(() -> EmailOrLoginId.of(invalidEmail))
                        .isInstanceOf(DomainLayerErrorException.class)
                        .hasMessageContaining("無効なメールアドレス形式です");
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "test@example.org",
                    "test@example.net",
                    "test@example.co.uk",
                    "test@example.io"
            })
            @DisplayName("許可されていないドメインの場合は例外をスローする")
            void shouldThrowExceptionWhenBannedDomain(String invalidEmail) {
                // Act & Assert
                assertThatThrownBy(() -> EmailOrLoginId.of(invalidEmail))
                        .isInstanceOf(DomainLayerErrorException.class)
                        .hasMessageContaining("無効なメールアドレス形式です");
            }
        }
    }

    @Nested
    @DisplayName("valueメソッドのテスト")
    class ValueMethodTest {

        @Test
        @DisplayName("生成時に渡した値がそのまま取得できる")
        void shouldReturnOriginalValue() {
            // Arrange
            String expectedEmail = "test@example.jp";
            EmailOrLoginId emailOrLoginId = EmailOrLoginId.of(expectedEmail);

            // Act
            String actualValue = emailOrLoginId.value();

            // Assert
            assertThat(actualValue).isEqualTo(expectedEmail);
        }
    }
}
