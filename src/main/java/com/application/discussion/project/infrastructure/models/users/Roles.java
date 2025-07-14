package com.application.discussion.project.infrastructure.models.users;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;


@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", updatable = false)
    private Integer roleId;

    @Column(name = "role_name", nullable = false, length = 255)
    private String roleName;

	@Column(name = "created_at", updatable = false)
	@CreationTimestamp
    private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = true)
	@UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",nullable = true)
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "roles",fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UsersRoles> usersRoles;

    public Roles() {
        // Default constructor
    }

    public Roles(Integer roleId, String roleName, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Set<UsersRoles> usersRoles) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.usersRoles = usersRoles;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<UsersRoles> getUsersRoles() {
        return usersRoles;
    }

    public void setUsersRoles(Set<UsersRoles> usersRoles) {
        this.usersRoles = usersRoles;
    }


    public static class RolesBuilder{
        private Set<UsersRoles> usersRoles = new HashSet<>();
    }
}
