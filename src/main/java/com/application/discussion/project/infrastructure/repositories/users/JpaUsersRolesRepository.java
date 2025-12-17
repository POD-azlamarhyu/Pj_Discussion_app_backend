package com.application.discussion.project.infrastructure.repositories.users;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.dtos.UsersRolesProjections;
import com.application.discussion.project.infrastructure.models.users.UsersRoles;

@Repository
public interface JpaUsersRolesRepository extends CrudRepository<UsersRoles, Integer> {

    @Query(
        value = "select r.role_id as roleId,r.role_name as roleName,r.created_at as createdAt, r.updated_at as updatedAt, r.deleted_at as deletedAt from roles as r left join users_roles as ur on r.role_id = ur.role_id where ur.user_id = :userId",
        nativeQuery = true
    )
    List<UsersRolesProjections> findUserRolesByUUID(@Param("userId") UUID userId);

}
