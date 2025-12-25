package com.application.discussion.project.infrastructure.repositories.users;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.dtos.UsersRolesProjections;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.models.users.UsersRoles;

@Repository
public interface JpaUsersRolesRepository extends JpaRepository<UsersRoles, Integer> {

    /**
    * 指定されたユーザーIDに関連付けられたロールを取得する
    * 
    * @param userId ユーザーID
    * @return ユーザーに関連付けられたロールのリスト
    */
    @Query(
        value = "select r.role_id as roleId,r.role_name as roleName,r.created_at as createdAt, r.updated_at as updatedAt, r.deleted_at as deletedAt from roles as r left join users_roles as ur on r.role_id = ur.role_id where ur.user_id = :userId",
        nativeQuery = true
    )
    List<UsersRolesProjections> findUserRolesByUUID(@Param("userId") UUID userId);

    /**
     * 指定されたユーザーとロールの組み合わせが存在するかチェックする
     * 
     * @param user 対象ユーザー
     * @param role 対象ロール
     * @return 存在する場合true
     */
    boolean existsByUsersAndRoles(Users user, Roles role);
}
