package com.application.discussion.project.application.dtos.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthCheckResponse テストクラス")
public class AuthCheckResponseTests {

    private static final String TEST_USERNAME = "testuser";
    private static final Boolean TEST_IS_AUTHENTICATED_TRUE = Boolean.TRUE;
    private static final Boolean TEST_IS_AUTHENTICATED_FALSE = Boolean.FALSE;
    private static final String TEST_MESSAGE = "認証済みです";

    private String testUsername;
    private Boolean testIsAuthenticated;
    private String testMessage;

    @BeforeEach
    void setUp() {
        testUsername = TEST_USERNAME;
        testIsAuthenticated = TEST_IS_AUTHENTICATED_TRUE;
        testMessage = TEST_MESSAGE;
    }

    @Test
    @DisplayName("デフォルトコンストラクタが正常に動作すること")
    public void constructDefaultSuccessfully() {
        AuthCheckResponse actualResponse = new AuthCheckResponse();

        assertNotNull(actualResponse);
        assertNull(actualResponse.getUsername());
        assertNull(actualResponse.getIsAuthenticated());
        assertNull(actualResponse.getMessage());
    }

    @Test
    @DisplayName("ファクトリメソッドでインスタンスが正常に生成されること")
    public void createInstanceUsingFactoryMethod() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, testIsAuthenticated, testMessage);

        assertNotNull(actualResponse);
        assertEquals(testUsername, actualResponse.getUsername());
        assertTrue(actualResponse.getIsAuthenticated());
        assertEquals(testMessage, actualResponse.getMessage());
    }

    @Test
    @DisplayName("getUsernameが正常に値を返すこと")
    public void getUsernameReturnsCorrectValue() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, testIsAuthenticated, testMessage);

        String actualUsername = actualResponse.getUsername();

        assertEquals(testUsername, actualUsername);
    }

    @Test
    @DisplayName("getIsAuthenticatedが正常に値を返すこと")
    public void getIsAuthenticatedReturnsCorrectValue() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, testIsAuthenticated, testMessage);

        Boolean actualIsAuthenticated = actualResponse.getIsAuthenticated();

        assertTrue(actualIsAuthenticated);
    }

    @Test
    @DisplayName("getMessageが正常に値を返すこと")
    public void getMessageReturnsCorrectValue() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, testIsAuthenticated, testMessage);

        String actualMessage = actualResponse.getMessage();

        assertEquals(testMessage, actualMessage);
    }

    @Test
    @DisplayName("toStringが正常にフォーマットされた文字列を返すこと")
    public void toStringReturnsCorrectFormat() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, testIsAuthenticated, testMessage);

        String actualString = actualResponse.toString();

        assertTrue(actualString.contains("AuthCheckResponse"));
        assertTrue(actualString.contains("username='" + testUsername + "'"));
        assertTrue(actualString.contains("isAuthenticated=" + testIsAuthenticated));
        assertTrue(actualString.contains("message='" + testMessage + "'"));
    }

    @Test
    @DisplayName("isAuthenticatedがfalseのインスタンスを正常に生成できること")
    public void createInstanceWithIsAuthenticatedFalse() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, TEST_IS_AUTHENTICATED_FALSE, testMessage);

        assertNotNull(actualResponse);
        assertEquals(testUsername, actualResponse.getUsername());
        assertFalse(actualResponse.getIsAuthenticated());
        assertEquals(testMessage, actualResponse.getMessage());
    }

    @Test
    @DisplayName("nullのusernameでインスタンスが正常に生成されること")
    public void createInstanceWithNullUsername() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(null, testIsAuthenticated, testMessage);

        assertNotNull(actualResponse);
        assertNull(actualResponse.getUsername());
        assertTrue(actualResponse.getIsAuthenticated());
        assertEquals(testMessage, actualResponse.getMessage());
    }

    @Test
    @DisplayName("nullのisAuthenticatedでインスタンスが正常に生成されること")
    public void createInstanceWithNullIsAuthenticated() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, null, testMessage);

        assertNotNull(actualResponse);
        assertEquals(testUsername, actualResponse.getUsername());
        assertNull(actualResponse.getIsAuthenticated());
        assertEquals(testMessage, actualResponse.getMessage());
    }

    @Test
    @DisplayName("nullのmessageでインスタンスが正常に生成されること")
    public void createInstanceWithNullMessage() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(testUsername, testIsAuthenticated, null);

        assertNotNull(actualResponse);
        assertEquals(testUsername, actualResponse.getUsername());
        assertTrue(actualResponse.getIsAuthenticated());
        assertNull(actualResponse.getMessage());
    }

    @Test
    @DisplayName("全引数がnullでインスタンスが正常に生成されること")
    public void createInstanceWithAllNull() {
        AuthCheckResponse actualResponse = AuthCheckResponse.of(null, null, null);

        assertNotNull(actualResponse);
        assertNull(actualResponse.getUsername());
        assertNull(actualResponse.getIsAuthenticated());
        assertNull(actualResponse.getMessage());
    }

    @Test
    @DisplayName("空文字列のusernameでインスタンスが正常に生成されること")
    public void createInstanceWithEmptyUsername() {
        String emptyUsername = "";

        AuthCheckResponse actualResponse = AuthCheckResponse.of(emptyUsername, testIsAuthenticated, testMessage);

        assertNotNull(actualResponse);
        assertEquals("", actualResponse.getUsername());
        assertTrue(actualResponse.getIsAuthenticated());
        assertEquals(testMessage, actualResponse.getMessage());
    }
}
