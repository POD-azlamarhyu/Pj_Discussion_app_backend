package com.application.discussion.project.infrastructure.seeds.topics;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.repositories.topics.JpaMaintopicsRepository;

@Component
@Profile("dev")
public class MaintopicDataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(MaintopicDataSeeder.class);

    private final List<Maintopics> maintopicsSeedList = Arrays.asList(
        new Maintopics(null, "日本の政治体制の危うさについて", "日本の政治体制について議論する場所", null, null, false, false),
        new Maintopics(null, "古典・漢文を高校で学ぶ意義とはなにか", "社会にでれば全く役に立たない古典・漢文を日本の高校で勉強することの意義と問題を議論する場", null, null, false, false),
        new Maintopics(null, "日本政府の増税信仰について", "日本政府，とりわけ自民党が掲げる増税をなぜこの不景気のときに進めるのか，その対策を議論する場", null, null, false, false),
        new Maintopics(null, "Support", "Get help and support from the community", null, null, false, false),
        new Maintopics(null, "某隣の国の静かな侵略に関する", "Discuss anything that doesn't fit in other categories", null, null, false, false)
    );
    
    @Autowired
    private JpaMaintopicsRepository jpaMaintopicsRepository;

    public void seed(){
        maintopicsSeedList.stream()
            .filter(maintopic -> !jpaMaintopicsRepository.existsByTitle(maintopic.getTitle()))
            .forEach(maintopic -> jpaMaintopicsRepository.save(maintopic));
        logger.info("Maintopics seeded successfully.");
    }
}
