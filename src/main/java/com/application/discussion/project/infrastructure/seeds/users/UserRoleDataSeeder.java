package com.application.discussion.project.infrastructure.seeds.users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.models.users.UsersRoles;
import com.application.discussion.project.infrastructure.repositories.users.JpaRolesRepository;
import com.application.discussion.project.infrastructure.repositories.users.JpaUsersRepository;
import com.application.discussion.project.infrastructure.repositories.users.JpaUsersRolesRepository;

@Component
@Profile("dev")
public class UserRoleDataSeeder {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRoleDataSeeder.class);
    private static final String NORMAL_ROLE = "NORMAL";

    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    @Autowired
    private JpaRolesRepository jpaRolesRepository;

    @Autowired
    private JpaUsersRolesRepository jpaUsersRolesRepository;

    public void seed() {
        logger.info("ユーザーロールのシード処理を開始します");

        List<Users> users = jpaUsersRepository.findAll();
        List<Roles> roles = jpaRolesRepository.findAll();
        
        logger.info("データベースから{}人のユーザーと{}個のロールを取得しました", users.size(), roles.size());
        logger.debug("取得したユーザー: {}", users);
        logger.debug("取得したロール: {}", roles);

        if (roles.isEmpty()) {
            logger.error("データベースにロールが存在しません。シード処理を中止します");
            return;
        }

        Roles normalRole = roles.stream()
                .filter(role -> NORMAL_ROLE.equals(role.getRoleName()))
                .findFirst()
                .orElse(null);

        if (normalRole == null) {
            logger.error("NORMALロールが見つかりません。シード処理を中止します");
            return;
        }

        long assignedCount = users.stream()
                .filter(user -> !hasRoleAssigned(user, normalRole))
                .map(user -> createUserRole(user, normalRole))
                .peek(userRole -> jpaUsersRolesRepository.save(userRole))
                .count();

        logger.info("ユーザーロールのシード処理が完了しました。{}件のロールを割り当てました", assignedCount);
    }

    /**
     * ユーザーに指定されたロールが既に割り当てられているかチェックする
     * 
     * @param user 対象ユーザー
     * @param role 対象ロール
     * @return 既に割り当てられている場合true
     */
    private boolean hasRoleAssigned(Users user, Roles role) {
        boolean exists = jpaUsersRolesRepository.existsByUsersAndRoles(user, role);
        
        if (exists) {
            logger.debug("ユーザー[{}]には既にロール[{}]が割り当てられています。スキップします", user.getUserId(), role.getRoleName());
        }
        
        return exists;
    }

    /**
     * ユーザーロールエンティティを作成する
     * 
     * @param user 対象ユーザー
     * @param role 対象ロール
     * @return 作成されたUsersRolesエンティティ
     */
    private UsersRoles createUserRole(Users user, Roles role) {
        logger.info("ユーザー[{}]にロール[{}]を割り当てます", user.getUserId(), role.getRoleName());
        return new UsersRoles(null, user, role, null, null, null);
    }
}
