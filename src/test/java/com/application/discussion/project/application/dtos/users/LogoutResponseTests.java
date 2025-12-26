package com.application.discussion.project.application.dtos.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LogoutResponse テストクラス")
public class LogoutResponseTests {

    private static final String TEST_MESSAGE = "Logout successful";
    private static final Boolean TEST_SUCCESS_TRUE = Boolean.TRUE;
    private static final Boolean TEST_SUCCESS_FALSE = Boolean.FALSE;

    private String testMessage;
    private Boolean testSuccess;

    @BeforeEach
    void setUp() {
        testMessage = TEST_MESSAGE;
        testSuccess = TEST_SUCCESS_TRUE;
    }

    @Test
    @DisplayName("デフォルトコンストラクタが正常に動作すること")
    void constructDefaultSuccessfully() {
        LogoutResponse actualResponse = new LogoutResponse();

        assertNotNull(actualResponse);
        assertNull(actualResponse.getMessage());
        assertNull(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("ファクトリーメソッドでインスタンスが正常に生成されること")
    void createInstanceUsingFactoryMethod() {
        LogoutResponse actualResponse = LogoutResponse.of(testMessage, testSuccess);

        assertNotNull(actualResponse);
        assertEquals(testMessage, actualResponse.getMessage());
        assertTrue(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("getMessageが正常に値を返すこと")
    void getMessageReturnsCorrectValue() {
        LogoutResponse actualResponse = LogoutResponse.of(testMessage, testSuccess);

        String actualMessage = actualResponse.getMessage();

        assertEquals(testMessage, actualMessage);
    }

    @Test
    @DisplayName("getSuccessが正常に値を返すこと")
    void getSuccessReturnsCorrectValue() {
        LogoutResponse actualResponse = LogoutResponse.of(testMessage, testSuccess);

        Boolean actualSuccess = actualResponse.getSuccess();

        assertTrue(actualSuccess);
    }

    @Test
    @DisplayName("toStringが正常に文字列を返すこと")
    void toStringReturnsCorrectFormat() {
        LogoutResponse actualResponse = LogoutResponse.of(testMessage, testSuccess);

        String actualString = actualResponse.toString();

        assertTrue(actualString.contains("LogoutResponse"));
        assertTrue(actualString.contains("message='" + testMessage + "'"));
        assertTrue(actualString.contains("success=" + testSuccess));
    }

    @Test
    @DisplayName("successがfalseの場合にインスタンスが正常に生成されること")
    void createInstanceWithSuccessFalse() {
        LogoutResponse actualResponse = LogoutResponse.of(testMessage, TEST_SUCCESS_FALSE);

        assertNotNull(actualResponse);
        assertEquals(testMessage, actualResponse.getMessage());
        assertFalse(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("ファクトリーメソッドがnullのmessageを受け入れること")
    void createInstanceWithNullMessage() {
        LogoutResponse actualResponse = LogoutResponse.of(null, testSuccess);

        assertNotNull(actualResponse);
        assertNull(actualResponse.getMessage());
        assertTrue(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("ファクトリーメソッドがnullのsuccessを受け入れること")
    void createInstanceWithNullSuccess() {
        LogoutResponse actualResponse = LogoutResponse.of(testMessage, null);

        assertNotNull(actualResponse);
        assertEquals(testMessage, actualResponse.getMessage());
        assertNull(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("ファクトリーメソッドが両方nullの引数を受け入れること")
    void createInstanceWithBothNull() {
        LogoutResponse actualResponse = LogoutResponse.of(null, null);

        assertNotNull(actualResponse);
        assertNull(actualResponse.getMessage());
        assertNull(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("空文字列のmessageでインスタンスが正常に生成されること")
    void createInstanceWithEmptyMessage() {
        String emptyMessage = "";

        LogoutResponse actualResponse = LogoutResponse.of(emptyMessage, testSuccess);

        assertNotNull(actualResponse);
        assertEquals("", actualResponse.getMessage());
        assertTrue(actualResponse.getSuccess());
    }

    @Test
    @DisplayName("長いmessageでtoStringが正常に動作すること")
    void toStringWithLongMessage() {
        String longMessage = "A".repeat(1000);

        LogoutResponse actualResponse = LogoutResponse.of(longMessage, testSuccess);
        String actualString = actualResponse.toString();

        assertTrue(actualString.contains(longMessage));
    }
}
