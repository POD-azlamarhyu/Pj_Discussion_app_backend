package com.application.discussion.project.infrastructure.seeds.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.repositories.users.JpaUsersRepository;

@Component
@Profile("dev")
public class UserDataSeeder{
    private final List<Users> users = List.of(
        new Users(null, "testuser1", "testuser1@example.com", "password1", null, null, null, null,false,false),
        new Users(null, "testuser2", "testuser2@example.com", "password1", null, null, null, null,false,false),
        new Users(null, "testuser3", "testuser3@example.com", "password1", null, null, null, null,false,false),
        new Users(null, "testuser4", "testuser4@example.com", "password4", null, null, null, null, false, false),
        new Users(null, "testuser5", "testuser5@example.com", "password5", null, null, null, null, false, false),
        new Users(null, "testuser6", "testuser6@example.com", "password6", null, null, null, null, false, false),
        new Users(null, "testuser7", "testuser7@example.com", "password7", null, null, null, null, false, false),
        new Users(null, "testuser8", "testuser8@example.com", "password8", null, null, null, null, false, false),
        new Users(null, "testuser9", "testuser9@example.com", "password9", null, null, null, null, false, false),
        new Users(null, "testuser10", "testuser10@example.com", "password10", null, null, null, null, false, false),
        new Users(null, "testuser11", "testuser11@example.com", "password11", null, null, null, null, false, false),
        new Users(null, "testuser12", "testuser12@example.com", "password12", null, null, null, null, false, false),
        new Users(null, "testuser13", "testuser13@example.com", "password13", null, null, null, null, false, false),
        new Users(null, "testuser14", "testuser14@example.com", "password14", null, null, null, null, false, false),
        new Users(null, "testuser15", "testuser15@example.com", "password15", null, null, null, null, false, false),
        new Users(null, "testuser16", "testuser16@example.com", "password16", null, null, null, null, false, false),
        new Users(null, "testuser17", "testuser17@example.com", "password17", null, null, null, null, false, false),
        new Users(null, "testuser18", "testuser18@example.com", "password18", null, null, null, null, false, false),
        new Users(null, "testuser19", "testuser19@example.com", "password19", null, null, null, null, false, false),
        new Users(null, "testuser20", "testuser20@example.com", "password20", null, null, null, null, false, false)
    );

    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    public void seed() {
        // テスト用ユーザーデータ投入ロジック
        System.out.println("Seeding test user data...");
        // ここにテスト用ユーザー作成処理を書く
        users.forEach(user -> {
            if (!jpaUsersRepository.existsByEmail(user.getEmail())) {
                jpaUsersRepository.save(user);
            } else {
                System.out.println("User with email or login ID already exists: " + user.getEmail());
            }
        });
    }

}
