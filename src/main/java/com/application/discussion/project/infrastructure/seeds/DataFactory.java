package com.application.discussion.project.infrastructure.seeds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.seeds.topics.MaintopicDataSeeder;
import com.application.discussion.project.infrastructure.seeds.discussions.DiscussionDataSeeder;

import com.application.discussion.project.infrastructure.seeds.users.RoleDataSeeder;
import com.application.discussion.project.infrastructure.seeds.users.UserDataSeeder;
import com.application.discussion.project.infrastructure.seeds.users.UserRoleDataSeeder;

@Component
@Profile("dev")
public class DataFactory implements CommandLineRunner {
    private final MaintopicDataSeeder maintopicDataSeeder;
    private final DiscussionDataSeeder discussionsDataSeeder;
    private final RoleDataSeeder roleDataSeeder;
    private final UserDataSeeder userDataSeeder;
    private final UserRoleDataSeeder userRoleDataSeeder;

    private static final Logger logger = LoggerFactory.getLogger(DataFactory.class);

    public DataFactory(
        MaintopicDataSeeder maintopicDataSeeder,
        DiscussionDataSeeder discussionsDataSeeder,
        RoleDataSeeder roleDataSeeder,
        UserDataSeeder userDataSeeder,
        UserRoleDataSeeder userRoleDataSeeder
    ) {
        this.maintopicDataSeeder = maintopicDataSeeder;
        this.discussionsDataSeeder = discussionsDataSeeder;
        this.roleDataSeeder = roleDataSeeder;
        this.userDataSeeder = userDataSeeder;
        this.userRoleDataSeeder = userRoleDataSeeder;
    }
    @Override
    public void run(String... args) {
        logger.info("Seeding initial data...");
        this.maintopicDataSeeder.seed();
        this.discussionsDataSeeder.seed();
        this.roleDataSeeder.seed();
        this.userDataSeeder.seed();
        // this.userRoleDataSeeder.seed();
        logger.info("Data seeding completed.");
    }
}
