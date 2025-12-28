package com.application.discussion.project.application.dtos.users;

/**
 * ログアウトレスポンスDTO
 */
public class LogoutResponse {
    
    /**
     * メッセージ
     */
    private String message;
    
    /**
     * 成功フラグ
     */
    private Boolean success;

    /**
     * デフォルトコンストラクタ
     */
    public LogoutResponse() {
    }

    /**
     * 全フィールドを初期化するコンストラクタ
     * 
     * @param message メッセージ
     * @param success 成功フラグ
     */
    private LogoutResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public static LogoutResponse of(String message, Boolean success) {
        return new LogoutResponse(message, success);
    }

    /**
     * メッセージを取得する
     * @return メッセージ
     */
    public String getMessage() {
        return message;
    }

    /**
     * 成功フラグを取得する
     * @return 成功フラグ
     */
    public Boolean getSuccess() {
        return success;
    }
    
    @Override
    public String toString() {
        return "LogoutResponse{message='" + message + "', success=" + success + "}";
    }
}
