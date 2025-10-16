package com.application.discussion.project.infrastructure.seeds.discussions;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.models.discussions.Discussions;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.repositories.discussions.JpaDiscussionsRepository;
import com.application.discussion.project.infrastructure.repositories.topics.JpaMaintopicsRepository;

/**
 * ディスカッションデータのシーダークラス
 * 開発環境でのテストデータ投入を担当する
 */
@Component
@Profile("dev")
public class DiscussionDataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DiscussionDataSeeder.class);

    @Autowired
    private JpaDiscussionsRepository jpaDiscussionsRepository;

    @Autowired
    private JpaMaintopicsRepository jpaMaintopicsRepository;

    public void seed() {
        List<Maintopics> maintopics = jpaMaintopicsRepository.findAll();
        
        maintopics.stream()
            .limit(3)
            .forEach(this::createDiscussionsForMaintopic);
        
        logger.info("Discussions seeded successfully.");
    }

    private void createDiscussionsForMaintopic(Maintopics maintopic) {
        List<Discussions> discussionsSeedList = Arrays.asList(
            Discussions.builder()
                .paragraph("このトピックについて、まず基本的な考え方を整理する必要があるよね。前提条件として、現状の課題を明確にすることが重要だと思うんだけど。")
                .maintopic(maintopic)
                .build(),
            Discussions.builder()
                .paragraph("前の意見に賛成！さらに具体的な実装方法について検討したいかも。特に、実現可能性と効果のバランスを考える必要があるっしょ。")
                .maintopic(maintopic)
                .build(),
            Discussions.builder()
                .paragraph("別の視点として、歴史的な経緯も踏まえて議論したいね。過去の事例から学べることは多いはず。長期的な影響も考慮に入れるべきだと思う。")
                .maintopic(maintopic)
                .build(),
            Discussions.builder()
                .paragraph("これまでの議論を踏まえて、次のステップとして具体的なアクションプランを策定することを提案するよ。実行可能な範囲で進めていきたいね。")
                .maintopic(maintopic)
                .build()
        );

        discussionsSeedList.forEach(discussion -> {
            jpaDiscussionsRepository.save(discussion);
        });
        
        logger.info("Created {} discussions for maintopic: {}", 
            discussionsSeedList.size(), maintopic.getTitle());
    }
}
