package com.application.discussion.project.application.services.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.valueobjects.users.RoleAdmin;
import com.application.discussion.project.domain.valueobjects.users.RoleNormalUser;
import com.application.discussion.project.domain.valueobjects.users.RoleType;
import com.application.discussion.project.infrastructure.models.users.Users;

@DisplayName("JWTAuthUserDetails単体テスト")
public class JWTAuthUserDetailsTests {

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "Test User";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "encoded4Password";
    private static final String TEST_LOGIN_ID = "testuser";
    private static final String TEST_ROLE_NAME = "ROLE_USER";
    
    private RoleType testRoleType;
    private Users testUser;
    private Set<Role> testRoles;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUserId(TEST_USER_ID);
        testUser.setUsername(TEST_USERNAME);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setLoginId(TEST_LOGIN_ID);
        testUser.setIsDeleted(false);
        testUser.setIsActive(true);

        testRoleType = RoleNormalUser.create();

        testRole = Role.of(
            1,
            TEST_ROLE_NAME,
            null,
            null,
            null,
            testRoleType
        );
        testRoles = Set.of(testRole);
    }

    @Test
    @DisplayName("buildメソッドでUserDetailsオブジェクトが正常に作成されること")
    void build_WithValidUserAndRoles_ReturnsUserDetails() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails).isNotNull();
        assertThat(actualUserDetails.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(actualUserDetails.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(actualUserDetails.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(actualUserDetails.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(actualUserDetails.getLoginId()).isEqualTo(TEST_LOGIN_ID);
        assertThat(actualUserDetails.getIsDeleted()).isFalse();
        assertThat(actualUserDetails.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("buildメソッドで権限情報が正しく設定されること")
    void build_WithRoles_SetsAuthoritiesCorrectly() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);
        Collection<? extends GrantedAuthority> predictAuthorities = List.of(
            new SimpleGrantedAuthority(TEST_ROLE_NAME)
        );

        Collection<? extends GrantedAuthority> actualAuthorities = actualUserDetails.getAuthorities();
        assertThat(actualAuthorities).hasSize(1);
        assertThat(actualAuthorities).isEqualTo(predictAuthorities);
        assertEquals(predictAuthorities.iterator().next(), actualAuthorities.iterator().next());
    }

    @Test
    @DisplayName("buildメソッドで複数のロールが正しく変換されること")
    void build_WithMultipleRoles_ConvertsAllRolesToAuthorities() {
        Role adminRole = Role.of(
                2, 
                "ROLE_ADMIN", 
                null,
                null, 
                null,
                RoleAdmin.create()
        );
        Set<Role> multipleRoles = Set.of(testRole, adminRole);

        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, multipleRoles);

        List<SimpleGrantedAuthority> predictAuthorities = List.of(
            new SimpleGrantedAuthority(TEST_ROLE_NAME),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        Collection<? extends GrantedAuthority> actualAuthorities = actualUserDetails.getAuthorities();
        assertThat(actualAuthorities).hasSize(2);
        assertThat(actualAuthorities).hasSize(predictAuthorities.size());
        assertThat(actualAuthorities.toArray())
            .hasSize(2)
            .containsExactlyInAnyOrder(
                predictAuthorities.toArray()
            );
    }

    @Test
    @DisplayName("buildメソッドで空のロールセットでも正常に動作すること")
    void build_WithEmptyRoles_ReturnsUserDetailsWithEmptyAuthorities() {
        Set<Role> emptyRoles = Set.of();

        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, emptyRoles);

        assertThat(actualUserDetails.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("isEnabledメソッドでアクティブかつ削除されていない場合にtrueを返すこと")
    void isEnabled_WhenActiveAndNotDeleted_ReturnsTrue() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("isEnabledメソッドで削除済みの場合にfalseを返すこと")
    void isEnabled_WhenDeleted_ReturnsFalse() {
        testUser.setIsDeleted(true);

        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("isEnabledメソッドで非アクティブの場合にfalseを返すこと")
    void isEnabled_WhenNotActive_ReturnsFalse() {
        testUser.setIsActive(false);

        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("isAccountNonLockedメソッドで削除されていない場合にtrueを返すこと")
    void isAccountNonLocked_WhenNotDeleted_ReturnsTrue() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    @DisplayName("isAccountNonLockedメソッドで削除済みの場合にfalseを返すこと")
    void isAccountNonLocked_WhenDeleted_ReturnsFalse() {
        testUser.setIsDeleted(true);

        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isAccountNonLocked()).isFalse();
    }

    @Test
    @DisplayName("isAccountNonExpiredメソッドで常にtrueを返すこと")
    void isAccountNonExpired_AlwaysReturnsTrue() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    @DisplayName("isCredentialsNonExpiredメソッドで常にtrueを返すこと")
    void isCredentialsNonExpired_AlwaysReturnsTrue() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("equalsメソッドで同じUserIdの場合にtrueを返すこと")
    void equals_WithSameUserId_ReturnsTrue() {
        JWTAuthUserDetails userDetails1 = JWTAuthUserDetails.build(testUser, testRoles);
        JWTAuthUserDetails userDetails2 = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(userDetails1).isEqualTo(userDetails2);
    }

    @Test
    @DisplayName("equalsメソッドで異なるUserIdの場合にfalseを返すこと")
    void equals_WithDifferentUserId_ReturnsFalse() {
        Users anotherUser = new Users();
        anotherUser.setUserId(UUID.randomUUID());
        anotherUser.setUsername("Another User");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password");
        anotherUser.setLoginId("anotheruser");
        anotherUser.setIsDeleted(false);
        anotherUser.setIsActive(true);

        JWTAuthUserDetails userDetails1 = JWTAuthUserDetails.build(testUser, testRoles);
        JWTAuthUserDetails userDetails2 = JWTAuthUserDetails.build(anotherUser, testRoles);

        assertThat(userDetails1).isNotEqualTo(userDetails2);
    }

    @Test
    @DisplayName("equalsメソッドで同じインスタンスの場合にtrueを返すこと")
    void equals_WithSameInstance_ReturnsTrue() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails).isEqualTo(actualUserDetails);
    }

    @Test
    @DisplayName("equalsメソッドでnullの場合にfalseを返すこと")
    void equals_WithNull_ReturnsFalse() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(actualUserDetails).isNotEqualTo(null);
    }

    @Test
    @DisplayName("equalsメソッドで異なるクラスの場合にfalseを返すこと")
    void equals_WithDifferentClass_ReturnsFalse() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);
        String differentObject = "different";

        assertThat(actualUserDetails).isNotEqualTo(differentObject);
    }

    @Test
    @DisplayName("hashCodeメソッドで同じUserIdの場合に同じハッシュコードを返すこと")
    void hashCode_WithSameUserId_ReturnsSameHashCode() {
        JWTAuthUserDetails userDetails1 = JWTAuthUserDetails.build(testUser, testRoles);
        JWTAuthUserDetails userDetails2 = JWTAuthUserDetails.build(testUser, testRoles);

        assertThat(userDetails1.hashCode()).isEqualTo(userDetails2.hashCode());
    }

    @Test
    @DisplayName("getAuthoritiesメソッドで権限情報を正しく取得できること")
    void getAuthorities_ReturnsCorrectAuthorities() {
        JWTAuthUserDetails actualUserDetails = JWTAuthUserDetails.build(testUser, testRoles);

        Collection<? extends GrantedAuthority> actualAuthorities = actualUserDetails.getAuthorities();
        assertThat(actualAuthorities).isNotEmpty();
        assertThat(actualAuthorities).allMatch(auth -> auth instanceof SimpleGrantedAuthority);
    }
}
