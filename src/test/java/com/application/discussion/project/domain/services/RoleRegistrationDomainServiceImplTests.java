package com.application.discussion.project.domain.services;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.services.users.RoleRegistrationDomainServiceImpl;
import com.application.discussion.project.domain.valueobjects.users.RoleAdmin;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleRegistrationDomainServiceImpl クラスのテスト")
class RoleRegistrationDomainServiceImplTests {

    @Mock
    private RolesRepositoryInterface mockRolesRepository;

    @InjectMocks
    private RoleRegistrationDomainServiceImpl actualService;

    private RoleType actualRoleType;
    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        actualRoleType = RoleAdmin.create();
        mockUser = mock(User.class);
        mockRole = mock(Role.class);
    }

    @Test
    @DisplayName("インスタンスが正常に生成されること")
    void instanceShouldBeCreatedSuccessfully() {
        assertThat(actualService).isNotNull();
    }

    @Test
    @DisplayName("ensureRoleIsUnique()でロールが存在する場合にtrueを返すこと")
    void ensureRoleIsUniqueWithExistingRoleShouldReturnTrue() {
        when(mockRolesRepository.existsByRoleName(any(RoleType.class))).thenReturn(Boolean.TRUE);

        Boolean actualResult = actualService.ensureRoleIsUnique(actualRoleType);

        assertThat(actualResult).isTrue();
        verify(mockRolesRepository, times(1)).existsByRoleName(actualRoleType);
    }

    @Test
    @DisplayName("ensureRoleIsUnique()でロールが存在しない場合にfalseを返すこと")
    void ensureRoleIsUniqueWithNonExistingRoleShouldReturnFalse() {
        when(mockRolesRepository.existsByRoleName(any(RoleType.class))).thenReturn(Boolean.FALSE);

        Boolean actualResult = actualService.ensureRoleIsUnique(actualRoleType);

        assertThat(actualResult).isFalse();
        verify(mockRolesRepository, times(1)).existsByRoleName(actualRoleType);
    }

    @Test
    @DisplayName("ensureRoleIsUnique()で異なるロールタイプでも正常に動作すること")
    void ensureRoleIsUniqueWithDifferentRoleTypeShouldWork() {
        RoleType normalUserRole = RoleNormalUser.create();
        when(mockRolesRepository.existsByRoleName(any(RoleType.class))).thenReturn(Boolean.FALSE);

        Boolean actualResult = actualService.ensureRoleIsUnique(normalUserRole);

        assertThat(actualResult).isFalse();
        verify(mockRolesRepository, times(1)).existsByRoleName(normalUserRole);
    }

    @Test
    @DisplayName("ensureUserRolesAreValid()でユーザロールが存在する場合にtrueを返すこと")
    void ensureUserRolesAreValidWithExistingUserRoleShouldReturnTrue() {
        when(mockRolesRepository.existsByUserAndRoleBoolean(any(User.class), any(Role.class)))
                .thenReturn(Boolean.TRUE);

        Boolean actualResult = actualService.ensureUserRolesAreValid(mockUser, mockRole);

        assertThat(actualResult).isTrue();
        verify(mockRolesRepository, times(1)).existsByUserAndRoleBoolean(mockUser, mockRole);
    }

    @Test
    @DisplayName("ensureUserRolesAreValid()でユーザロールが存在しない場合にfalseを返すこと")
    void ensureUserRolesAreValidWithNonExistingUserRoleShouldReturnFalse() {
        when(mockRolesRepository.existsByUserAndRoleBoolean(any(User.class), any(Role.class)))
                .thenReturn(Boolean.FALSE);

        Boolean actualResult = actualService.ensureUserRolesAreValid(mockUser, mockRole);

        assertThat(actualResult).isFalse();
        verify(mockRolesRepository, times(1)).existsByUserAndRoleBoolean(mockUser, mockRole);
    }

    @Test
    @DisplayName("ensureUserRolesAreValid()でnullパラメータでも正常に動作すること")
    void ensureUserRolesAreValidWithNullParametersShouldWork() {
        when(mockRolesRepository.existsByUserAndRoleBoolean(any(), any())).thenReturn(Boolean.FALSE);

        Boolean actualResult = actualService.ensureUserRolesAreValid(null, null);

        assertThat(actualResult).isFalse();
        verify(mockRolesRepository, times(1)).existsByUserAndRoleBoolean(null, null);
    }

    @Test
    @DisplayName("リポジトリが正しく注入されていること")
    void repositoryShouldBeInjectedCorrectly() {
        assertThat(mockRolesRepository).isNotNull();
    }
}
