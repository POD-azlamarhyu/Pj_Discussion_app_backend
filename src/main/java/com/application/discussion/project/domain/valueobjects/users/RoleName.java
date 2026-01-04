package com.application.discussion.project.domain.valueobjects.users;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;

/**
 * ロール名を表す値オブジェクト
 * ユーザの権限を識別するためのロール名を保持する
 */
public class RoleName {

    private static final int MAX_LENGTH = 50;

    private final String value;

    private static final Logger logger = LoggerFactory.getLogger(RoleName.class);

    /**
     * デフォルトコンストラクタ（使用禁止）
     */
    private RoleName() {
        this.value = "";
    }

    /**
     * RoleNameを生成する
     * 
     * @param value ロール名
     * @throws IllegalArgumentException ロール名がnull、空文字、または最大長を超える場合
     */
    private RoleName(String value, Boolean isSkipValidation) {
        logger.info("Initializing RoleName with value: {}", value);
        if (!isSkipValidation) {
            validate(value);
        }
        this.value = value;
        logger.info("RoleName created with value: {}", value);
    }

    /**
     * RoleNameを生成する
     * 
     * @param value ロール名
     * @throws DomainLayerErrorException ロール名がnull、空文字、または最大長を超える場合
     */
    private void validate(String value) {
        if (Objects.isNull(value) || StringUtils.isBlank(value)) {
            logger.error("RoleName validation failed: value is null or empty");
            throw new DomainLayerErrorException("ロール名は必須項目です", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400));
        }
        if (value.length() > MAX_LENGTH) {
            logger.error("RoleName validation failed: value exceeds max length of {}", MAX_LENGTH);
            throw new DomainLayerErrorException("ロール名は最大 " + MAX_LENGTH + " 文字以内で指定してください", HttpStatus.BAD_REQUEST, HttpStatus.valueOf(400));
        }
        logger.info("RoleName validation passed for value: {}", value);
    }

    /**
     * ファクトリーメソッド：RoleNameインスタンスを作成する
     * 
     * @param value ロール名
     * @return RoleNameインスタンス
     */
    public static RoleName of(String value) {
        logger.info("Creating RoleName with value: {}", value);
        return new RoleName(value, Boolean.FALSE);
    }

    /**
     * 再構築メソッド：既存のRoleNameインスタンスを再構築する
     * 
     * @param value ロール名
     * @return RoleNameインスタンス
     */
    public static RoleName reBuild(String value) {
        logger.info("Rebuilding RoleName with value: {}", value);
        return new RoleName(value, Boolean.TRUE);
    }

    /**
     * ロール名の値を取得する
     * 
     * @return ロール名
     */
    public String getValue() {
        return value;
    }

    /**
     * equalsおよびhashCodeのオーバーライド
     * @return 等価性の判定およびハッシュコード
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RoleName roleName = (RoleName) obj;
        return Objects.equals(value, roleName.value);
    }

    /**
     * hashCodeのオーバーライド
     * 
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * RoleNameの文字列表現を取得する
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return "RoleName{" +
                "value='" + value + '\'' +
                '}';
    }
}
