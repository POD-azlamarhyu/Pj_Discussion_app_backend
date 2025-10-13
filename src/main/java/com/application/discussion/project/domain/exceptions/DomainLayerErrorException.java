package com.application.discussion.project.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class DomainLayerErrorException extends RuntimeException {

    private final HttpStatusCode code;
    private final HttpStatus status;

    /**
     * デフォルトコンストラクタ
     * メッセージ、ステータス、コードなしで例外を作成する
     */
    public DomainLayerErrorException(
        String message,
        HttpStatus status,
        HttpStatusCode code
    ) {
        super(message);
        this.status = status;
        this.code = code;
    }
    
    /**
     * HTTPステータスコードを取得する
     * @return
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
