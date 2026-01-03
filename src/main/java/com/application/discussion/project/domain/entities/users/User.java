package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.users.Email;
import com.application.discussion.project.domain.valueobjects.users.LoginId;
import com.application.discussion.project.domain.valueobjects.users.Password;
import com.application.discussion.project.domain.valueobjects.users.UserName;

public class User {
    private final UUID userId;
    private final UserName userName;
    private final Email email;
    private final Password password;
    private final LoginId loginId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
    private final Boolean isActive;
    private final Boolean isDeleted;

    private final static Logger logger = LoggerFactory.getLogger(User.class);
    
    /**
     * Private constructor to enforce the use of the builder or factory method.
     *
     * @param userId    The unique identifier for the user.
     * @param userName  The name of the user.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     * @param loginId   The login ID of the user.
     * @param createdAt The timestamp when the user was created.
     * @param updatedAt The timestamp when the user was last updated.
     * @param deletedAt The timestamp when the user was deleted, if applicable.
     * @param isActive  Indicates if the user is active.
     * @param isDeleted Indicates if the user is deleted.
     */
    private User(
        UUID userId,
        UserName userName,
        Email email,
        Password password,
        LoginId loginId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean isActive,
        Boolean isDeleted
    ){
        logger.info("Creating User with username: {} user's email: {}", userName.toString(), email.toString());
        if (Objects.isNull(userId)) {
            logger.error("Failed to create User: userId is null");
            throw new DomainLayerErrorException("エラーが発生しました",HttpStatus.INTERNAL_SERVER_ERROR,HttpStatusCode.valueOf(500));
        }

        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.loginId = loginId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;

        logger.info("User created successfully with userId: {} username: {}", userId.toString(), userName.toString());
    }

    /**
     * ユーザーを新規作成するファクトリメソッド
     * 
     * @param userName ユーザー名
     * @param email メールアドレス
     * @param password パスワード
     * @return 新規作成されたUserオブジェクト
     */
    public static User create(
        final String userName,
        final String email,
        final String password
    ) {
        return new User(
            UUID.randomUUID(),
            UserName.of(userName),
            Email.of(email),
            Password.of(password),
            null,
            LocalDateTime.now(),
            null,
            null,
            true,
            false
        );
    }

    public static User of(
        final UUID userId,
        final String userName,
        final String email,
        final String password,
        final LocalDateTime createdAt,
        final LocalDateTime updatedAt
    ) {
        return new User(
            userId,
            UserName.of(userName),
            Email.of(email),
            Password.of(password),
            null,
            createdAt,
            updatedAt,
            null,
            true,
            false
        );
    }

    /**
     * 永続化されたユーザー情報からUserオブジェクトを再構築する
     * 
     * @param userId ユーザーID
     * @param userName ユーザー名
     * @param email メールアドレス
     * @param loginId ログインID
     * @param password パスワード
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @param isActive アクティブ状態
     * @param isDeleted 削除状態
     * @return 再構築されたUserオブジェクト
     */
    public static User reBuild(
        final UUID userId,
        final String userName,
        final String email,
        final String loginId,
        final String password,
        final LocalDateTime createdAt,
        final LocalDateTime updatedAt,
        final LocalDateTime deletedAt,
        final Boolean isActive,
        final Boolean isDeleted
    ) {
        logger.info("Rebuilding User with userId: {}, username: {}", userId.toString(), userName);
        return new User(
            userId,
            UserName.reBuild(userName),
            Email.reBuild(email),
            Password.reBuild(password),
            LoginId.reBuild(loginId),
            createdAt,
            updatedAt,
            deletedAt,
            isActive,
            isDeleted
        );
    }

    /**
     * アクティブなユーザーかどうかを判定する
     * 
     * @return アクティブかつ削除されていない場合true
     */
    public boolean isActiveUser() {
        return Boolean.TRUE.equals(isActive) && Boolean.FALSE.equals(isDeleted);
    }

    /**
     * ユーザーが登録可能な状態かを検証する
     * アクティブでないユーザーや削除済みユーザーの再登録を防ぐ
     * 
     * @return 登録可能な場合true
     */
    public boolean canRegister() {
        return isActiveUser();
    }

    /**
     * ハッシュ化されたパスワードでUserオブジェクトを再構築する
     * 
     * @param hashedPassword ハッシュ化されたパスワード
     * @return 再構築されたUserオブジェクト
     */
    public User reBuildWithHashedPassword(final Password hashedPassword) {
        logger.info("Rebuilding User with hashed password for userId: {}", this.userId.toString());
        return User.reBuild(
            this.userId,
            this.userName.value(),
            this.email.value(),
            this.loginId != null ? this.loginId.value() : null,
            hashedPassword.value(),
            this.createdAt,
            this.updatedAt,
            this.deletedAt,
            this.isActive,
            this.isDeleted
        );
    }

    /**
     * ユーザーIDを取得する
     * @return ユーザーID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * ユーザー名を取得する
     * @return ユーザー名
     */
    public UserName getUserName() {
        return userName;
    }

    /**
     * メールアドレスを取得する
     * @return メールアドレス
     */
    public Email getEmail() {
        return email;
    }

    /**
     * パスワードを取得する
     * @return パスワード
     */
    public Password getPassword() {
        return password;
    }

    /**
     * ログインIDを取得する
     * @return ログインID
     */
    public LoginId getLoginId() {
        return loginId;
    }

    /**
     * 作成日時を取得する
     * @return 作成日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 更新日時を取得する
     * @return 更新日時
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 削除日時を取得する
     * @return 削除日時
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * アクティブ状態を取得する
     * @return アクティブ状態
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * 削除状態を取得する
     * @return 削除状態
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * オブジェクトの文字列表現を返す
     * @return オブジェクトの文字列表現
     */
    @Override
    public String toString() {
        String toStringLoginId = Objects.nonNull(this.loginId) ? this.loginId.value() : "null";
        String toStringUpdatedAt = Objects.nonNull(this.updatedAt) ? this.updatedAt.toString() : "null";
        String toStringDeletedAt = Objects.nonNull(this.deletedAt) ? this.deletedAt.toString() : "null";
        
        return "User{" +
            "userId=" + this.userId.toString() +
            ", userName=" + this.userName.value() +
            ", email=" + this.email.value() +
            ", password=************" +
            ", loginId=" + toStringLoginId +
            ", createdAt=" + this.createdAt +
            ", updatedAt=" + toStringUpdatedAt +
            ", deletedAt=" + toStringDeletedAt +
            ", isActive=" + isActive +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
