package com.application.discussion.project.application.services.security;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.infrastructure.models.users.Users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWTUtils テストクラス")
public class JWTUtilsTests {

    private static final String TEST_JWT_SECRET = "dGVzdFNlY3JldEtleUZvckpXVFRva2VuVGhhdElzTG9uZ0Vub3VnaEZvckhtYWNTaGE1MTJBbGdvcml0aG0=";
    private static final Long TEST_EXPIRATION_MS = 3600000L;
    private static final String TEST_COOKIE_NAME = "jwt";
    private static final String TEST_COOKIE_PATH = "/";
    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_LOGIN_ID = "testuser";
    private static final String TEST_PASSWORD = "encoded4Password";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "Test User";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TEST_ROLE_NAME = "ROLE_USER";
    private static final Boolean TEST_IS_ACTIVE = true;
    private static final Boolean TEST_IS_DELETED = false;
    private static final String TEST_ALGORITHM = "HmacSHA384";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_JWT_SECRET));

    @InjectMocks
    private JWTUtils jwtUtils;

    private JWTAuthUserDetails mockUserDetails;
    private HttpServletRequest mockRequest;

    private Users testUser;
    private Set<Role> testRoles;
    private Role testRole;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenSecret", TEST_JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenExpirationMs", TEST_EXPIRATION_MS);
        ReflectionTestUtils.setField(jwtUtils, "jwtCookieName", TEST_COOKIE_NAME);
        ReflectionTestUtils.setField(jwtUtils, "jwtCookiesPath", TEST_COOKIE_PATH);
        ReflectionTestUtils.setField(jwtUtils, "isCookieHttpOnly", true);
        ReflectionTestUtils.setField(jwtUtils, "isCookieSecure", false);

        testUser = new Users();
        testUser.setUserId(TEST_USER_ID);
        testUser.setUsername(TEST_USERNAME);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setLoginId(TEST_LOGIN_ID);
        testUser.setIsDeleted(TEST_IS_DELETED);
        testUser.setIsActive(TEST_IS_ACTIVE);

        testRole = Role.of(
            1,
            TEST_ROLE_NAME,
            null,
            null,
            null,
            RoleNormalUser.create()
        );
        testRoles = Set.of(testRole);

        mockUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        mockRequest = mock(HttpServletRequest.class);
    }

    @Test
    @DisplayName("Authorizationヘッダーから正常にJWTトークンを抽出できること")
    void getJwtFromHeaderReturnsToken() {
        String expectedToken = "testToken123";
        when(mockRequest.getHeader("Authorization")).thenReturn(BEARER_PREFIX + expectedToken);

        String actualToken = jwtUtils.getJwtFromHeader(mockRequest);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    @DisplayName("Authorizationヘッダーが存在しない場合はnullを返すこと")
    void getJwtFromHeaderReturnsNullWhenHeaderMissing() {
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        String actualToken = jwtUtils.getJwtFromHeader(mockRequest);

        assertNull(actualToken);
    }

    @Test
    @DisplayName("AuthorizationヘッダーがBearer形式でない場合はnullを返すこと")
    void getJwtFromHeaderReturnsNullWhenNotBearerFormat() {
        when(mockRequest.getHeader("Authorization")).thenReturn("InvalidFormat token");

        String actualToken = jwtUtils.getJwtFromHeader(mockRequest);

        assertNull(actualToken);
    }

    @Test
    @DisplayName("Cookieから正常にJWTトークンを抽出できること")
    void getJwtFromCookiesReturnsToken() {
        String expectedToken = "cookieToken123";
        Cookie jwtCookie = new Cookie(TEST_COOKIE_NAME, expectedToken);
        Cookie[] cookies = {jwtCookie};
        when(mockRequest.getCookies()).thenReturn(cookies);

        String actualToken = jwtUtils.getJwtFromCookies(mockRequest);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    @DisplayName("Cookieが存在しない場合はnullを返すこと")
    void getJwtFromCookiesReturnsNullWhenNoCookies() {
        when(mockRequest.getCookies()).thenReturn(null);

        String actualToken = jwtUtils.getJwtFromCookies(mockRequest);

        assertNull(actualToken);
    }

    @Test
    @DisplayName("対象のCookieが存在しない場合はnullを返すこと")
    void getJwtFromCookiesReturnsNullWhenTargetCookieMissing() {
        Cookie otherCookie = new Cookie("other", "value");
        Cookie[] cookies = {otherCookie};
        when(mockRequest.getCookies()).thenReturn(cookies);

        String actualToken = jwtUtils.getJwtFromCookies(mockRequest);

        assertNull(actualToken);
    }

    @Test
    @DisplayName("ユーザー詳細情報から正常にJWTトークンを生成できること")
    void generateTokenCreatesValidToken() {
        String actualToken = jwtUtils.generateToken(mockUserDetails);

        assertNotNull(actualToken);
        assertTrue(actualToken.length() > 0);
    }

    @Test
    @DisplayName("生成されたトークンから正しくメールアドレスを取得できること")
    void getEmailOrLoginIdReturnsCorrectValue() {
        String token = jwtUtils.generateToken(mockUserDetails);

        String actualLoginId = jwtUtils.getEmailOrLoginId(token);

        assertEquals(TEST_LOGIN_ID, actualLoginId);
    }

    @Test
    @DisplayName("生成されたトークンから正しくユーザーIDを取得できること")
    void getUserIdFromTokenReturnsCorrectValue() {
        String token = jwtUtils.generateToken(mockUserDetails);

        String actualUserId = jwtUtils.getUserIdFromToken(token);

        assertEquals(TEST_USER_ID.toString(), actualUserId);
    }

    @Test
    @DisplayName("有効なJWTトークンの検証が成功すること")
    void validateJwtTokenReturnsTrueForValidToken() {
        String validToken = jwtUtils.generateToken(mockUserDetails);

        Boolean isValid = jwtUtils.validateJwtToken(validToken);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("不正な形式のJWTトークンで例外がスローされること")
    void validateJwtTokenThrowsExceptionForMalformedToken() {
        String malformedToken = "invalid.jwt.token";

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> jwtUtils.validateJwtToken(malformedToken)
        );

        assertEquals("Invalid JWT token", exception.getMessage());
        assertEquals(400, exception.getCode().value());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("期限切れのJWTトークンで例外がスローされること")
    void validateJwtTokenThrowsExceptionForExpiredToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenExpirationMs", -1000L);
        String expiredToken = jwtUtils.generateToken(mockUserDetails);
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenExpirationMs", TEST_EXPIRATION_MS);

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> jwtUtils.validateJwtToken(expiredToken)
        );

        assertEquals("JWT token is expired", exception.getMessage());
        assertEquals(400, exception.getCode().value());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("空のJWTトークンで例外がスローされること")
    void validateJwtTokenThrowsExceptionForEmptyToken() {
        String emptyToken = "";

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> jwtUtils.validateJwtToken(emptyToken)
        );

        assertEquals("JWT claims string is empty", exception.getMessage());
        assertEquals(400, exception.getCode().value());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("ユーザー詳細情報から正常にJWT Cookieを生成できること")
    void generateJwtCookieCreatesValidCookie() {
        ResponseCookie actualCookie = jwtUtils.generateJwtCookie(mockUserDetails);

        assertNotNull(actualCookie);
        assertEquals(TEST_COOKIE_NAME, actualCookie.getName());
        assertTrue(actualCookie.getValue().length() > 0);
        assertTrue(actualCookie.isHttpOnly());
        assertEquals(TEST_COOKIE_PATH, actualCookie.getPath());
    }

    @Test
    @DisplayName("クリア用のJWT Cookieが正常に生成されること")
    void getClearJwtCookieCreatesValidClearCookie() {
        ResponseCookie actualCookie = jwtUtils.getClearJwtCookie();

        assertNotNull(actualCookie);
        assertEquals(TEST_COOKIE_NAME, actualCookie.getName());
        assertEquals("", actualCookie.getValue());
        assertEquals(0L, actualCookie.getMaxAge().getSeconds());
        assertEquals(TEST_COOKIE_PATH, actualCookie.getPath());
    }

    @Test
    @DisplayName("署名用の秘密鍵が正常に取得できること")
    void getKeyReturnsValidKey() {
        Key actualKey = jwtUtils.getKey();

        assertNotNull(actualKey);
        assertEquals(TEST_ALGORITHM, actualKey.getAlgorithm());
    }

    @Test
    @DisplayName("秘密鍵の長さが不足している場合に新しい鍵を生成すること")
    void getKeyGeneratesNewKeyWhenSecretTooShort() {
        String shortSecret = "c2hvcnRTZWNyZXQ=";
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenSecret", shortSecret);

        Key actualKey = jwtUtils.getKey();

        assertNotNull(actualKey);
        assertEquals("HmacSHA512", actualKey.getAlgorithm());
    }

    @Test
    @DisplayName("秘密鍵が空文字列の場合に新しい鍵を生成すること")
    void getKeyGeneratesNewKeyWhenSecretEmpty() {
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenSecret", "");

        Key actualKey = jwtUtils.getKey();

        assertNotNull(actualKey);
        assertEquals("HmacSHA512", actualKey.getAlgorithm());
    }

    @Test
    @DisplayName("秘密鍵がちょうど256ビットの場合に正常に鍵を生成すること")
    void getKeyReturnsValidKeyWhenSecretExactly256Bits() {
        String exactLengthSecret = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";
        ReflectionTestUtils.setField(jwtUtils, "jwtTokenSecret", exactLengthSecret);

        Key actualKey = jwtUtils.getKey();

        assertNotNull(actualKey);
        assertEquals("HmacSHA256", actualKey.getAlgorithm());
    }

    @Test
    @DisplayName("loginIdがnullの場合にemailがsubjectとして使用されること")
    void generateTokenUsesEmailWhenLoginIdIsNull() {

        Users userWithNullLoginId = new Users();
        userWithNullLoginId.setUserId(TEST_USER_ID);
        userWithNullLoginId.setUsername(TEST_USERNAME);
        userWithNullLoginId.setEmail(TEST_EMAIL);
        userWithNullLoginId.setPassword(TEST_PASSWORD);
        userWithNullLoginId.setLoginId(null);
        userWithNullLoginId.setIsDeleted(TEST_IS_DELETED);
        userWithNullLoginId.setIsActive(TEST_IS_ACTIVE);

        JWTAuthUserDetails userDetailsWithNullLoginId = JWTAuthUserDetails.build(
            userWithNullLoginId,
            testRoles
        );

        String token = jwtUtils.generateToken(userDetailsWithNullLoginId);
        String actualSubject = jwtUtils.getEmailOrLoginId(token);

        assertEquals(TEST_EMAIL, actualSubject);
    }

    @Test
    @DisplayName("サポートされていないJWTトークンで例外がスローされること")
    void validateJwtTokenThrowsExceptionForUnsupportedToken() {
        String unsupportedToken = Jwts.builder()
                .subject(TEST_LOGIN_ID)
                .compact();

        ApplicationLayerException exception = assertThrows(
            ApplicationLayerException.class,
            () -> jwtUtils.validateJwtToken(unsupportedToken)
        );

        assertEquals("Unsupported JWT token: ", exception.getMessage());
        assertEquals(400, exception.getCode().value());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
