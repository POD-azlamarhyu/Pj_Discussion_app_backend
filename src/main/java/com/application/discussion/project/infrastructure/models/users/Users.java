package com.application.discussion.project.infrastructure.models.users;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false)
    private UUID userId;
    
    @Column(name = "username", nullable = false, length = 255, columnDefinition="TEXT")
    private String username;
    
    @Column(name= "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255, columnDefinition="TEXT")
    private String password;

    @Column(name = "login_id", nullable = true, unique = true, length = 255, columnDefinition="TEXT")
    private String loginId;
    
	@Column(name = "created_at",updatable = false)
	@CreationTimestamp
    private LocalDateTime createdAt;

	@Column(name = "updated_at",nullable=true)
	@UpdateTimestamp
    private LocalDateTime updatedAt;
    
	@Column(name = "last_login_at",nullable=true)
	@UpdateTimestamp
    private LocalDateTime lastLoginAt;

    @Column(name = "deleted_at",nullable=true)
    private LocalDateTime deletedAt;
    
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;

    @Column(name = "is_deleted",nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;
    
    @OneToMany(mappedBy = "users",fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UsersRoles> usersRoles;

    public Users() {
        this.isActive = true;
        this.isDeleted = false;
        this.usersRoles = new HashSet<>();
    }

    public Users(UUID userId, String username, String email, String password, String loginId,LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLoginAt,Boolean isActive, Boolean isDeleted) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.loginId = loginId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.usersRoles = new HashSet<>();
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public Set<UsersRoles> getUsersRoles() {
        return usersRoles;
    }

    public void setUsersRoles(Set<UsersRoles> usersRoles) {
        this.usersRoles = usersRoles;
    }

    public static UsersBuilder builder() {
        return new UsersBuilder();
    }
    public Users build() {
        Users users = new Users();
        users.setUsersRoles(this.usersRoles);
        return users;
    }

    
    public static class UsersBuilder{
        private Set<UsersRoles> usersRoles = new HashSet<>();
    }
}
