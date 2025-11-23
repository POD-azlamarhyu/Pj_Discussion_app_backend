package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
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
    }

    public static User create(
        String userName,
        String email,
        String password
    ) {
        return new User(
            UUID.randomUUID(),
            UserName.of(userName),
            Email.of(email),
            Password.of(password),
            LoginId.of(""),
            null,
            null,
            null,
            true,
            false
        );
    }
    
}
