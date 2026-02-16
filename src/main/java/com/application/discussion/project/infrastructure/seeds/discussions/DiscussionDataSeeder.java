package com.application.discussion.project.infrastructure.seeds.discussions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.application.discussion.project.infrastructure.models.discussions.Discussions;
import com.application.discussion.project.infrastructure.models.topics.Maintopics;
import com.application.discussion.project.infrastructure.models.users.Users;
import com.application.discussion.project.infrastructure.repositories.discussions.JpaDiscussionsRepository;
import com.application.discussion.project.infrastructure.repositories.topics.JpaMaintopicsRepository;
import com.application.discussion.project.infrastructure.repositories.users.JpaUsersRepository;

/**
 * ディスカッションデータのシーダークラス
 * 開発環境でのテストデータ投入を担当する
 */
@Component
@Profile("dev")
public class DiscussionDataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DiscussionDataSeeder.class);
    private static final int DISCUSSIONS_PER_MAINTOPIC = 25;
    private static final int MAINTOPIC_LIMIT = 4;

    @Autowired
    private JpaDiscussionsRepository jpaDiscussionsRepository;

    @Autowired
    private JpaMaintopicsRepository jpaMaintopicsRepository;

    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    private final Random random = new Random();

    private static final String[] DISCUSSION_TEMPLATES = {
        "このトピックについて、まず基本的な考え方を整理する必要があるよね！",
        "前の意見に賛成！さらに具体的な実装方法について検討したいかも！",
        "別の視点として、歴史的な経緯も踏まえて議論したいね！",
        "これまでの議論を踏まえて、次のステップを考えたいっしょ！",
        "実際にやってみた経験から言うと、この方法が効果的だったよ！",
        "理論的には理解できるけど、実践的にはどうなんだろう？",
        "他の事例と比較してみると、興味深い違いがあるね！",
        "長期的な視点で考えると、別のアプローチも必要かも！",
        "コストと効果のバランスを考慮に入れる必要があると思う！",
        "ユーザー目線で考えると、この点が重要だと感じるよ！",
        "技術的な課題としては、ここをクリアする必要があるっしょ！",
        "既存のシステムとの互換性も検討しないとね！",
        "セキュリティの観点からも慎重に進めたい！",
        "パフォーマンスへの影響を最小限に抑えたいね！",
        "メンテナンス性を考えると、シンプルな設計がいいかも！",
        "拡張性を持たせることで、将来的に柔軟に対応できるよ！",
        "テスト戦略についても議論したいっしょ！",
        "ドキュメント整備も並行して進めることが大切だね！",
        "チーム全体で共通認識を持つことが重要だと思う！",
        "ステークホルダーとの調整も必要になってくるね！",
        "リスク管理の観点からも検討が必要だよ！",
        "スケジュールとリソースのバランスを考えないとね！",
        "優先順位をつけて段階的に進めたいかも！",
        "フィードバックループを確立することが大事っしょ！",
        "継続的な改善の仕組みを作りたいね！"
    };

    public void seed() {
        List<Users> activeUsers = jpaUsersRepository.findAllActiveUsers();
        
        if (activeUsers.isEmpty()) {
            logger.warn("No active users found. Skipping discussion seeding.");
            return;
        }

        List<Maintopics> maintopics = jpaMaintopicsRepository.findAll();
        
        maintopics.stream()
            .limit(MAINTOPIC_LIMIT)
            .forEach(maintopic -> createDiscussionsForMaintopic(maintopic, activeUsers));
        
        logger.info("Discussions seeded successfully.");
    }

    private void createDiscussionsForMaintopic(final Maintopics maintopic, final List<Users> activeUsers) {
        List<Discussions> discussionsSeedList = new ArrayList<>();
        
        for (int i = 0; i < DISCUSSIONS_PER_MAINTOPIC; i++) {
            String paragraph = DISCUSSION_TEMPLATES[i % DISCUSSION_TEMPLATES.length];
            
            discussionsSeedList.add(
                new Discussions(
                    null,
                    paragraph,
                    maintopic,
                    getRandomUser(activeUsers),
                    null,
                    null,
                    null
                )
            );
        }

        discussionsSeedList.forEach(jpaDiscussionsRepository::save);
        
        logger.info("Created {} discussions for maintopic: {}", 
            discussionsSeedList.size(), maintopic.getTitle());
    }

    private Users getRandomUser(final List<Users> users) {
        return users.get(random.nextInt(users.size()));
    }
}
