package com.application.discussion.project.domain.services.users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.discussion.project.domain.entities.users.Role;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.valueobjects.users.RoleType;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;

@Service
public class RoleRegistrationDomainServiceImpl implements RoleRegistrationDomainService {

    @Autowired
    private RolesRepositoryInterface rolesRepositoryInterface;

    private static final Logger logger = LoggerFactory.getLogger(RoleRegistrationDomainServiceImpl.class);

    /**
     * ロールの重複チェックを行う
     * 既に同じロールが存在する場合は例外をスローする
     * 
     * @param roleType チェック対象のロール
     */
    @Override
    public Boolean ensureRoleIsUnique(RoleType roleType) {
        logger.info("Starting role uniqueness check: role={}", roleType.getRoleValue());
        Boolean exists = rolesRepositoryInterface.existsByRoleName(roleType);
        logger.info("Role uniqueness check completed: exists={}", exists);
        return exists;
    }

    /**
     * ユーザのロールが有効かどうかを検証して、未登録のみのリストを返す
     * 
     * @param roleTypes 検証対象のロールリスト
     */
    @Override
    public Boolean ensureUserRolesAreValid(User user, Role role) {
        logger.info("Starting user roles validation");
        Boolean exists = rolesRepositoryInterface.existsByUserAndRoleBoolean(user, role);
        logger.info("User roles validation passed");
        return exists;
    }

}
