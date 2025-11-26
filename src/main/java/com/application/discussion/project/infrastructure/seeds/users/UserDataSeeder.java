package com.application.discussion.project.infrastructure.seeds.users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.repositories.users.JpaUsersRepository;

@Component
@Profile("dev")
public class UserDataSeeder {


    private PasswordEncoder passwordEncoder;

    private JpaUsersRepository jpaUsersRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDataSeeder.class);

    public UserDataSeeder(
        PasswordEncoder passwordEncoder, 
        JpaUsersRepository jpaUsersRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jpaUsersRepository = jpaUsersRepository;
    }
    

    public void seed() {
        logger.info("Seeding user data...");
        List<Users> users = createTestUsers();
        users.forEach(user -> {
            if (!jpaUsersRepository.existsByEmail(user.getEmail())) {
                logger.debug("Creating user with email: " + user.getEmail());
                jpaUsersRepository.save(user);
            } else {
                logger.debug("User with email already exists: " + user.getEmail());
            }
        });
    }

    private List<Users> createTestUsers() {
        return List.of(
            createUser("testuser1", "testuser1@example.com", "passwordskl"),
            createUser("testuser2", "testuser2@example.com", "szin09skl"),
            createUser("testuser3", "testuser3@example.com", "password1"),
            createUser("testuser4", "testuser4@example.com", "password4"),
            createUser("testuser5", "testuser5@example.com", "password5"),
            createUser("testuser6", "testuser6@example.com", "password6"),
            createUser("testuser7", "testuser7@example.com", "password7"),
            createUser("testuser8", "testuser8@example.com", "password8"),
            createUser("testuser9", "testuser9@example.com", "password9"),
            createUser("testuser10", "testuser10@example.com", "password10"),
            createUser("testuser11", "testuser11@example.com", "password11"),
            createUser("testuser12", "testuser12@example.com", "password12"),
            createUser("testuser13", "testuser13@example.com", "password13"),
            createUser("testuser14", "testuser14@example.com", "password14"),
            createUser("testuser15", "testuser15@example.com", "password15"),
            createUser("testuser16", "testuser16@example.com", "password16"),
            createUser("testuser17", "testuser17@example.com", "password17"),
            createUser("testuser18", "testuser18@example.com", "password18"),
            createUser("testuser19", "testuser19@example.com", "password19"),
            createUser("testuser20", "testuser20@example.com", "password20"),
            createUser("testuser21", "testuser21@example.com", "pASswOrd20bVs"),
            createUser("testuser22", "testuser22@example.com", "svNkf93nsss")
        );
    }

    private Users createUser(String loginId, String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        return new Users(
            null, 
            loginId, 
            email, 
            encodedPassword, 
            null, 
            null, 
            null, 
            null, 
            true, 
            false
        );
    }

}
