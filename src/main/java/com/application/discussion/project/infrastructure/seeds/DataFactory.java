package com.application.discussion.project.infrastructure.seeds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.seeds.users.RoleDataSeeder;
import com.application.discussion.project.infrastructure.seeds.users.UserDataSeeder;
import com.application.discussion.project.infrastructure.seeds.users.UserRoleDataSeeder;

@Component
@Profile("dev")
public class DataFactory implements CommandLineRunner {
    
    private final RoleDataSeeder roleDataSeeder;
    private final UserDataSeeder userDataSeeder;
    private final UserRoleDataSeeder userRoleDataSeeder;

    public DataFactory(
        RoleDataSeeder roleDataSeeder,
        UserDataSeeder userDataSeeder,
        UserRoleDataSeeder userRoleDataSeeder
    ) {
        this.roleDataSeeder = roleDataSeeder;
        this.userDataSeeder = userDataSeeder;
        this.userRoleDataSeeder = userRoleDataSeeder;
    }

    @Override
    public void run(String... args) {
        // データシードの実行
        roleDataSeeder.seed();
        userDataSeeder.seed();
        userRoleDataSeeder.seed();
        System.out.println("Data seeding completed successfully.");
        // 他のデータシーダーもここで呼び出すことができます
    }
}
