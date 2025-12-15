package com.application.discussion.project.application.services.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class JWTAuthUserDetails implements UserDetails {
    
    private UUID userId;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private String loginId;

    private Boolean isDeleted;

    private Boolean isActive;

    private Collection<? extends GrantedAuthority> authorities;

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthUserDetails.class);

    /**
     * JWTAuthUserDetailsのコンストラクタ
     *
     * @param userId        ユーザID 
     * @param username      ユーザー名 
     * @param email         Emailアドレス 
     * @param password      パスワード 
     * @param loginId       ログインID 
     * @param isDeleted     削除フラグ 
     * @param isActive      アクティブフラグ 
     * @param authorities   ロール情報のコレクション 
     */
    public JWTAuthUserDetails(
        UUID userId,
        String username,
        String email,
        String password,
        String loginId,
        Boolean isDeleted,
        Boolean isActive,
        Collection<? extends GrantedAuthority> authorities
    ){
        logger.info("Initializing JWTAuthUserDetails for user: {}", username);
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.loginId = loginId;
        this.isDeleted = isDeleted;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    /**
     * ユーザー情報とその役割からJWTAuthUserDetailsオブジェクトを構築する
     * 
     * @param user ユーザー情報を含むUsersオブジェクト
     * @param roles ユーザーの役割を含むRolesのセット
     * @return 構築されたJWTAuthUserDetailsオブジェクト
     */
    public static JWTAuthUserDetails build(Users user, Set<Role> roles){
        logger.info("Building JWTAuthUserDetails for user: {}", user.getUsername());
        List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());

        logger.info("Authorities assigned: {}", authorities);
        return new JWTAuthUserDetails(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getLoginId(),
            user.getIsDeleted(),
            user.getIsActive(),
            authorities
        );
    }

    /**
     * ユーザーの権限情報を取得する
     * @return ユーザーの権限を含むコレクション
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    /**
     * ユーザーIDを取得する
     * 
     * @return ユーザーのUUID
     */
    public UUID getUserId(){
        return userId;
    }

    /**
     * ユーザー名を取得する
     * @return ユーザー名
     */
    @Override
    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    /**
     * パスワードを取得する
     * @return パスワード
     */
    @Override
    public String getPassword(){
        return password;
    }

    /**
     * ログインIDを取得する
     * @return ログインID
     */
    public String getLoginId(){
        return loginId;
    }

    /**
     * 削除フラグを取得する
     * @return 削除フラグ
     */
    public Boolean getIsDeleted(){
        return isDeleted;
    }

    /**
     * アクティブフラグを取得する
     * @return アクティブフラグ
     */
    public Boolean getIsActive(){
        return isActive;
    }

    /**
     * アカウントの有効期限が切れていないかを示す
     * @return true - アカウントは有効期限内, false - アカウントは期限切れ
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * アカウントがロックされていないかを示す
     * @return true - アカウントはロックされていない, false - アカウントはロックされている
     */
    @Override
    public boolean isAccountNonLocked() {
        return !this.isDeleted;
    }

    /**
     * 資格情報の有効期限が切れていないかを示す
     * @return true - 資格情報は有効期限内, false - 資格情報は期限切れ
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * アカウントが有効かどうかを示す
     * @return true - アカウントは有効, false - アカウントは無効
     */
    @Override
    public boolean isEnabled() {
        return this.isActive && !this.isDeleted;
    }

    /**
     * ハッシュコードを生成する
     * 
     * @return オブジェクトのハッシュコード
     */
    @Override
    public int hashCode(){
        return Objects.hash(userId);
    }

    /**
     * オブジェクトの等価性を比較する
     * 
     * @param object 比較対象のオブジェクト
     * @return true - 等価, false - 非等価
     */
    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (!(object instanceof JWTAuthUserDetails)) return false;
        if (getClass() != object.getClass()) return false;

        JWTAuthUserDetails user = (JWTAuthUserDetails) object;
        return Objects.equals(userId, user.userId);
    }
}
