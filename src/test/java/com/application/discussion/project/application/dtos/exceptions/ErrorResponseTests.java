package com.application.discussion.project.application.dtos.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorResponse クラスのテスト")
public class ErrorResponseTests {

    @Test
    @DisplayName("ファクトリメソッドでErrorResponseが正しく作成されることを確認する")
    void createErrorResponseWithFactoryMethodTest() {
        
        String expectedMessage = "エラーが発生しました";
        Integer expectedStatusCode = 400;
        String expectedType = "バリデーションエラー";

        
        ErrorResponse errorResponse = ErrorResponse.of(expectedMessage, expectedStatusCode, expectedType);

        
        assertNotNull(errorResponse);
        assertEquals(expectedMessage, errorResponse.getMessage());
        assertEquals(expectedStatusCode, errorResponse.getStatusCode());
        assertEquals(expectedType, errorResponse.getType());
    }

    @Test
    @DisplayName("Builderを使用してErrorResponseが正しく作成されることを確認する")
    void createErrorResponseWithBuilderTest() {
        
        String expectedMessage = "認証エラーです";
        Integer expectedStatusCode = 401;
        String expectedType = "認証失敗";

        
        ErrorResponse errorResponse = ErrorResponse.of(expectedMessage, expectedStatusCode, expectedType);

        
        assertNotNull(errorResponse);
        assertEquals(expectedMessage, errorResponse.getMessage());
        assertEquals(expectedStatusCode, errorResponse.getStatusCode());
        assertEquals(expectedType, errorResponse.getType());
    }

    @Test
    @DisplayName("getMessageメソッドが正しい値を返すことを確認する")
    void getMessageTest() {
        
        String expectedMessage = "サーバーエラーが発生しました";
        ErrorResponse errorResponse = ErrorResponse.of(expectedMessage, 500, "サーバーエラー");

        
        String actualMessage = errorResponse.getMessage();

        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("getStatusCodeメソッドが正しい値を返すことを確認する")
    void getStatusCodeTest() {
        
        Integer expectedStatusCode = 404;
        ErrorResponse errorResponse = ErrorResponse.of("見つかりません", expectedStatusCode, "リソース不明");

        
        Integer actualStatusCode = errorResponse.getStatusCode();

        
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    @DisplayName("getTypeメソッドが正しい値を返すことを確認する")
    void getTypeTest() {
        
        String expectedType = "データベースエラー";
        ErrorResponse errorResponse = ErrorResponse.of("データベース接続に失敗しました", 500, expectedType);

        
        String actualType = errorResponse.getType();

        
        assertEquals(expectedType, actualType);
    }

    @Test
    @DisplayName("nullパラメータでErrorResponseが作成できることを確認する")
    void createErrorResponseWithNullParametersTest() {
        
        ErrorResponse errorResponse = ErrorResponse.of(null, null, null);

        
        assertNotNull(errorResponse);
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getStatusCode());
        assertNull(errorResponse.getType());
    }

    @Test
    @DisplayName("空文字パラメータでErrorResponseが作成できることを確認する")
    void createErrorResponseWithEmptyParametersTest() {
        
        String emptyMessage = "";
        String emptyType = "";
        Integer statusCode = 0;

        
        ErrorResponse errorResponse = ErrorResponse.of(emptyMessage, statusCode, emptyType);

        
        assertNotNull(errorResponse);
        assertEquals(emptyMessage, errorResponse.getMessage());
        assertEquals(statusCode, errorResponse.getStatusCode());
        assertEquals(emptyType, errorResponse.getType());
    }
}
