package com.application.discussion.project.presentation.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.discussion.project.application.dtos.users.LoginRequest;
import com.application.discussion.project.application.dtos.users.LoginResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponse;
import com.application.discussion.project.application.dtos.users.LogoutResponseDTO;
import com.application.discussion.project.application.services.users.AuthLoginServiceInterface;
import com.application.discussion.project.application.services.users.AuthLogoutService;
import com.application.discussion.project.presentation.validations.AuthLoginRequestValidation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Tag(name="authentication",description = "Authentication API")
@RestController
@RequestMapping(value = "/v1/auth")
public class AuthController {
    
    @Autowired
    private AuthLoginServiceInterface AuthLoginServiceInterface;

    @Autowired
    private AuthLogoutService authLogoutService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(
        summary = "ユーザーログイン",
        description = "メールアドレスまたはログインIDとパスワードを使用してアプリケーションにログインします。" +
        "認証に成功するとアクセストークンとリフレッシュトークンが返却されます。",
        tags = {"authentication"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "ログイン認証情報（メールアドレスまたはログインIDとパスワード）",
        required = true,
        content = @Content(
            schema = @Schema(implementation = LoginRequest.class),
            mediaType = "application/json"
        )
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "ログイン成功 - アクセストークンとリフレッシュトークンを返却",
                content = @Content(
                    schema = @Schema(implementation = LoginResponse.class),
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "リクエストが不正 - バリデーションエラー（メールアドレス/ログインIDまたはパスワードが未入力）",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "401",
                description = "認証失敗 - メールアドレス/ログインIDまたはパスワードが間違っています",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "404",
                description = "ユーザーが見つかりません - 指定されたメールアドレス/ログインIDのユーザーが存在しません",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "403",
                description = "アクセス禁止 - アカウントがロックされているか無効化されています",
                content = @Content
            )
        }
    )
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for user: {}", loginRequest.getEmailOrLoginId());

        AuthLoginRequestValidation.validate(loginRequest);
        return AuthLoginServiceInterface.service(
            loginRequest
        );
    }
    
    @Operation(
        summary = "ユーザーログアウト",
        description = "現在ログイン中のユーザーをログアウトさせます。" +
        "セキュリティコンテキストをクリアし、CookieからJWTトークンを削除します。",
        tags = {"authentication"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "ログアウト成功 - セッションとトークンが正常に削除されました",
                content = @Content(
                    schema = @Schema(implementation = LogoutResponse.class),
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "未認証 - ログインしていないか、トークンが無効です",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "403",
                description = "アクセス禁止 - ログアウト権限がありません",
                content = @Content
            )
        }
    )
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("ログアウトリクエストを受信しました");
        
        LogoutResponseDTO logoutResponseDTO = authLogoutService.service(request, response);
        
        return ResponseEntity.status(HttpStatus.OK)
            .header("Set-Cookie", logoutResponseDTO.getJwtCookie().toString())
            .body(logoutResponseDTO.getLogoutResponse());
    }
    
    @Operation(summary = "refresh access token jwt",tags = {"authentication"})
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "access token refresh successful",
                content = @Content(schema = @Schema(implementation = ResponseEntity.class))
            ),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)
        }
    )
    @PostMapping("/refresh")
    public void refresh(){

    }
}
