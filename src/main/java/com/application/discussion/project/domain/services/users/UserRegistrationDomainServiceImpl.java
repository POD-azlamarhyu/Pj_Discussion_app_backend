package com.application.discussion.project.domain.services.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.valueobjects.users.Email;

/**
 * ユーザードメインサービス
 * 複数のエンティティやリポジトリに跨るドメインロジックを提供する
 */
@Service
public class UserRegistrationDomainServiceImpl implements UserRegistrationDomainService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationDomainServiceImpl.class);
    
    @Autowired
    private UsersRepositoryInterface usersRepositoryInterface;

    /**
     * メールアドレスの重複チェックを行う
     * 既に同じメールアドレスが存在する場合は例外をスローする
     * 
     * @param email チェック対象のメールアドレス
     * @throws DomainLayerErrorException メールアドレスが既に存在する場合
     */
    @Override
    public void ensureEmailIsUnique(Email email) {
        logger.info("Starting email uniqueness check: email={}", email.toString());
        Boolean exists = usersRepositoryInterface.existsByEmail(email);
        
        if (exists) {
            logger.error("Email duplication detected: email={}", email.value());
            throw new DomainLayerErrorException(
                "このメールアドレスは既に登録されています",
                HttpStatus.CONFLICT,
                HttpStatusCode.valueOf(409)
            );
        }
        
        logger.info("Email uniqueness check passed: email={}", email.value());
    }
}
