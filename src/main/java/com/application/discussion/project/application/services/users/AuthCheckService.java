package com.application.discussion.project.application.services.users;

import com.application.discussion.project.application.dtos.users.AuthCheckResponse;

/**
 * 認証状態確認のアプリケーションサービス
 */
public interface AuthCheckService {

    /**
     * 現在の認証状態を確認する
     *
     * @return 認証状態確認レスポンス
     */
    AuthCheckResponse service();
}
