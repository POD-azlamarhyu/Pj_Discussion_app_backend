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
     * Constructor for JWTAuthUserDetails.
     *
     * @param userId      the unique identifier of the user
     * @param username    the username of the user
     * @param email       the email of the user
     * @param password    the password of the user
     * @param loginId     the login ID of the user
     * @param isDeleted   indicates if the user is deleted
     * @param isActive    indicates if the user is active
     * @param authorities the authorities granted to the user
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
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.loginId = loginId;
        this.isDeleted = isDeleted;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    public static JWTAuthUserDetails build(Users user, Set<Roles> roles){
        List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    public UUID getUserId(){
        return userId;
    }

    @Override
    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }
    public String getLoginId(){
        return loginId;
    }
    public Boolean getIsDeleted(){
        return isDeleted;
    }
    public Boolean getIsActive(){
        return isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (!(object instanceof JWTAuthUserDetails)) return false;
        if (object == null || getClass() != object.getClass()) return false;

        JWTAuthUserDetails user = (JWTAuthUserDetails) object;
        return Objects.equals(userId, user.userId);
    }
}
