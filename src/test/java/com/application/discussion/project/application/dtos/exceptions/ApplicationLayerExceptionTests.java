package com.application.discussion.project.application.dtos.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationLayerException クラスのテスト")
public class ApplicationLayerExceptionTests {

    @Test
    @DisplayName("デフォルトコンストラクタでApplicationLayerExceptionが正しく作成されることを確認する")
    void createApplicationLayerExceptionWithDefaultConstructorTest() {
        
        ApplicationLayerException exception = new ApplicationLayerException();

        
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getStatus());
        assertNull(exception.getCode());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("メッセージのみのコンストラクタでApplicationLayerExceptionが正しく作成されることを確認する")
    void createApplicationLayerExceptionWithMessageOnlyTest() {
        
        String expectedMessage = "アプリケーション層でエラーが発生しました";

        
        ApplicationLayerException exception = new ApplicationLayerException(expectedMessage);

        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getStatus());
        assertNull(exception.getCode());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("全パラメータ指定のコンストラクタでApplicationLayerExceptionが正しく作成されることを確認する")
    void createApplicationLayerExceptionWithAllParametersTest() {
        
        String expectedMessage = "認証に失敗しました";
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        HttpStatusCode expectedCode = HttpStatus.UNAUTHORIZED;

        
        ApplicationLayerException exception = new ApplicationLayerException(expectedMessage, expectedStatus, expectedCode);

        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedCode, exception.getCode());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("getMessageメソッドが正しい値を返すことを確認する")
    void getMessageTest() {
        
        String expectedMessage = "バリデーションエラーです";
        ApplicationLayerException exception = new ApplicationLayerException(expectedMessage);

        
        String actualMessage = exception.getMessage();

        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("getStatusメソッドが正しい値を返すことを確認する")
    void getStatusTest() {
        
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        ApplicationLayerException exception = new ApplicationLayerException(
            "リクエストが不正です", 
            expectedStatus, 
            HttpStatus.BAD_REQUEST
        );

        
        HttpStatus actualStatus = exception.getStatus();

        
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    @DisplayName("getCodeメソッドが正しい値を返すことを確認する")
    void getCodeTest() {
        
        HttpStatusCode expectedCode = HttpStatus.INTERNAL_SERVER_ERROR;
        ApplicationLayerException exception = new ApplicationLayerException(
            "サーバー内部エラーです", 
            HttpStatus.INTERNAL_SERVER_ERROR, 
            expectedCode
        );

        
        HttpStatusCode actualCode = exception.getCode();

        
        assertEquals(expectedCode, actualCode);
    }

    @Test
    @DisplayName("RuntimeExceptionを継承していることを確認する")
    void isRuntimeExceptionTest() {
        
        ApplicationLayerException exception = new ApplicationLayerException();

        
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("nullメッセージでApplicationLayerExceptionが作成できることを確認する")
    void createApplicationLayerExceptionWithNullMessageTest() {
        
        ApplicationLayerException exception = new ApplicationLayerException(null);

        
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("空文字メッセージでApplicationLayerExceptionが作成できることを確認する")
    void createApplicationLayerExceptionWithEmptyMessageTest() {
        
        String emptyMessage = "";

        
        ApplicationLayerException exception = new ApplicationLayerException(emptyMessage);

        
        assertNotNull(exception);
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("nullパラメータを含む全パラメータコンストラクタでApplicationLayerExceptionが作成できることを確認する")
    void createApplicationLayerExceptionWithNullParametersTest() {
        
        ApplicationLayerException exception = new ApplicationLayerException(null, null, null);

        
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getStatus());
        assertNull(exception.getCode());
    }

    @Test
    @DisplayName("異なるHTTPステータスの組み合わせでApplicationLayerExceptionが正しく作成されることを確認する")
    void createApplicationLayerExceptionWithDifferentHttpStatusTest() {
        
        String message = "リソースが見つかりません";
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpStatusCode code = HttpStatus.NOT_FOUND;

        
        ApplicationLayerException exception = new ApplicationLayerException(message, status, code);

        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(status, exception.getStatus());
        assertEquals(code, exception.getCode());
        assertEquals(404, status.value());
        assertEquals(404, code.value());
    }
}
