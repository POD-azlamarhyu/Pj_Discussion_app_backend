package com.application.discussion.project.infrastructure.seeds.users;

import java.util.List;

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
    // ユーザーロールのシードデータを定義
    // ここでは、ユーザーロールの初期データを定義するロジックを実装します。
    // 例えば、管理者や一般ユーザーなどのロールを定義できます。
    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    @Autowired
    private JpaRolesRepository jpaRolesRepository;

    @Autowired
    private JpaUsersRolesRepository jpaUsersRolesRepository;

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
                System.out.println("No roles found to assign to user: " + user.getEmail());
                return null;
            }
            
            // ユーザーにロールを割り当てるロジック
            // ここでは、各ユーザーに最初のロールを割り当てる例を示します。
            UsersRoles userRole = new UsersRoles(null, user, normalRole, null, null, null);
            return userRole;
        }).forEach(userRole -> {
            if (userRole != null) {
                // ユーザーロールを保存
                jpaUsersRolesRepository.save(userRole);
            }
        });
        System.out.println("Seeding user roles...");
        // ここにユーザーロール作成処理を書く
    }
}
