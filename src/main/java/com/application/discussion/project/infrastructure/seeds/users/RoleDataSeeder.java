package com.application.discussion.project.infrastructure.seeds.users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.repositories.users.JpaRolesRepository;

@Component
public class RoleDataSeeder {

    private final List<String> roles = List.of(
        "ADMIN",
        "PREMIUM",
        "SUPER",
        "NORMAL",
        "GUEST"
    );

    @Autowired
    private JpaRolesRepository jpaRolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleDataSeeder.class);

    public void seed() {
        
        logger.info("Seeding role data...");
        
        roles.forEach(roleName -> {
            if (!jpaRolesRepository.existsByRoleName(roleName)) {
                jpaRolesRepository.save(new Roles(null, roleName, null, null, null, null));
                logger.info("Role created: {}", roleName);
            } else {
                logger.info("Role already exists: {}", roleName);
            }
        });
    }
}
