package com.application.discussion.project.infrastructure.repositories.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.models.users.Roles;

@Repository
public interface JpaRolesRepository extends JpaRepository<Roles, Integer> {

    @Query(
        value = "select r.*,ur.* from roles as r left join users_roles as ur on r.role_id = ur.role_id where ur.user_id = :userId",
        nativeQuery = true
    )
    Optional<Object> findUserRolesByUUID(@Param("userId") UUID userId);
    Boolean existsByRoleName(String roleName);
    Roles findByRoleName(String roleName);
}
