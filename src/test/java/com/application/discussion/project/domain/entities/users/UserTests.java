package com.application.discussion.project.domain.entities.users;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.valueobjects.users.Password;

@DisplayName("Userドメインエンティティのテスト")
class UserTests {

    private static final String VALID_USER_NAME = "testUser";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "securePassword123";
    private static final String VALID_LOGIN_ID = "login123";
    private static final String VALID_HASHED_PASSWORD = "$2a$10$hashedpassword";
    private static final UUID VALID_USER_ID = UUID.randomUUID();
    private static final LocalDateTime VALID_CREATED_AT = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
    private static final LocalDateTime VALID_UPDATED_AT = LocalDateTime.of(2024, 1, 2, 12, 0, 0);
    private static final LocalDateTime VALID_DELETED_AT = null;
    private static final Boolean VALID_IS_ACTIVE = true;
    private static final Boolean VALID_IS_DELETED = false;

    @Test
    @DisplayName("createメソッドは有効な入力でUserインスタンスを返す")
    void create_ShouldReturnUser_WhenValidInput() {
        User actualUser = User.create(VALID_USER_NAME, VALID_EMAIL, VALID_PASSWORD);

        assertThat(actualUser).isNotNull();
    }

    @Test
    @DisplayName("createメソッドはnullのユーザー名で例外をスローする")
    void create_ShouldThrowException_WhenUserNameIsNull() {
        assertThatThrownBy(() -> User.create(null, VALID_EMAIL, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("createメソッドは空のユーザー名で例外をスローする")
    void create_ShouldThrowException_WhenUserNameIsEmpty() {
        String emptyUserName = "";

        assertThatThrownBy(() -> User.create(emptyUserName, VALID_EMAIL, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("ユーザー名は必須項目です");
    }

    @Test
    @DisplayName("createメソッドはnullのメールアドレスで例外をスローする")
    void create_ShouldThrowException_WhenEmailIsNull() {
        assertThatThrownBy(() -> User.create(VALID_USER_NAME, null, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("メールアドレスの形式が正しくありません");
    }

    @Test
    @DisplayName("createメソッドは無効なメールアドレスで例外をスローする")
    void create_ShouldThrowException_WhenEmailIsInvalid() {
        String invalidEmail = "invalid-email";

        assertThatThrownBy(() -> User.create(VALID_USER_NAME, invalidEmail, VALID_PASSWORD))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("メールアドレスの形式が正しくありません");
    }

    @Test
    @DisplayName("createメソッドはnullのパスワードで例外をスローする")
    void create_ShouldThrowException_WhenPasswordIsNull() {
        assertThatThrownBy(() -> User.create(VALID_USER_NAME, VALID_EMAIL, null))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("パスワードは必須項目です");
    }

    @Test
    @DisplayName("createメソッドは弱いパスワードで例外をスローする")
    void create_ShouldThrowException_WhenPasswordIsWeak() {
        String weakPassword = "1111111111";

        assertThatThrownBy(() -> User.create(VALID_USER_NAME, VALID_EMAIL, weakPassword))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessage("パスワードは英大文字、英小文字、数字をそれぞれ1文字以上含む必要があります");
    }

    @Test
    @DisplayName("正常系：reBuildメソッドで永続化されたユーザ情報からUserオブジェクトを再構築できること")
    void reBuild_withValidData_returnsUserInstance() {
        User actual = User.reBuild(
            VALID_USER_ID,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_LOGIN_ID,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT,
            VALID_IS_ACTIVE,
            VALID_IS_DELETED
        );

        assertThat(actual.getUserId()).isEqualTo(VALID_USER_ID);
        assertThat(actual.getUserName().value()).isEqualTo(VALID_USER_NAME);
        assertThat(actual.getEmail().value()).isEqualTo(VALID_EMAIL);
        assertThat(actual.getLoginId().value()).isEqualTo(VALID_LOGIN_ID);
        assertThat(actual.getPassword().value()).isEqualTo(VALID_PASSWORD);
        assertThat(actual.getCreatedAt()).isEqualTo(VALID_CREATED_AT);
        assertThat(actual.getUpdatedAt()).isEqualTo(VALID_UPDATED_AT);
        assertThat(actual.getDeletedAt()).isNull();
        assertThat(actual.getIsActive()).isTrue();
        assertThat(actual.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("異常系：reBuildメソッドでuserIdがnullの場合にDomainLayerErrorExceptionがスローされること")
    void reBuild_withNullUserId_throwsDomainLayerErrorException() {
        assertThatThrownBy(() -> User.reBuild(
            null,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_LOGIN_ID,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT,
            VALID_IS_ACTIVE,
            VALID_IS_DELETED
        ))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessageContaining("エラーが発生しました");
    }

    @Test
    @DisplayName("正常系：ofメソッドでUserオブジェクトを生成できること")
    void of_withValidData_returnsUserInstance() {
        User actual = User.of(
            VALID_USER_ID,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT
        );

        assertThat(actual.getUserId()).isEqualTo(VALID_USER_ID);
        assertThat(actual.getUserName().value()).isEqualTo(VALID_USER_NAME);
        assertThat(actual.getEmail().value()).isEqualTo(VALID_EMAIL);
        assertThat(actual.getPassword().value()).isEqualTo(VALID_PASSWORD);
        assertThat(actual.getCreatedAt()).isEqualTo(VALID_CREATED_AT);
        assertThat(actual.getUpdatedAt()).isEqualTo(VALID_UPDATED_AT);
        assertThat(actual.getIsActive()).isTrue();
        assertThat(actual.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("異常系：ofメソッドでuserIdがnullの場合にDomainLayerErrorExceptionがスローされること")
    void of_withNullUserId_throwsDomainLayerErrorException() {
        assertThatThrownBy(() -> User.of(
            null,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT
        ))
            .isInstanceOf(DomainLayerErrorException.class)
            .hasMessageContaining("エラーが発生しました");
    }

    @Test
    @DisplayName("正常系：reBuildWithHashedPasswordメソッドでハッシュ化されたパスワードでUserオブジェクトを再構築できること")
    void reBuildWithHashedPassword_withValidPassword_returnsUserWithHashedPassword() {
        User originalUser = User.reBuild(
            VALID_USER_ID,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_LOGIN_ID,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT,
            VALID_IS_ACTIVE,
            VALID_IS_DELETED
        );
        Password hashedPassword = Password.reBuildHashed(VALID_HASHED_PASSWORD);

        User actual = originalUser.reBuildWithHashedPassword(hashedPassword);

        assertThat(actual.getUserId()).isEqualTo(VALID_USER_ID);
        assertThat(actual.getUserName().value()).isEqualTo(VALID_USER_NAME);
        assertThat(actual.getEmail().value()).isEqualTo(VALID_EMAIL);
        assertThat(actual.getPassword().value()).isEqualTo(VALID_HASHED_PASSWORD);
        assertThat(actual.getCreatedAt()).isEqualTo(VALID_CREATED_AT);
        assertThat(actual.getUpdatedAt()).isEqualTo(VALID_UPDATED_AT);
        assertThat(actual.getIsActive()).isTrue();
        assertThat(actual.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("正常系：reBuildWithHashedPasswordメソッドでloginIdがnullの場合も再構築できること")
    void reBuildWithHashedPassword_withNullLoginId_returnsUserWithHashedPassword() {
        User originalUser = User.reBuild(
            VALID_USER_ID,
            VALID_USER_NAME,
            VALID_EMAIL,
            null,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT,
            VALID_IS_ACTIVE,
            VALID_IS_DELETED
        );
        Password hashedPassword = Password.reBuildHashed(VALID_HASHED_PASSWORD);

        User actual = originalUser.reBuildWithHashedPassword(hashedPassword);

        assertThat(actual.getUserId()).isEqualTo(VALID_USER_ID);
        assertThat(actual.getPassword().value()).isEqualTo(VALID_HASHED_PASSWORD);
        assertThat(actual.getLoginId()).isNull();
    }

    @Test
    @DisplayName("正常系：toStringメソッドでオブジェクトの文字列表現を返せること")
    void toString_returnsStringRepresentation() {
        User user = User.reBuild(
            VALID_USER_ID,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_LOGIN_ID,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT,
            VALID_IS_ACTIVE,
            VALID_IS_DELETED
        );

        String actual = user.toString();

        assertThat(actual).contains("User{");
        assertThat(actual).contains("userId=" + VALID_USER_ID);
        assertThat(actual).contains("userName=");
        assertThat(actual).contains("email=");
        assertThat(actual).contains("password=************");
        assertThat(actual).contains("loginId=");
        assertThat(actual).contains("createdAt=" + VALID_CREATED_AT);
        assertThat(actual).contains("updatedAt=" + VALID_UPDATED_AT);
        assertThat(actual).contains("isActive=" + VALID_IS_ACTIVE);
        assertThat(actual).contains("isDeleted=" + VALID_IS_DELETED);
    }

    @Test
    @DisplayName("正常系：toStringメソッドでパスワードがマスクされていること")
    void toString_passwordIsMasked() {
        User user = User.reBuild(
            VALID_USER_ID,
            VALID_USER_NAME,
            VALID_EMAIL,
            VALID_LOGIN_ID,
            VALID_PASSWORD,
            VALID_CREATED_AT,
            VALID_UPDATED_AT,
            VALID_DELETED_AT,
            VALID_IS_ACTIVE,
            VALID_IS_DELETED
        );

        String actual = user.toString();

        assertThat(actual).contains("password=************");
        assertThat(actual).doesNotContain("password=" + VALID_PASSWORD);
    }
}
