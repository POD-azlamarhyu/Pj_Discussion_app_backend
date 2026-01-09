package com.application.discussion.project.domain.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.services.users.UserAuthenticationDomainServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("ユーザ認証ドメインサービスのテスト")
public class UserAuthenticationDomainServiceImplTests {

    @Mock
    private UsersRepositoryInterface usersRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private JWTAuthUserDetails userDetails;

    @InjectMocks
    private UserAuthenticationDomainServiceImpl userAuthenticationDomainService;

    private static final UUID TEST_USER_UUID = UUID.randomUUID();
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "pAssworD12345";

    private User mockUser;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        mockUser = User.of(
            TEST_USER_UUID,
            TEST_USERNAME,
            TEST_EMAIL,
            TEST_PASSWORD,
            testDateTime,
            testDateTime
        );
    }

    @Test
    @DisplayName("認証されたユーザが存在し、正常にユーザ情報が取得できること")
    void testGetAuthenticatedUserSuccessfully() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUserId()).thenReturn(TEST_USER_UUID);
            when(usersRepository.findById(TEST_USER_UUID)).thenReturn(Optional.of(mockUser));

            User actualResult = userAuthenticationDomainService.getAuthenticatedUser();

            assertNotNull(actualResult);
            assertEquals(TEST_USER_UUID, actualResult.getUserId());
            assertEquals(TEST_USERNAME, actualResult.getUserName().value());
            assertEquals(TEST_EMAIL, actualResult.getEmail().value());
            verify(usersRepository, times(1)).findById(TEST_USER_UUID);
        }
    }

    @Test
    @DisplayName("認証情報がnullの場合に例外が投げられること")
    void testThrowsExceptionWhenAuthenticationIsNull() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            DomainLayerErrorException actualException = assertThrows(
                DomainLayerErrorException.class,
                () -> userAuthenticationDomainService.getAuthenticatedUser()
            );

            assertEquals("認証されたユーザーが見つかりません", actualException.getMessage());
            assertEquals(401, actualException.getCode().value());
            verify(usersRepository, never()).findById(any(UUID.class));
        }
    }

    @Test
    @DisplayName("ユーザが認証されていない場合に例外が投げられること")
    void testThrowsExceptionWhenUserNotAuthenticated() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            DomainLayerErrorException actualException = assertThrows(
                DomainLayerErrorException.class,
                () -> userAuthenticationDomainService.getAuthenticatedUser()
            );

            assertEquals("認証されたユーザーが見つかりません", actualException.getMessage());
            assertEquals(401, actualException.getCode().value());
            verify(usersRepository, never()).findById(any(UUID.class));
        }
    }

    @Test
    @DisplayName("認証されたユーザがDBに存在しない場合に例外が投げられること")
    void testThrowsExceptionWhenUserNotFoundInRepository() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUserId()).thenReturn(TEST_USER_UUID);
            when(usersRepository.findById(TEST_USER_UUID)).thenReturn(Optional.empty());

            DomainLayerErrorException actualException = assertThrows(
                DomainLayerErrorException.class,
                () -> userAuthenticationDomainService.getAuthenticatedUser()
            );

            assertEquals("認証されたユーザーが見つかりません", actualException.getMessage());
            assertEquals(401, actualException.getCode().value());
            verify(usersRepository, times(1)).findById(TEST_USER_UUID);
        }
    }

    @Test
    @DisplayName("SecurityContextから取得した認証情報が正しく処理されること")
    void testAuthenticationFromSecurityContextProcessedCorrectly() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUserId()).thenReturn(TEST_USER_UUID);
            when(usersRepository.findById(TEST_USER_UUID)).thenReturn(Optional.of(mockUser));

            User actualResult = userAuthenticationDomainService.getAuthenticatedUser();

            assertNotNull(actualResult);
            verify(securityContext, times(1)).getAuthentication();
            verify(authentication, times(1)).isAuthenticated();
            verify(authentication, times(1)).getPrincipal();
            verify(userDetails, times(1)).getUserId();
        }
    }

    @Test
    @DisplayName("リポジトリから取得したユーザ情報が正しく返却されること")
    void testUserFromRepositoryReturnedCorrectly() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUserId()).thenReturn(TEST_USER_UUID);
            when(usersRepository.findById(TEST_USER_UUID)).thenReturn(Optional.of(mockUser));

            User actualResult = userAuthenticationDomainService.getAuthenticatedUser();

            assertNotNull(actualResult);
            assertFalse(actualResult.getIsDeleted());
            assertEquals(TEST_USER_UUID, mockUser.getUserId());
            assertEquals(testDateTime, actualResult.getCreatedAt());
            assertEquals(testDateTime, actualResult.getUpdatedAt());
        }
    }
}
