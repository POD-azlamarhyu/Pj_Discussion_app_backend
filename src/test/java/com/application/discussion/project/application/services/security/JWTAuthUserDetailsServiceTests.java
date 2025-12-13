package com.application.discussion.project.application.services.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWTAuthUserDetailsService単体テスト")
public class JWTAuthUserDetailsServiceTests {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_LOGIN_ID = "testuser";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_PASSWORD = "encoded2Password";
    private static final String TEST_USERNAME = "Test User";
    private static final Integer TEST_ROLE_ID = 1;

    @Mock
    private UsersRepositoryInterface mockUsersRepository;

    @Mock
    private RolesRepositoryInterface mockRolesRepository;

    @InjectMocks
    private JWTAuthUserDetailsService jwtAuthUserDetailsService;

    private Users testUser;
    private Set<Roles> testRoles;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUserId(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setLoginId(TEST_LOGIN_ID);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setUsername(TEST_USERNAME);

        Roles testRole = new Roles();
        testRole.setRoleId(TEST_ROLE_ID);
        testRole.setRoleName("ROLE_USER");
        testRoles = Set.of(testRole);
    }

    @Test
    @DisplayName("メールアドレスでユーザーを正常にロードできること")
    void loadUserByUsernameWithEmailReturnsUserDetails() {
        when(mockUsersRepository.findByEmailOrLoginId(TEST_EMAIL))
            .thenReturn(Optional.of(testUser));
        when(mockRolesRepository.findUserRolesById(TEST_USER_ID))
            .thenReturn(testRoles);

        UserDetails actualUserDetails = jwtAuthUserDetailsService.loadUserByUsername(TEST_EMAIL);

        assertThat(actualUserDetails).isNotNull();
        assertThat(actualUserDetails.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(actualUserDetails.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(actualUserDetails.getAuthorities()).hasSize(1);
        verify(mockUsersRepository).findByEmailOrLoginId(TEST_EMAIL);
        verify(mockRolesRepository).findUserRolesById(TEST_USER_ID);
    }

    @Test
    @DisplayName("ログインIDでユーザーを正常にロードできること")
    void loadUserByUsernameWithLoginIdReturnsUserDetails() {
        when(mockUsersRepository.findByEmailOrLoginId(TEST_LOGIN_ID))
            .thenReturn(Optional.of(testUser));
        when(mockRolesRepository.findUserRolesById(TEST_USER_ID))
            .thenReturn(testRoles);

        UserDetails actualUserDetails = jwtAuthUserDetailsService.loadUserByUsername(TEST_LOGIN_ID);

        assertThat(actualUserDetails).isNotNull();
        assertThat(actualUserDetails.getUsername()).isEqualTo(TEST_USERNAME);
        verify(mockUsersRepository).findByEmailOrLoginId(TEST_LOGIN_ID);
        verify(mockRolesRepository).findUserRolesById(TEST_USER_ID);
    }

    @Test
    @DisplayName("ユーザーが見つからない場合ApplicationLayerExceptionがスローされること")
    void loadUserByUsernameUserNotFoundThrowsApplicationLayerException() {
        String nonExistentId = "nonexistent@example.com";
        when(mockUsersRepository.findByEmailOrLoginId(nonExistentId))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> jwtAuthUserDetailsService.loadUserByUsername(nonExistentId))
            .isInstanceOf(ApplicationLayerException.class)
            .hasMessageContaining("ユーザーが見つかりません")
            .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);

        verify(mockUsersRepository).findByEmailOrLoginId(nonExistentId);
    }

    @Test
    @DisplayName("複数のロールを持つユーザーを正常にロードできること")
    void loadUserByUsername_WithMultipleRoles_ReturnsUserDetailsWithAllRoles() {
        Roles adminRole = new Roles();
        adminRole.setRoleId(2);
        adminRole.setRoleName("ROLE_ADMIN");
        Set<Roles> multipleRoles = Set.of(testRoles.iterator().next(), adminRole);

        when(mockUsersRepository.findByEmailOrLoginId(TEST_EMAIL))
            .thenReturn(Optional.of(testUser));
        when(mockRolesRepository.findUserRolesById(TEST_USER_ID))
            .thenReturn(multipleRoles);

        UserDetails actualUserDetails = jwtAuthUserDetailsService.loadUserByUsername(TEST_EMAIL);

        assertThat(actualUserDetails.getAuthorities()).hasSize(2);
        verify(mockUsersRepository).findByEmailOrLoginId(TEST_EMAIL);
        verify(mockRolesRepository).findUserRolesById(TEST_USER_ID);
    }

    @Test
    @DisplayName("ロールが空のユーザーを正常にロードできること")
    void loadUserByUsername_WithNoRoles_ReturnsUserDetailsWithEmptyAuthorities() {
        Set<Roles> emptyRoles = Set.of();
        when(mockUsersRepository.findByEmailOrLoginId(TEST_EMAIL))
            .thenReturn(Optional.of(testUser));
        when(mockRolesRepository.findUserRolesById(TEST_USER_ID))
            .thenReturn(emptyRoles);

        UserDetails actualUserDetails = jwtAuthUserDetailsService.loadUserByUsername(TEST_EMAIL);

        assertThat(actualUserDetails).isNotNull();
        assertThat(actualUserDetails.getAuthorities()).isEmpty();
        verify(mockUsersRepository).findByEmailOrLoginId(TEST_EMAIL);
        verify(mockRolesRepository).findUserRolesById(TEST_USER_ID);
    }
}
