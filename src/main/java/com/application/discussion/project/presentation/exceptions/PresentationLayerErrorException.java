package com.application.discussion.project.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class PresentationLayerErrorException extends RuntimeException {
    private HttpStatusCode code;
    private HttpStatus status;
    /**
     * デフォルトコンストラクタ
     * メッセージ、ステータス、コードなしで例外を作成する
     */
    public PresentationLayerErrorException(){}

    /**
     * メッセージ、HTTPステータス、HTTPステータスコードを指定するコンストラクタ
     * @param message エラーメッセージ
     * @param status HTTPステータス
     * @param code HTTPステータスコード
     */
    public PresentationLayerErrorException(String message, HttpStatus status,HttpStatusCode code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    /**
     * メッセージのみを指定するコンストラクタ
     * @param message エラーメッセージ
     */
    public PresentationLayerErrorException(String message) {
        super(message);
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
