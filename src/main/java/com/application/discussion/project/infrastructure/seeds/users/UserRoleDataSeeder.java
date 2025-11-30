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
    
    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    @Autowired
    private JpaRolesRepository jpaRolesRepository;

    @Autowired
    private JpaUsersRolesRepository jpaUsersRolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserRoleDataSeeder.class);

    public void seed() {
        List<Users> users = jpaUsersRepository.findAll();
        List<Roles> roles = jpaRolesRepository.findAll();
        logger.info("Seeding user roles...");

        logger.info("Found {} users and {} roles in the database.", users.size(), roles.size());
        logger.info("user available: {}", users);
        logger.info("roles available: {}", roles);
        String NORMAL_ROLE= "NORMAL";
        Roles normalRole = roles.stream()
            .filter(role -> NORMAL_ROLE.equals(role.getRoleName()))
            .findFirst()
            .orElse(null);
        /**
         * * ユーザとロールの関連付けについて、すべてのユーザに対してNORMALロールを割り当てる処理
         * ! 注意: 既存の関連付けがある場合、重複して割り当てられる可能性があります。
         * ! 重複チェックをする必要がある
         */
        users.stream().map(user ->{
            if (roles.isEmpty()) {
                logger.error("No roles found in the database.");
                return null;
            }
            UsersRoles userRole = new UsersRoles(null, user, normalRole, null, null, null);
            return userRole;
        }).forEach(userRole -> {
            if (userRole != null) {
                jpaUsersRolesRepository.save(userRole);
            }
        });
        logger.info("User roles seeding completed.");
    }
}
