package com.application.discussion.project.infrastructure.repositories.users;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.valueobjects.users.RoleType;
import com.application.discussion.project.infrastructure.dtos.UsersRolesProjections;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.models.users.UsersRoles;

@Repository
public class RolesRepositoryImpl implements RolesRepositoryInterface {
    
    @Autowired
    private JpaUsersRolesRepository jpaUsersRolesRepository;

    @Autowired
    private JpaRolesRepository jpaRolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(RolesRepositoryImpl.class);

    @Override
    public Set<Role> findUserRolesById(UUID userId) {
        logger.info("Fetching roles for user ID: {}", userId);
        List<UsersRolesProjections> roles = jpaUsersRolesRepository.findUserRolesByUUID(userId);

        logger.info("Roles fetched: {}", roles);
        
        return roles.stream()
                .map(role -> {
                    return Role.of(
                        role.getRoleId(),
                        role.getRoleName(),
                        role.getCreatedAt(),
                        role.getUpdatedAt(),
                        role.getDeletedAt(),
                        null
                    );
                })
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * ロール名でロールの存在を確認する
     * @return 存在する場合はtrue、存在しない場合はfalse
     */
    @Override
    public Boolean existsByRoleName(RoleType roleType) {
        logger.info("Checking existence of role: {}", roleType.getRoleValue());
        Boolean exists = jpaRolesRepository.existsByRoleName(roleType.getRoleValue());
        logger.info("Existence check result for role {}: {}", roleType.getRoleValue(), exists);
        return exists;
    }

    /**
     * ユーザーとロールの組み合わせの存在を確認する
     * @return 存在する場合はtrue、存在しない場合はfalse
     */
    @Override
    public Boolean existsByUserAndRoleBoolean(User user, Role role) {
        logger.info("Checking existence of user_role user:{} roles: {}",user.toString(), role.toString());
        Users userModel = new Users();
        userModel.setUserId(user.getUserId());
        userModel.setLoginId(user.getLoginId().value());
        userModel.setEmail(user.getEmail().value());

        Roles roleModel = new Roles();
        roleModel.setRoleId(role.getRoleId());
        roleModel.setRoleName(role.getRoleNameValue());
        
        Boolean exists = jpaUsersRolesRepository.existsByUsersAndRoles(userModel, roleModel);
        logger.info("Existence check result for roles {}", exists);
        return exists;
    }

    /**
     * ロールを保存する
     * @param role 保存するロールエンティティ
     * @return 保存されたロールエンティティ
     */
    @Override
    public Role saveRole(final Role role) {
        logger.info("Saving role: {}", role.getRoleName());
        Roles RoleModel = new Roles();
        RoleModel.setRoleName(role.getRoleNameValue());

        Roles savedRoleEntity = jpaRolesRepository.save(RoleModel);

        logger.info("Role saved successfully with ID: {}", RoleModel.getRoleId());
        return Role.of(
            savedRoleEntity.getRoleId(),
            savedRoleEntity.getRoleName(),
            savedRoleEntity.getCreatedAt(),
            savedRoleEntity.getUpdatedAt(),
            savedRoleEntity.getDeletedAt(),
            role.getRoleType()
        );
    }

    /**
     * ロール名でロールを取得する
     * @param roleType 取得するロール名
     * @return 取得されたロールエンティティ
     */
    @Override
    public Role findByRoleName(final RoleType roleType) {
        logger.info("Fetching role by name: {}", roleType.getRoleValue());
        Roles roleEntity = jpaRolesRepository.findByRoleName(roleType.getRoleValue());
        logger.info("Role fetched: {}", roleEntity.getRoleName());
        return Role.of(
            roleEntity.getRoleId(),
            roleEntity.getRoleName(),
            roleEntity.getCreatedAt(),
            roleEntity.getUpdatedAt(),
            roleEntity.getDeletedAt(),
            roleType
        );
    }

    /**
     * ユーザーとロールのマッピングを保存する
     * @param user ユーザーエンティティ
     * @param role ロールエンティティ
     */
    @Override
    public void saveUserRoleMapping(final User user, final Role role) {
        logger.info("Saving user-role mapping: userId={}, roleId={}", user.getUserId(), role.getRoleId());
        UsersRoles userRoleMapping = new UsersRoles();
        Users userModel = new Users();
        userModel.setUserId(user.getUserId());
        userRoleMapping.setUsers(userModel);
        Roles roleModel = new Roles();
        roleModel.setRoleId(role.getRoleId());
        userRoleMapping.setUsers(userModel);
        userRoleMapping.setRoles(roleModel);
        jpaUsersRolesRepository.save(userRoleMapping);
        logger.info("User-role mapping saved successfully for userId={}, roleId={}", user.getUserId(), role.getRoleId());
    }
}
