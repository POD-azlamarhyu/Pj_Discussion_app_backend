package com.application.discussion.project.application.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログアウトレスポンスDTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponse {
    
    /**
     * メッセージ
     */
    private String message;
    
    /**
     * 成功フラグ
     */
    private Boolean success;
}
