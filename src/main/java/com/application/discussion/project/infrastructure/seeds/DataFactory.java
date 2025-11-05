package com.application.discussion.project.infrastructure.seeds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.seeds.topics.MaintopicDataSeeder;
import com.application.discussion.project.infrastructure.seeds.discussions.DiscussionDataSeeder;


@Component
@Profile("dev")
public class DataFactory implements CommandLineRunner {
    private final MaintopicDataSeeder maintopicDataSeeder;
    private final DiscussionDataSeeder discussionsDataSeeder;

    private static final Logger logger = LoggerFactory.getLogger(DataFactory.class);

    public DataFactory(
        MaintopicDataSeeder maintopicDataSeeder,
        DiscussionDataSeeder discussionsDataSeeder
    ) {
        this.maintopicDataSeeder = maintopicDataSeeder;
        this.discussionsDataSeeder = discussionsDataSeeder;
    }
    @Override
    public void run(String... args) {
        logger.info("Seeding initial data...");
        this.maintopicDataSeeder.seed();
        this.discussionsDataSeeder.seed();
        logger.info("Data seeding completed.");
    }
}