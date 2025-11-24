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

        String NORMAL_ROLE= "NORMAL";
        Roles normalRole = roles.stream()
            .filter(role -> NORMAL_ROLE.equals(role.getRoleName()))
            .findFirst()
            .orElse(null);

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
