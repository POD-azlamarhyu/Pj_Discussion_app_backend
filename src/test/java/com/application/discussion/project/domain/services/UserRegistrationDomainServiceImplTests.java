package com.application.discussion.project.domain.services;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.services.users.UserRegistrationDomainServiceImpl;
import com.application.discussion.project.domain.valueobjects.users.Email;

@DisplayName("UserRegistrationDomainServiceImpl ドメインサービスのテスト")
@ExtendWith(MockitoExtension.class)
class UserRegistrationDomainServiceImplTests {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String DUPLICATE_EMAIL = "duplicate@example.com";

    @Mock
    private UsersRepositoryInterface mockUsersRepository;

    @InjectMocks
    private UserRegistrationDomainServiceImpl userRegistrationDomainService;

    @Test
    @DisplayName("正常系：DBに存在しないメールアドレスの場合、例外がスローされずメソッドが正常終了すること")
    void ensureEmailIsUnique_withNonExistingEmail_completesSuccessfully() {
        Email email = Email.of(TEST_EMAIL);
        when(mockUsersRepository.existsByEmail(email)).thenReturn(false);

        assertThatCode(() -> userRegistrationDomainService.ensureEmailIsUnique(email))
            .doesNotThrowAnyException();

        verify(mockUsersRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("異常系：DBに重複したメールアドレスが存在する場合、DomainLayerErrorExceptionがスローされること")
    void ensureEmailIsUnique_withDuplicateEmail_throwsDomainLayerErrorException() {
        Email email = Email.of(DUPLICATE_EMAIL);
        when(mockUsersRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> userRegistrationDomainService.ensureEmailIsUnique(email))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessageContaining("このメールアドレスは既に登録されています");

        verify(mockUsersRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("異常系：メールアドレス重複時のエラーステータスがCONFLICT(409)であること")
    void ensureEmailIsUnique_withDuplicateEmail_returnsConflictStatus() {
        Email email = Email.of(DUPLICATE_EMAIL);
        when(mockUsersRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> userRegistrationDomainService.ensureEmailIsUnique(email))
            .isInstanceOf(DomainLayerErrorException.class)
            .extracting("status")
            .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("正常系：リポジトリのexistsByEmailメソッドが正しく呼び出されること")
    void ensureEmailIsUnique_callsRepositoryWithCorrectEmail() {
        Email email = Email.of(TEST_EMAIL);
        when(mockUsersRepository.existsByEmail(email)).thenReturn(false);

        userRegistrationDomainService.ensureEmailIsUnique(email);

        verify(mockUsersRepository).existsByEmail(email);
    }
}
