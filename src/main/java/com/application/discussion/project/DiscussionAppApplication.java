package com.application.discussion.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.*")
@ComponentScan(basePackages = "com.application.discussion.project.*")
@EnableJpaRepositories(basePackages = "com.application.discussion.project.infrastructure.repositories")
public class DiscussionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscussionAppApplication.class, args);
	}

}
