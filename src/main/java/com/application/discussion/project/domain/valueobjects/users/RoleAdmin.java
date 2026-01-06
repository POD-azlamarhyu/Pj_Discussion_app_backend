package com.application.discussion.project.domain.valueobjects.users;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * アドミンのロールを表す値オブジェクト
 * 管理者権限レベルを表現する
 */
public final class RoleAdmin implements RoleType {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Logger logger = LoggerFactory.getLogger(RoleAdmin.class);

    private final RoleName roleName;

    /**
     * デフォルトコンストラクタ（使用禁止）
     */
    private RoleAdmin() {
        this.roleName = RoleName.reBuild(ROLE_ADMIN);
    }

    /**
     * RoleAdminを生成する
     */
    private RoleAdmin(RoleName roleName) {
        logger.info("Initializing RoleAdmin with roleName: {}", roleName);
        this.roleName = roleName;
        logger.info("RoleAdmin created successfully");
    }

    /**
     * ファクトリーメソッド：RoleAdminインスタンスを作成する
     * 
     * @return RoleAdminインスタンス
     */
    public static RoleAdmin create() {
        logger.info("Creating RoleAdmin instance");
        return new RoleAdmin(RoleName.of(ROLE_ADMIN));
    }

    /**
     * 再構築メソッド：既存のRoleAdminインスタンスを再構築する
     * 
     * @return RoleAdminインスタンス
     */
    public static RoleAdmin reBuild() {
        logger.info("Rebuilding RoleAdmin instance");
        return new RoleAdmin(RoleName.reBuild(ROLE_ADMIN));
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
        RoleAdmin roleAdmin = (RoleAdmin) obj;
        return Objects.equals(roleName, roleAdmin.roleName);
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
     * RoleAdminの文字列表現を取得する
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return "RoleAdmin{" +
                "roleName=" + roleName +
                '}';
    }
}
