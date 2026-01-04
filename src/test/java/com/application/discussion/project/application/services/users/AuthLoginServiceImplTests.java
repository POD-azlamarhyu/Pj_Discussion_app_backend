package com.application.discussion.project.application.services.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.anything;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.application.services.security.JWTUtils;
import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthLoginServiceのテスト")
public class AuthLoginServiceImplTests {

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private JWTUtils mockJwtUtils;

    @Mock
    private Authentication mockAuthentication;

    @InjectMocks
    private AuthLoginService authLoginService;

    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_PASSWORD = "pAssword12345";
    private static final String TEST_USERNAME = "testuser";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final Integer TEST_ROLE_ID = 1;
    protected static final String TEST_ROLE = "ROLE_USER";
    protected JWTAuthUserDetails mockUserDetails;
    protected ResponseCookie mockResponseCookie;
    protected Users mockUser = new Users();
    protected Role mockRole;
    protected ResponseEntity<LoginResponse> mockResponseEntity;
    protected Set<Role> roles = new HashSet<>();
    protected RoleType roleType = RoleNormalUser.create();

    protected  LoginRequest loginRequest;


    @Nested
    @DisplayName("正常系テスト")
    class SuccessTests {

        @BeforeEach
        void setUp() {
            mockUser.setUserId(TEST_USER_ID);
            mockUser.setUsername(TEST_USERNAME);
            mockUser.setEmail(TEST_EMAIL);
            mockUser.setPassword(TEST_PASSWORD);

            mockRole =  Role.of(
                TEST_ROLE_ID, 
                TEST_ROLE, 
                null, 
                null, 
                null,
                roleType
            );

            roles.add(mockRole);
            mockUserDetails = JWTAuthUserDetails.build(mockUser, roles);

            mockResponseCookie = ResponseCookie.from("jwt", "test-jwt-token")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();
            mockResponseEntity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, mockResponseCookie.toString())
                .body(new LoginResponse(
                    TEST_USER_ID,
                    TEST_USERNAME,
                    List.of(TEST_ROLE)
                ));
        }

        @Test
        @DisplayName("メールアドレスでのログインが成功し、正しいレスポンスが返される")
        void loginSuccessWithEmail() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockJwtUtils.generateJwtCookie(any(JWTAuthUserDetails.class))).thenReturn(mockResponseCookie);


            ResponseEntity<LoginResponse> actualResponse = authLoginService.service(loginRequest);

            
            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertEquals(actualResponse.getStatusCode(), HttpStatus.OK);
            assertThat(actualResponse.getHeaders().get(HttpHeaders.SET_COOKIE))
                .containsExactly(mockResponseCookie.toString());
            assertEquals(actualResponse.getHeaders().get(HttpHeaders.SET_COOKIE).getFirst(), mockResponseCookie.toString());
            assertThat(actualResponse).isNotNull();
            assertNotNull(actualResponse);
            assertEquals(TEST_USER_ID, actualResponse.getBody().getUserId());
            assertThat(actualResponse.getBody().getUserId()).isEqualTo(TEST_USER_ID);
            assertThat(actualResponse.getBody().getUsername()).isEqualTo(TEST_USERNAME);
            assertEquals(TEST_USERNAME, actualResponse.getBody().getUsername());
            assertThat(actualResponse.getBody().getRoles()).isNotNull();
            assertEquals(TEST_ROLE,actualResponse.getBody().getRoles().getFirst());
        }

        @Test
        @DisplayName("ログインIDでのログインが成功し、正しいレスポンスが返される")
        void loginSuccessWithLoginId() {
            String testLoginId = "testloginid";
            LoginRequest loginRequest = new LoginRequest(
                testLoginId,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockJwtUtils.generateJwtCookie(mockUserDetails)).thenReturn(mockResponseCookie);

            ResponseEntity<LoginResponse> actualResponse = authLoginService.service(loginRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(actualResponse.getBody()).isNotNull();
            assertThat(actualResponse.getBody().getUserId()).isEqualTo(TEST_USER_ID);
        }

        @Test
        @DisplayName("複数のロールを持つユーザーのログインが成功する")
        void loginSuccessWithMultipleRoles() {
            Set<Role> multiRoles = new HashSet<>();
            Role adminRole = Role.of(
                2, 
                "ROLE_ADMIN", 
                null,
                null, 
                null,
                roleType
            );

            multiRoles.add(mockRole);
            multiRoles.add(adminRole);
            JWTAuthUserDetails multiRoleUserDetails = JWTAuthUserDetails.build(mockUser, multiRoles);

            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(multiRoleUserDetails);
            when(mockJwtUtils.generateJwtCookie(multiRoleUserDetails)).thenReturn(mockResponseCookie);

            ResponseEntity<LoginResponse> actualResponse = authLoginService.service(loginRequest);

            assertThat(actualResponse.getBody()).isNotNull();
            assertThat(actualResponse.getBody().getRoles()).size().isEqualTo(2);
            assertThat(actualResponse.getBody().getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
        }
    }

    @Nested
    @DisplayName("異常系テスト - 認証エラー")
    class AuthenticationErrorTests {

        @Test
        @DisplayName("認証時にAuthenticationExceptionが発生した場合、ApplicationLayerExceptionがスローされる")
        void throwExceptionWhenAuthenticationFails() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                "fisnk3i"
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

            assertThatThrownBy(() -> authLoginService.service(loginRequest))
                .isInstanceOf(ApplicationLayerException.class)
                .hasMessage("認証エラーが発生しました");
        }

        @Test
        @DisplayName("認証結果がnullの場合、ApplicationLayerExceptionがスローされる")
        void throwExceptionWhenAuthenticationIsNull() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

            assertThatThrownBy(() -> authLoginService.service(loginRequest))
                .isInstanceOf(ApplicationLayerException.class)
                .hasMessage("認証に失敗しました");
        }

        @Test
        @DisplayName("認証結果が未認証の場合、ApplicationLayerExceptionがスローされる")
        void throwExceptionWhenAuthenticationIsNotAuthenticated() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(false);

            assertThatThrownBy(() -> authLoginService.service(loginRequest))
                .isInstanceOf(ApplicationLayerException.class)
                .hasMessage("認証に失敗しました");
        }
    }

    @Nested
    @DisplayName("レスポンス検証テスト")
    class ResponseValidationTests {

        @BeforeEach
        void setUp() {
            mockUser.setUserId(TEST_USER_ID);
            mockUser.setUsername(TEST_USERNAME);
            mockUser.setEmail(TEST_EMAIL);
            mockUser.setPassword(TEST_PASSWORD);
            
            mockRole =  Role.of(
                TEST_ROLE_ID, 
                TEST_EMAIL, 
                null, 
                null, 
                null,
                roleType
            );
            roles.add(mockRole);
            mockUserDetails = JWTAuthUserDetails.build(mockUser, roles);

            mockResponseCookie = ResponseCookie.from("jwt", "test-jwt-token")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();
            mockResponseEntity = ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, mockResponseCookie.toString())
                .body(new LoginResponse(
                    TEST_USER_ID,
                    TEST_USERNAME,
                    List.of(TEST_ROLE)
                ));
        }

        @Test
        @DisplayName("レスポンスのHTTPステータスがOKである")
        void responseStatusIsOk() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockJwtUtils.generateJwtCookie(mockUserDetails)).thenReturn(mockResponseCookie);

            ResponseEntity<LoginResponse> actualResponse = authLoginService.service(loginRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("レスポンスヘッダーにJWT Cookieが含まれている")
        void responseContainsJwtCookie() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );
            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockJwtUtils.generateJwtCookie(mockUserDetails)).thenReturn(mockResponseCookie);

            ResponseEntity<LoginResponse> actualResponse = authLoginService.service(loginRequest);

            assertThat(actualResponse.getHeaders().get(HttpHeaders.SET_COOKIE))
                .isNotNull()
                .isNotEmpty();
        }

        @Test
        @DisplayName("レスポンスボディにユーザー情報が正しく設定されている")
        void responseBodyContainsCorrectUserInfo() {
            LoginRequest loginRequest = new LoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD
            );

            when(mockAuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockJwtUtils.generateJwtCookie(mockUserDetails)).thenReturn(mockResponseCookie);

            ResponseEntity<LoginResponse> actualResponse = authLoginService.service(loginRequest);

            LoginResponse actualBody = actualResponse.getBody();
            assertThat(actualBody).isNotNull();
            assertThat(actualBody.getUserId()).isEqualTo(TEST_USER_ID);
            assertThat(actualBody.getUsername()).isEqualTo(TEST_USERNAME);
            assertThat(actualBody.getRoles()).isNotNull();
            assertThat(actualBody.getRoles()).hasSize(1);
        }
    }
}
