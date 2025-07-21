package com.application.discussion.project.infrastructure.seeds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.seeds.topics.MaintopicDataSeeder;


@Component
@Profile("dev")
public class DataFactory implements CommandLineRunner {
    private final MaintopicDataSeeder maintopicDataSeeder;

    public DataFactory(MaintopicDataSeeder maintopicDataSeeder) {
        this.maintopicDataSeeder = maintopicDataSeeder;
    }
    @Override
    public void run(String... args) {
        // データシードの実行
        this.maintopicDataSeeder.seed();
        System.out.println("Data seeding completed successfully.");
        // 他のデータシーダーもここで呼び出すことができます
    }
}