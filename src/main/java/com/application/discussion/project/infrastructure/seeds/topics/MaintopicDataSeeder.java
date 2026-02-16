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
    private final Random random = new Random();

    private final List<Maintopics> maintopicsSeedList = Arrays.asList(
        new Maintopics(null, "日本の政治体制の危うさについて", "日本の政治体制について議論する場所", null, null, null, false, false),
        new Maintopics(null, "古典・漢文を高校で学ぶ意義とはなにか", "社会にでれば全く役に立たない古典・漢文を日本の高校で勉強することの意義と問題を議論する場", null, null, null, false, false),
        new Maintopics(null, "日本政府の増税信仰について", "日本政府、とりわけ自民党が掲げる増税をなぜこの不景気のときに進めるのか、その対策を議論する場", null, null, null, false, false),
        new Maintopics(null, "某隣の国の静かな侵略に関する", "近隣諸国との関係性と日本の安全保障について議論する場", null, null, null, false, false),
        new Maintopics(null, "AIと人間の共存について", "AI技術の発展が社会や雇用に与える影響と、人間との共存のあり方を議論する場", null, null, null, false, false),
        new Maintopics(null, "環境問題と経済成長の両立", "持続可能な社会を実現するための環境保護と経済発展のバランスについて議論する場", null, null, null, false, false),
        new Maintopics(null, "日本の教育システムの課題", "現代社会に適応した教育制度の改革と、創造性を育む教育について議論する場", null, null, null, false, false),
        new Maintopics(null, "働き方改革の実効性", "長時間労働の是正とワークライフバランスの実現に向けた課題を議論する場", null, null, null, false, false),
        new Maintopics(null, "少子高齢化社会への対策", "人口減少と高齢化が進む日本の社会保障と経済への影響について議論する場", null, null, null, false, false),
        new Maintopics(null, "デジタル化社会のセキュリティ", "サイバーセキュリティとプライバシー保護の重要性について議論する場", null, null, null, false, false),
        new Maintopics(null, "地方創生と東京一極集中", "地方の活性化と都市部への人口集中の問題について議論する場", null, null, null, false, false),
        new Maintopics(null, "ジェンダー平等の実現に向けて", "男女平等やLGBTQ+の権利について、多様性を尊重する社会を議論する場", null, null, null, false, false),
        new Maintopics(null, "メディアリテラシーの重要性", "情報社会における正しい情報の見極め方とフェイクニュース対策について議論する場", null, null, null, false, false),
        new Maintopics(null, "食料自給率の向上策", "日本の農業振興と食の安全保障について議論する場", null, null, null, false, false),
        new Maintopics(null, "エネルギー政策の未来", "再生可能エネルギーと原子力発電、化石燃料のバランスについて議論する場", null, null, null, false, false),
        new Maintopics(null, "大学教育の在り方", "高等教育の質と学費負担、奨学金制度について議論する場", null, null, null, false, false),
        new Maintopics(null, "医療制度の持続可能性", "国民皆保険制度の維持と医療費増大への対策について議論する場", null, null, null, false, false),
        new Maintopics(null, "観光立国としての日本", "インバウンド観光と地域活性化の戦略について議論する場", null, null, null, false, false),
        new Maintopics(null, "企業のDX推進", "デジタルトランスフォーメーションによる業務効率化と競争力強化について議論する場", null, null, null, false, false),
        new Maintopics(null, "スタートアップ支援策", "日本の起業環境の改善とイノベーション創出について議論する場", null, null, null, false, false),
        new Maintopics(null, "リモートワークの定着", "テレワークの普及と新しい働き方の課題について議論する場", null, null, null, false, false),
        new Maintopics(null, "災害対策と防災意識", "自然災害への備えと地域コミュニティの防災力向上について議論する場", null, null, null, false, false),
        new Maintopics(null, "子育て支援の充実", "保育園待機児童問題や育児休業制度の改善について議論する場", null, null, null, false, false),
        new Maintopics(null, "スポーツと社会の関係", "スポーツを通じた健康増進と地域活性化について議論する場", null, null, null, false, false),
        new Maintopics(null, "文化芸術の振興", "日本の伝統文化の継承と現代アートの発展について議論する場", null, null, null, false, false)
    );
    
    @Autowired
    private JpaMaintopicsRepository jpaMaintopicsRepository;

    @Autowired
    private JpaUsersRepository jpaUserRepository;

    public void seed() {
        List<Users> users = jpaUserRepository.findAll();
        
        if (users.isEmpty()) {
            logger.error("No users found in database. Maintopics will be created without user assignment.");
            throw new InfrastructureLayerErrorException("ユーザが存在しないため、エラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500));
        }
        
        maintopicsSeedList.stream()
            .filter(maintopic -> !jpaMaintopicsRepository.existsByTitle(maintopic.getTitle()))
            .forEach(maintopic -> {
                maintopic.setUsers(getRandomUser(users));
                jpaMaintopicsRepository.save(maintopic);
                logger.info("Seeded maintopic: {}", maintopic.getTitle());
            });
        
        logger.info("Maintopics seeded successfully.");
    }

    private Users getRandomUser(final List<Users> users) {
        return users.get(random.nextInt(users.size()));
    }
}
