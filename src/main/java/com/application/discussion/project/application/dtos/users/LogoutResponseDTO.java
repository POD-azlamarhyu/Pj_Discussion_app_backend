package com.application.discussion.project.application.dtos.users;

import org.springframework.http.ResponseCookie;

public class LogoutResponseDTO {
    private LogoutResponse logoutResponse;
    private ResponseCookie jwtCookie;

    public LogoutResponseDTO(){}
    /**
     * コンストラクタ
     * @param logoutResponse
     * @param jwtCookie
     */
    private LogoutResponseDTO(LogoutResponse logoutResponse, ResponseCookie jwtCookie) {
        this.logoutResponse = logoutResponse;
        this.jwtCookie = jwtCookie;
    }

    /**
     * ファクトリーメソッド
     * @param logoutResponse
     * @param jwtCookie
     * @return
     */
    public static LogoutResponseDTO of(LogoutResponse logoutResponse, ResponseCookie jwtCookie) {
        return new LogoutResponseDTO(logoutResponse, jwtCookie);
    }

    /**
     * ゲッター
     * @return logoutResponse
     */
    public LogoutResponse getLogoutResponse() {
        return logoutResponse;
    }

    /**
     * ゲッター
     * @return jwtCookie
     */
    public ResponseCookie getJwtCookie() {
        return jwtCookie;
    }
}
