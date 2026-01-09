package com.application.discussion.project.infrastructure.seeds.topics;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.exceptions.InfrastructureLayerErrorException;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.repositories.topics.JpaMaintopicsRepository;
import com.application.discussion.project.infrastructure.repositories.users.JpaUsersRepository;

@Component
@Profile("dev")
public class MaintopicDataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(MaintopicDataSeeder.class);
    private static final Random random = new Random();

    private final List<Maintopics> maintopicsSeedList = Arrays.asList(
        new Maintopics(null, "日本の政治体制の危うさについて", "日本の政治体制について議論する場所",null, null, null, false, false),
        new Maintopics(null, "古典・漢文を高校で学ぶ意義とはなにか", "社会にでれば全く役に立たない古典・漢文を日本の高校で勉強することの意義と問題を議論する場",null, null, null, false, false),
        new Maintopics(null, "日本政府の増税信仰について", "日本政府，とりわけ自民党が掲げる増税をなぜこの不景気のときに進めるのか，その対策を議論する場",null, null, null, false, false),
        new Maintopics(null, "Support", "Get help and support from the community",null, null, null, false, false),
        new Maintopics(null, "某隣の国の静かな侵略に関する", "Discuss anything that doesn't fit in other categories",null, null, null, false, false)
    );
    
    @Autowired
    private JpaMaintopicsRepository jpaMaintopicsRepository;

    @Autowired
    private JpaUsersRepository jpaUserRepository;

    public void seed(){
        List<Users> users = jpaUserRepository.findAll();
        
        if (users.isEmpty()) {
            logger.error("No users found in database. Maintopics will be created without user assignment.");
            throw new InfrastructureLayerErrorException("ユーザが存在しないため、エラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500));
        }
        
        maintopicsSeedList.stream()
            .filter(maintopic -> !jpaMaintopicsRepository.existsByTitle(maintopic.getTitle()))
            .forEach(maintopic -> {
                users.stream()
                    .skip(random.nextInt(users.size()))
                    .findFirst()
                    .ifPresent(maintopic::setUsers);
                jpaMaintopicsRepository.save(maintopic);
            });
        
        logger.info("Maintopics seeded successfully.");
    }
}
