package com.application.discussion.project.application.dtos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ApplicationLayerException extends RuntimeException {
    private HttpStatusCode code;
    private HttpStatus status;

    /**
     * デフォルトコンストラクタ
     * メッセージ、ステータス、コードなしで例外を作成する
     */
    public ApplicationLayerException(){}

    /**
     * メッセージ、HTTPステータス、HTTPステータスコードを指定するコンストラクタ
     * @param message エラーメッセージ
     * @param status HTTPステータス
     * @param code HTTPステータスコード
     */
    public ApplicationLayerException(String message, HttpStatus status,HttpStatusCode code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    /**
     * メッセージのみを指定するコンストラクタ
     * @param message エラーメッセージ
     */
    public ApplicationLayerException(String message) {
        super(message);
    }
    /**
     * エラーメッセージを取得する
     * @return エラーメッセージ
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * HTTPステータスコードを取得する
     * @return HTTPステータスコード
     */
    public HttpStatusCode getCode() {
        return this.code;
    }
    
    /**
     * HTTPステータスを取得する
     * @return HTTPステータス
     */
    public HttpStatus getStatus() {
        return this.status;
    }
}
