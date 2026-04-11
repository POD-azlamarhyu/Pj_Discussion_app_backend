package com.application.discussion.project.application.services.users;

import com.application.discussion.project.application.dtos.users.AuthRefreshTokenServiceResult;

import jakarta.servlet.http.HttpServletRequest;

/**
 * リフレッシュトークンによるアクセストークン再発行サービスのインターフェース
 */
public interface AuthRefreshTokenService {

    /**
     * リフレッシュトークンを検証してアクセストークンを再発行する
     *
     * @param request HTTPリクエスト（Cookieからリフレッシュトークンを取得）
     * @return リフレッシュ結果のDTO（新アクセストークン、新リフレッシュトークンCookie）
     */
    AuthRefreshTokenServiceResult service(HttpServletRequest request);
}
