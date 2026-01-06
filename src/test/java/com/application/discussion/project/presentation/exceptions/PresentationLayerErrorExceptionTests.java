package com.application.discussion.project.presentation.exceptions;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@DisplayName("PresentationLayerErrorException クラスのテスト")
class PresentationLayerErrorExceptionTests {

    private static final String EXPECTED_ERROR_MESSAGE = "プレゼンテーション層でエラーが発生しました";
    private static final HttpStatus EXPECTED_HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final HttpStatusCode EXPECTED_HTTP_STATUS_CODE = HttpStatusCode.valueOf(400);

    @Test
    @DisplayName("デフォルトコンストラクタでインスタンスが生成されること")
    void defaultConstructorCreatesInstance() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException();

        assertThat(actualException).isNotNull();
        assertThat(actualException.getMessage()).isNull();
        assertThat(actualException.getStatus()).isNull();
        assertThat(actualException.getCode()).isNull();
    }

    @Test
    @DisplayName("メッセージのみを指定するコンストラクタでインスタンスが生成されること")
    void messageOnlyConstructorCreatesInstance() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(EXPECTED_ERROR_MESSAGE);

        assertThat(actualException).isNotNull();
        assertThat(actualException.getMessage()).isEqualTo(EXPECTED_ERROR_MESSAGE);
        assertThat(actualException.getStatus()).isNull();
        assertThat(actualException.getCode()).isNull();
    }

    @Test
    @DisplayName("フルパラメータのコンストラクタでインスタンスが生成されること")
    void fullParameterConstructorCreatesInstance() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                EXPECTED_ERROR_MESSAGE,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        assertThat(actualException).isNotNull();
        assertThat(actualException.getMessage()).isEqualTo(EXPECTED_ERROR_MESSAGE);
        assertThat(actualException.getStatus()).isEqualTo(EXPECTED_HTTP_STATUS);
        assertThat(actualException.getCode()).isEqualTo(EXPECTED_HTTP_STATUS_CODE);
    }

    @Test
    @DisplayName("getMessage()が正しいエラーメッセージを返すこと")
    void getMessageReturnsCorrectErrorMessage() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                EXPECTED_ERROR_MESSAGE,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        String actualMessage = actualException.getMessage();

        assertThat(actualMessage).isEqualTo(EXPECTED_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("getStatus()が正しいHTTPステータスを返すこと")
    void getStatusReturnsCorrectHttpStatus() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                EXPECTED_ERROR_MESSAGE,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        HttpStatus actualStatus = actualException.getStatus();

        assertThat(actualStatus).isEqualTo(EXPECTED_HTTP_STATUS);
    }

    @Test
    @DisplayName("getCode()が正しいHTTPステータスコードを返すこと")
    void getCodeReturnsCorrectHttpStatusCode() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                EXPECTED_ERROR_MESSAGE,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        HttpStatusCode actualCode = actualException.getCode();

        assertThat(actualCode).isEqualTo(EXPECTED_HTTP_STATUS_CODE);
    }

    @Test
    @DisplayName("RuntimeExceptionを継承していること")
    void extendsRuntimeException() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                EXPECTED_ERROR_MESSAGE,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        assertThat(actualException).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("異なるHTTPステータスでインスタンスが生成されること")
    void createsInstanceWithDifferentHttpStatus() {
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatusCode expectedCode = HttpStatusCode.valueOf(500);

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                "内部サーバーエラー",
                expectedStatus,
                expectedCode
        );

        assertThat(actualException.getStatus()).isEqualTo(expectedStatus);
        assertThat(actualException.getCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("401エラーでインスタンスが生成されること")
    void createsInstanceWithUnauthorizedStatus() {
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        HttpStatusCode expectedCode = HttpStatusCode.valueOf(401);

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                "認証エラー",
                expectedStatus,
                expectedCode
        );

        assertThat(actualException.getStatus()).isEqualTo(expectedStatus);
        assertThat(actualException.getCode()).isEqualTo(expectedCode);
        assertThat(actualException.getMessage()).isEqualTo("認証エラー");
    }

    @Test
    @DisplayName("403エラーでインスタンスが生成されること")
    void createsInstanceWithForbiddenStatus() {
        HttpStatus expectedStatus = HttpStatus.FORBIDDEN;
        HttpStatusCode expectedCode = HttpStatusCode.valueOf(403);

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                "アクセス権限なし",
                expectedStatus,
                expectedCode
        );

        assertThat(actualException.getStatus()).isEqualTo(expectedStatus);
        assertThat(actualException.getCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("404エラーでインスタンスが生成されること")
    void createsInstanceWithNotFoundStatus() {
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        HttpStatusCode expectedCode = HttpStatusCode.valueOf(404);

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                "リソースが見つかりません",
                expectedStatus,
                expectedCode
        );

        assertThat(actualException.getStatus()).isEqualTo(expectedStatus);
        assertThat(actualException.getCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("409エラーでインスタンスが生成されること")
    void createsInstanceWithConflictStatus() {
        HttpStatus expectedStatus = HttpStatus.CONFLICT;
        HttpStatusCode expectedCode = HttpStatusCode.valueOf(409);

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                "リソース競合エラー",
                expectedStatus,
                expectedCode
        );

        assertThat(actualException.getStatus()).isEqualTo(expectedStatus);
        assertThat(actualException.getCode()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("nullのメッセージでインスタンスが生成されること")
    void createsInstanceWithNullMessage() {
        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                null,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        assertThat(actualException.getMessage()).isNull();
        assertThat(actualException.getStatus()).isEqualTo(EXPECTED_HTTP_STATUS);
        assertThat(actualException.getCode()).isEqualTo(EXPECTED_HTTP_STATUS_CODE);
    }

    @Test
    @DisplayName("空文字のメッセージでインスタンスが生成されること")
    void createsInstanceWithEmptyMessage() {
        String emptyMessage = "";

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                emptyMessage,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        assertThat(actualException.getMessage()).isEqualTo(emptyMessage);
    }

    @Test
    @DisplayName("長いエラーメッセージでインスタンスが生成されること")
    void createsInstanceWithLongErrorMessage() {
        String longMessage = "エラー".repeat(100);

        PresentationLayerErrorException actualException = new PresentationLayerErrorException(
                longMessage,
                EXPECTED_HTTP_STATUS,
                EXPECTED_HTTP_STATUS_CODE
        );

        assertThat(actualException.getMessage()).isEqualTo(longMessage);
    }

    @Test
    @DisplayName("例外がスローできること")
    void canThrowException() {
        try {
            throw new PresentationLayerErrorException(
                    EXPECTED_ERROR_MESSAGE,
                    EXPECTED_HTTP_STATUS,
                    EXPECTED_HTTP_STATUS_CODE
            );
        } catch (PresentationLayerErrorException actualException) {
            assertThat(actualException).isNotNull();
            assertThat(actualException.getMessage()).isEqualTo(EXPECTED_ERROR_MESSAGE);
            assertThat(actualException.getStatus()).isEqualTo(EXPECTED_HTTP_STATUS);
            assertThat(actualException.getCode()).isEqualTo(EXPECTED_HTTP_STATUS_CODE);
        }
    }
}
