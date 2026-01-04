package com.application.discussion.project.application.services.users;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.SignUpRequest;
import com.application.discussion.project.application.dtos.users.SignUpResponse;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.services.users.RoleRegistrationDomainService;
import com.application.discussion.project.domain.services.users.UserRegistrationDomainService;
import com.application.discussion.project.domain.valueobjects.users.Email;

@DisplayName("UserRegistrationServiceImpl アプリケーションサービスのテスト")
@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTests {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "pAssword1234Q";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_HASHED_PASSWORD = "$2a$10$hashedpassword";
    private static final String TEST_LOGIN_ID = "login-123";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

    @Mock
    private UsersRepositoryInterface mockUsersRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRegistrationDomainService mockUserRegistrationDomainService;

    @Mock
    private RoleRegistrationDomainService mockRoleRegistrationDomainService;

    @Mock
    private RolesRepositoryInterface mockRolesRepository;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest(TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME);
    }

    @Test
    @DisplayName("正常系：有効なリクエストでユーザー登録が成功すること")
    void service_withValidRequest_returnsSignUpResponse() {
        User mockSavedUser = createMockSavedUser();
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(TEST_USER_ID.toString());
        assertThat(actual.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(actual.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(actual.getCreatedAt()).isEqualTo(TEST_CREATED_AT);
        verify(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        verify(mockUsersRepository).save(any(User.class));
    }

    @Test
    @DisplayName("異常系：重複したメールアドレスで登録時にDomainLayerErrorExceptionがスローされること")
    void service_withDuplicateEmail_throwsDomainLayerErrorException() {
        doThrow(new DomainLayerErrorException("このメールアドレスは既に登録されています", HttpStatus.CONFLICT, HttpStatusCode.valueOf(409)))
            .when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));

        assertThatThrownBy(() -> userRegistrationService.service(signUpRequest))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessageContaining("このメールアドレスは既に登録されています");
    }

    @Test
    @DisplayName("異常系：パスワードハッシュ検証失敗時にApplicationLayerExceptionがスローされること")
    void service_whenPasswordHashVerificationFails_throwsApplicationLayerException() {
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(false);

        assertThatThrownBy(() -> userRegistrationService.service(signUpRequest))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessageContaining("不明なエラーが発生しました")
            .extracting("status")
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private User createMockSavedUser() {
        return User.reBuild(
            TEST_USER_ID,
            TEST_USERNAME,
            TEST_EMAIL,
            TEST_LOGIN_ID,
            TEST_HASHED_PASSWORD,
            TEST_CREATED_AT,
            null,
            null,
            Boolean.TRUE,
            Boolean.FALSE
        );
    }
}
