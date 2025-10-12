package com.application.discussion.project.domain.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@DisplayName("ドメイン層エラー例外のテスト")
public class DomainLayerErrorExceptionTests {

    private static final String VALID_ERROR_MESSAGE = "これは有効なエラーメッセージです";
    private static final String JAPANESE_ERROR_MESSAGE = "日本語のエラーメッセージテスト";
    private static final String LONG_ERROR_MESSAGE = "これは長いエラーメッセージのテストです。" +
            "複数行にわたる詳細なエラー情報を含む場合のテストケースとして使用します。" +
            "実際のアプリケーションでは、このような詳細なエラーメッセージが必要になることがあります。";
    private static final String EMPTY_MESSAGE = "";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;
    private static final HttpStatusCode DEFAULT_CODE = HttpStatusCode.valueOf(400);

    @Test
    @DisplayName("有効なパラメータで例外を作成できる")
    void createExceptionWithValidParametersTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            DEFAULT_STATUS,
            DEFAULT_CODE
        );

        assertNotNull(exception);
        assertEquals(VALID_ERROR_MESSAGE, exception.getMessage());
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getCode());
    }

    @Test
    @DisplayName("日本語メッセージで例外を作成できる")
    void createExceptionWithJapaneseMessageTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            JAPANESE_ERROR_MESSAGE,
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatusCode.valueOf(500)
        );

        assertEquals(JAPANESE_ERROR_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(HttpStatusCode.valueOf(500), exception.getCode());
    }

    @Test
    @DisplayName("長いメッセージで例外を作成できる")
    void createExceptionWithLongMessageTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            LONG_ERROR_MESSAGE,
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatusCode.valueOf(422)
        );

        assertEquals(LONG_ERROR_MESSAGE, exception.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        assertEquals(HttpStatusCode.valueOf(422), exception.getCode());
    }

    @Test
    @DisplayName("空のメッセージで例外を作成できる")
    void createExceptionWithEmptyMessageTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            EMPTY_MESSAGE,
            DEFAULT_STATUS,
            DEFAULT_CODE
        );

        assertEquals(EMPTY_MESSAGE, exception.getMessage());
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getCode());
    }

    @Test
    @DisplayName("nullメッセージで例外を作成できる")
    void createExceptionWithNullMessageTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            null,
            DEFAULT_STATUS,
            DEFAULT_CODE
        );

        assertNull(exception.getMessage());
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getCode());
    }

    @Test
    @DisplayName("getMessageが正しい値を返す")
    void getMessageReturnsCorrectValueTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            DEFAULT_STATUS,
            DEFAULT_CODE
        );

        assertEquals(VALID_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("getStatusが正しい値を返す")
    void getStatusReturnsCorrectValueTest() {
        HttpStatus testStatus = HttpStatus.NOT_FOUND;
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            testStatus,
            DEFAULT_CODE
        );

        assertEquals(testStatus, exception.getStatus());
    }

    @Test
    @DisplayName("getCodeが正しい値を返す")
    void getCodeReturnsCorrectValueTest() {
        HttpStatusCode testCode = HttpStatusCode.valueOf(404);
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            DEFAULT_STATUS,
            testCode
        );

        assertEquals(testCode, exception.getCode());
    }

    @ParameterizedTest
    @EnumSource(HttpStatus.class)
    @DisplayName("すべてのHttpStatusで例外を作成できる")
    void createExceptionWithAllHttpStatusTest(HttpStatus status) {
        HttpStatusCode code = HttpStatusCode.valueOf(status.value());
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            status,
            code
        );

        assertEquals(VALID_ERROR_MESSAGE, exception.getMessage());
        assertEquals(status, exception.getStatus());
        assertEquals(code, exception.getCode());
    }

    @Test
    @DisplayName("RuntimeExceptionを継承している")
    void extendsRuntimeExceptionTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            DEFAULT_STATUS,
            DEFAULT_CODE
        );

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Throwableとして扱える")
    void canBeTreatedAsThrowableTest() {
        DomainLayerErrorException exception = new DomainLayerErrorException(
            VALID_ERROR_MESSAGE,
            DEFAULT_STATUS,
            DEFAULT_CODE
        );

        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("例外をthrowできる")
    void canBeThrownTest() {
        DomainLayerErrorException exception = assertThrows(
            DomainLayerErrorException.class,
            () -> {
                throw new DomainLayerErrorException(
                    VALID_ERROR_MESSAGE,
                    DEFAULT_STATUS,
                    DEFAULT_CODE
                );
            }
        );

        assertEquals(VALID_ERROR_MESSAGE, exception.getMessage());
        assertEquals(DEFAULT_STATUS, exception.getStatus());
        assertEquals(DEFAULT_CODE, exception.getCode());
    }

    @Test
    @DisplayName("BAD_REQUESTステータスで例外を作成できる")
    void createExceptionWithBadRequestStatusTest() {
        HttpStatus badRequestStatus = HttpStatus.BAD_REQUEST;
        HttpStatusCode badRequestCode = HttpStatusCode.valueOf(400);
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            "不正なリクエストです",
            badRequestStatus,
            badRequestCode
        );

        assertEquals("不正なリクエストです", exception.getMessage());
        assertEquals(badRequestStatus, exception.getStatus());
        assertEquals(badRequestCode, exception.getCode());
    }

    @Test
    @DisplayName("INTERNAL_SERVER_ERRORステータスで例外を作成できる")
    void createExceptionWithInternalServerErrorStatusTest() {
        HttpStatus serverErrorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatusCode serverErrorCode = HttpStatusCode.valueOf(500);
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            "内部サーバーエラーが発生しました",
            serverErrorStatus,
            serverErrorCode
        );

        assertEquals("内部サーバーエラーが発生しました", exception.getMessage());
        assertEquals(serverErrorStatus, exception.getStatus());
        assertEquals(serverErrorCode, exception.getCode());
    }

    @Test
    @DisplayName("NOT_FOUNDステータスで例外を作成できる")
    void createExceptionWithNotFoundStatusTest() {
        HttpStatus notFoundStatus = HttpStatus.NOT_FOUND;
        HttpStatusCode notFoundCode = HttpStatusCode.valueOf(404);
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            "リソースが見つかりません",
            notFoundStatus,
            notFoundCode
        );

        assertEquals("リソースが見つかりません", exception.getMessage());
        assertEquals(notFoundStatus, exception.getStatus());
        assertEquals(notFoundCode, exception.getCode());
    }

    @Test
    @DisplayName("複数の例外インスタンスが独立している")
    void multipleInstancesAreIndependentTest() {
        DomainLayerErrorException exception1 = new DomainLayerErrorException(
            "最初のエラー",
            HttpStatus.BAD_REQUEST,
            HttpStatusCode.valueOf(400)
        );

        DomainLayerErrorException exception2 = new DomainLayerErrorException(
            "2番目のエラー",
            HttpStatus.NOT_FOUND,
            HttpStatusCode.valueOf(404)
        );

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotEquals(exception1.getStatus(), exception2.getStatus());
        assertNotEquals(exception1.getCode(), exception2.getCode());
    }

    @Test
    @DisplayName("ステータスとコードが一致している場合")
    void statusAndCodeMatchTest() {
        HttpStatus status = HttpStatus.FORBIDDEN;
        HttpStatusCode code = HttpStatusCode.valueOf(403);
        
        DomainLayerErrorException exception = new DomainLayerErrorException(
            "アクセスが禁止されています",
            status,
            code
        );

        assertEquals(status.value(), code.value());
        assertEquals(status, exception.getStatus());
        assertEquals(code, exception.getCode());
    }
}
