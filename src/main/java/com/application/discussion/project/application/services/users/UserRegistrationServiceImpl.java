package com.application.discussion.project.application.services.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.discussion.project.application.dtos.users.SignUpRequest;
import com.application.discussion.project.application.dtos.users.SignUpResponse;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.services.users.UserRegistrationDomainServiceImpl;
import com.application.discussion.project.domain.valueobjects.users.Email;
import com.application.discussion.project.domain.valueobjects.users.Password;

/**
 * ユーザー登録アプリケーションサービス
 * ユーザー登録のユースケースを実現する
 */
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    @Autowired
    private UsersRepositoryInterface usersRepositoryInterface;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRegistrationDomainServiceImpl userRegistrationDomainService;

    /**
     * ユーザーを登録する
     * 
     * @param request ユーザー登録リクエスト
     * @return ユーザー登録レスポンス
     */
    @Transactional
    @Override
    public SignUpResponse service(final SignUpRequest request) {
        logger.info("starting user registration: email={}, user={}", request.getEmail(), request.getUsername());

        Email email = Email.of(request.getEmail());
        userRegistrationDomainService.ensureEmailIsUnique(email);

        final User newUser = User.create(
            request.getUsername(),
            request.getEmail(),
            request.getPassword()
        );
        final Password hashedPassword = Password.reBuildHashed(
            passwordEncoder.encode(newUser.getPassword().value())
        );

        final User userEntityWithHashPassword = newUser.reBuildWithHashedPassword(hashedPassword);

        if (userEntityWithHashPassword.getPassword().isHashed(request.getPassword(), passwordEncoder)) {
            logger.info("Password has been successfully hashed for user: {}", userEntityWithHashPassword.getLoginId());
        } else {
            logger.error("Password hashing failed for user: {}", userEntityWithHashPassword.getLoginId());
            throw new ApplicationLayerException("不明なエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatusCode.valueOf(500));
        }

        final User savedUser = usersRepositoryInterface.save(userEntityWithHashPassword);
        
        logger.info("ユーザー登録が完了しました: userId={}", savedUser.getUserId());

        return SignUpResponse.of(
            savedUser.getUserId().toString(),
            savedUser.getUserName().value(),
            savedUser.getEmail().value(),
            savedUser.getCreatedAt(),
            savedUser.getIsActive().toString()
        );
    }
}
