package com.application.discussion.project.application.services.users;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.services.users.RoleRegistrationDomainService;
import com.application.discussion.project.domain.services.users.UserRegistrationDomainServiceImpl;
import com.application.discussion.project.domain.valueobjects.users.Email;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

@DisplayName("UserRegistrationServiceImpl アプリケーションサービスのテスト")
@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTests {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "pAssword1234Q";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_HASHED_PASSWORD = "$2a$10$hashedpassword";
    private static final String TEST_LOGIN_ID = "login-123";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final Integer TEST_ROLE_ID = 1;
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    private static final String DUPLICATE_EMAIL_ERROR_MESSAGE = "このメールアドレスは既に登録されています";
    private static final String INVALID_PASSWORD_ERROR_MESSAGE = "パスワードの形式が不正です";
    private static final String INVALID_EMAIL_ERROR_MESSAGE = "メールアドレスの形式が正しくありません";
    private static final String EMPTY_USERNAME_ERROR_MESSAGE = "ユーザー名が空です";
    private static final String EMPTY_EMAIL_ERROR_MESSAGE = "メールアドレスが空です";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String EMPTY_STRING = "";
    private static final String WEAK_PASSWORD = "weak";

    @Mock
    private UsersRepositoryInterface mockUsersRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRegistrationDomainServiceImpl mockUserRegistrationDomainService;

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
    void service_withValidRequest_registersUserSuccessfully() {
        User mockSavedUser = createMockSavedUser();
        Role mockRole = createMockRole();
        
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);
        when(mockRoleRegistrationDomainService.ensureRoleIsUnique(any(RoleType.class))).thenReturn(true);
        when(mockRolesRepository.findByRoleName(any(RoleType.class))).thenReturn(mockRole);
        when(mockRoleRegistrationDomainService.ensureUserRolesAreValid(any(User.class), any(Role.class))).thenReturn(true);
        doNothing().when(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(TEST_USER_ID.toString());
        assertThat(actual.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(actual.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(actual.getCreatedAt()).isEqualTo(TEST_CREATED_AT);
        verify(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        verify(mockUsersRepository).save(any(User.class));
        verify(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));
    }

    @Test
    @DisplayName("異常系：重複したメールアドレスで登録時にDomainLayerErrorExceptionがスローされること")
    void service_withDuplicateEmail_throwsDomainLayerErrorException() {
        doThrow(new DomainLayerErrorException(DUPLICATE_EMAIL_ERROR_MESSAGE, HttpStatus.CONFLICT, HttpStatusCode.valueOf(409)))
            .when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));

        assertThatThrownBy(() -> userRegistrationService.service(signUpRequest))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessageContaining(DUPLICATE_EMAIL_ERROR_MESSAGE);
        
        verify(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
    }

    @Test
    @DisplayName("異常系：不正なパスワードでのユーザー登録時にDomainLayerErrorExceptionがスローされること")
    void service_withInvalidPassword_throwsDomainLayerErrorException() {
        SignUpRequest invalidPasswordRequest = new SignUpRequest(TEST_EMAIL, WEAK_PASSWORD, TEST_USERNAME);
        
        doThrow(new DomainLayerErrorException(INVALID_PASSWORD_ERROR_MESSAGE, HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400)))
            .when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> userRegistrationService.service(invalidPasswordRequest)
        );

        assertThat(exception.getMessage()).contains(INVALID_PASSWORD_ERROR_MESSAGE);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系：不正な形式のメールアドレス登録時にDomainLayerErrorExceptionがスローされること")
    void service_withInvalidEmailFormat_throwsDomainLayerErrorException() {
        SignUpRequest invalidEmailRequest = new SignUpRequest(INVALID_EMAIL, TEST_PASSWORD, TEST_USERNAME);
        
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> userRegistrationService.service(invalidEmailRequest)
        );

        assertThat(exception.getMessage()).contains(INVALID_EMAIL_ERROR_MESSAGE);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("異常系：ユーザーネームが空文字の登録時にDomainLayerErrorExceptionがスローされること")
    void service_withEmptyUsername_throwsDomainLayerErrorException() {
        SignUpRequest emptyUsernameRequest = new SignUpRequest(TEST_EMAIL, TEST_PASSWORD, EMPTY_STRING);
        
        doThrow(new DomainLayerErrorException(EMPTY_USERNAME_ERROR_MESSAGE, HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400)))
            .when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));

        assertThatThrownBy(() -> userRegistrationService.service(emptyUsernameRequest))
            .isInstanceOf(DomainLayerErrorException.class);
    }

    @Test
    @DisplayName("異常系：メールアドレスが空文字もしくはNullでの登録時にDomainLayerErrorExceptionがスローされること")
    void service_withEmptyOrNullEmail_throwsDomainLayerErrorException() {
        SignUpRequest emptyEmailRequest = new SignUpRequest(EMPTY_STRING, TEST_PASSWORD, TEST_USERNAME);
        
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> userRegistrationService.service(emptyEmailRequest)
        );

        assertThat(exception.getMessage()).contains(EMPTY_EMAIL_ERROR_MESSAGE);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("正常系：パスワードが正常にハッシュ化されて正常にユーザー登録できること")
    void service_withPasswordHashedSuccessfully_registersUserSuccessfully() {
        User mockSavedUser = createMockSavedUser();
        Role mockRole = createMockRole();
        
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);
        when(mockRoleRegistrationDomainService.ensureRoleIsUnique(any(RoleType.class))).thenReturn(true);
        when(mockRolesRepository.findByRoleName(any(RoleType.class))).thenReturn(mockRole);
        when(mockRoleRegistrationDomainService.ensureUserRolesAreValid(any(User.class), any(Role.class))).thenReturn(true);
        doNothing().when(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        verify(mockPasswordEncoder).encode(anyString());
        verify(mockPasswordEncoder).matches(TEST_PASSWORD, TEST_HASHED_PASSWORD);
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

    @Test
    @DisplayName("正常系：ロールが既に登録されている状態で正常にユーザーが登録できること")
    void service_withExistingRole_registersUserSuccessfully() {
        User mockSavedUser = createMockSavedUser();
        Role mockRole = createMockRole();
        
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);
        when(mockRoleRegistrationDomainService.ensureRoleIsUnique(any(RoleType.class))).thenReturn(true);
        when(mockRolesRepository.findByRoleName(any(RoleType.class))).thenReturn(mockRole);
        when(mockRoleRegistrationDomainService.ensureUserRolesAreValid(any(User.class), any(Role.class))).thenReturn(true);
        doNothing().when(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        verify(mockRolesRepository).findByRoleName(any(RoleType.class));
    }

    @Test
    @DisplayName("正常系：ロールが登録されていない状態で正常にユーザーが登録できること")
    void service_withoutExistingRole_registersUserSuccessfully() {
        User mockSavedUser = createMockSavedUser();
        Role mockRole = createMockRole();
        
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);
        when(mockRoleRegistrationDomainService.ensureRoleIsUnique(any(RoleType.class))).thenReturn(false);
        when(mockRolesRepository.saveRole(any(Role.class))).thenReturn(mockRole);
        when(mockRoleRegistrationDomainService.ensureUserRolesAreValid(any(User.class), any(Role.class))).thenReturn(true);
        doNothing().when(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        verify(mockRolesRepository).saveRole(any(Role.class));
    }

    @Test
    @DisplayName("正常系：ユーザーとロールがマッピング済みの状態で正常にユーザーが登録できていること")
    void service_withUserRoleMappingExists_registersUserSuccessfully() {
        User mockSavedUser = createMockSavedUser();
        Role mockRole = createMockRole();
        
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);
        when(mockRoleRegistrationDomainService.ensureRoleIsUnique(any(RoleType.class))).thenReturn(true);
        when(mockRolesRepository.findByRoleName(any(RoleType.class))).thenReturn(mockRole);
        when(mockRoleRegistrationDomainService.ensureUserRolesAreValid(any(User.class), any(Role.class))).thenReturn(true);
        doNothing().when(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        verify(mockRoleRegistrationDomainService).ensureUserRolesAreValid(any(User.class), any(Role.class));
        verify(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));
    }

    @Test
    @DisplayName("正常系：ユーザーとロールがマッピングされていない状態で正常にユーザーが登録できていること")
    void service_withoutUserRoleMapping_registersUserSuccessfully() {
        User mockSavedUser = createMockSavedUser();
        Role mockRole = createMockRole();
        
        doNothing().when(mockUserRegistrationDomainService).ensureEmailIsUnique(any(Email.class));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(TEST_HASHED_PASSWORD);
        when(mockPasswordEncoder.matches(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(mockUsersRepository.save(any(User.class))).thenReturn(mockSavedUser);
        when(mockRoleRegistrationDomainService.ensureRoleIsUnique(any(RoleType.class))).thenReturn(true);
        when(mockRolesRepository.findByRoleName(any(RoleType.class))).thenReturn(mockRole);
        when(mockRoleRegistrationDomainService.ensureUserRolesAreValid(any(User.class), any(Role.class))).thenReturn(true);
        doNothing().when(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));

        SignUpResponse actual = userRegistrationService.service(signUpRequest);

        assertThat(actual).isNotNull();
        verify(mockRolesRepository).saveUserRoleMapping(any(User.class), any(Role.class));
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

    private Role createMockRole() {
        return Role.of(
            TEST_ROLE_ID,
            "NORMAL_USER",
            TEST_CREATED_AT,
            null,
            null,
            RoleNormalUser.defaultNormalUserRole()
        );
    }
}
