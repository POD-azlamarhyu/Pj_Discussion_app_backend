package com.application.discussion.project.application.dtos.exceptions;

import lombok.Builder;

@Builder
public class ErrorResponse {
    private final String message;
    private final Integer statusCode;
    private final String type;
    
    /**
     * コンストラクタ
     * @param message エラーメッセージ
     * @param statusCode HTTPステータスコード
     * @param type エラータイプ
     */
    private ErrorResponse(final String message,final Integer statusCode,final String type) {
        this.message = message;
        this.statusCode = statusCode;
        this.type = type;
    }
    /**
     * エラーレスポンスオブジェクトを作成するファクトリメソッド
     * @param message エラーメッセージ
     * @param statusCode HTTPステータスコード
     * @param type エラータイプ
     * @return ErrorResponseオブジェクト
     */
    public static ErrorResponse of(
        final String message,
        final Integer statusCode,
        final String type
    ) {
        return new ErrorResponse(message, statusCode, type);
    }

    /**
     * エラーメッセージを取得する
     * @return エラーメッセージ
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * ステータスコードを取得する
     * @return ステータスコード
     */
    public Integer getStatusCode(){
        return this.statusCode;
    }

    /**
     * エラータイプを取得する
     * @return エラータイプ
     */
    public String getType(){
        return this.type;
    }
}
