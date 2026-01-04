package com.application.discussion.project.domain.valueobjects.users;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ノーマルユーザのロールを表す値オブジェクト
 * 一般ユーザの権限レベルを表現する
 */
public final class RoleNormalUser implements RoleType {

    private static final String ROLE_NORMAL_USER = "ROLE_NORMAL";
    private static final Logger logger = LoggerFactory.getLogger(RoleNormalUser.class);

    private final RoleName roleName;

    /**
     * デフォルトコンストラクタ（使用禁止）
     */
    private RoleNormalUser() {
        this.roleName = RoleName.reBuild(ROLE_NORMAL_USER);
    }

    /**
     * RoleNormalUserを生成する
     */
    private RoleNormalUser(RoleName roleName) {
        logger.info("Initializing RoleNormalUser with roleName: {}", roleName);
        this.roleName = roleName;
        logger.info("RoleNormalUser created successfully");
    }

    /**
     * ファクトリーメソッド：RoleNormalUserインスタンスを作成する
     * 
     * @return RoleNormalUserインスタンス
     */
    public static RoleNormalUser create() {
        logger.info("Creating RoleNormalUser instance");
        return new RoleNormalUser(RoleName.of(ROLE_NORMAL_USER));
    }

    /**
     * 再構築メソッド：既存のRoleNormalUserインスタンスを再構築する
     * 
     * @return RoleNormalUserインスタンス
     */
    public static RoleNormalUser reBuild() {
        logger.info("Rebuilding RoleNormalUser instance");
        return new RoleNormalUser(RoleName.reBuild(ROLE_NORMAL_USER));
    }

    /**
     * デフォルトのノーマルユーザロールを取得する
     * 
     * @return RoleNormalUserインスタンス
     */
    public static RoleNormalUser defaultNormalUserRole() {
        logger.info("Creating default RoleNormalUser instance");
        return new RoleNormalUser(RoleName.of(ROLE_NORMAL_USER));
    }

    /**
     * ロール名を取得する
     * 
     * @return RoleNameインスタンス
     */
    @Override
    public RoleName getRoleName() {
        return roleName;
    }

    /**
     * ロール名の文字列値を取得する
     * 
     * @return ロール名の文字列
     */
    @Override
    public String getRoleValue() {
        return roleName.getValue();
    }

    /**
     * equalsのオーバーライド
     * 
     * @param obj 比較対象のオブジェクト
     * @return 等価性の判定結果
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RoleNormalUser that = (RoleNormalUser) obj;
        return Objects.equals(roleName, that.roleName);
    }

    /**
     * hashCodeのオーバーライド
     * 
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }

    /**
     * RoleNormalUserの文字列表現を取得する
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return "RoleNormalUser{" +
                "roleName=" + roleName +
                '}';
    }
}
