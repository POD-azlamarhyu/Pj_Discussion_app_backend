package com.application.discussion.project.infrastructure.seeds.users;

import java.util.List;


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

    public void seed() {
        // テスト用ロールデータ投入ロジック
        System.out.println("Seeding test role data...");
        // ここにテスト用ロール作成処理を書く
        roles.forEach(roleName -> {
            if (!jpaRolesRepository.existsByRoleName(roleName)) {
                jpaRolesRepository.save(new Roles(null, roleName, null, null, null, null));
                System.out.println("Created role: " + roleName);
            } else {
                System.out.println("Role already exists: " + roleName);
            }
        });
    }
}
