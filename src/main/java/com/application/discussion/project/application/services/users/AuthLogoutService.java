package com.application.discussion.project.application.services.users;

import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;


/**
 * ログアウト処理を行うアプリケーションサービスのインターフェース
 */
public interface AuthLogoutService {
    
    /**
     * ログアウト処理を実行する
     * 
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @return ログアウト結果を含むレスポンス
     */
    LogoutResponseDTO service();
}
