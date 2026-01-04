package com.application.discussion.project.infrastructure.repositories.users;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.domain.valueobjects.users.Email;
import com.application.discussion.project.infrastructure.models.users.Users;

@Repository
public class UsersRepositoryImpl implements UsersRepositoryInterface{
    
    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsersRepositoryImpl.class);


    @Override
    public Optional<Users> findByEmailOrLoginId(String emailOrLoginId){
        logger.debug("Searching for user with email or login ID: {}", emailOrLoginId);
        return jpaUsersRepository.findByEmailOrLoginId(emailOrLoginId, emailOrLoginId);
    }

    @Override
    public void saveSeededUsers(Users user) {
        Users editedUser = new Users();
        editedUser.setLoginId(user.getLoginId());
        editedUser.setEmail(user.getEmail());
        editedUser.setPassword(user.getPassword());
        jpaUsersRepository.save(editedUser);
    }

    @Override
    public Boolean existsByEmail(Email email) {
        logger.info("Checking existence of email: {}", email.toString());
        
        Boolean exists = jpaUsersRepository.existsByEmail(email.value());
        
        logger.info("email existence result: exists={}", exists);
        
        return exists;
    }

    @Override
    public User save(final User user) {
        logger.info("Saving user with loginId: {}", user.getLoginId());
        Users usersModel = mapToModelEntity(user);
        Users savedUser = jpaUsersRepository.save(usersModel);
        
        logger.info("User saved successfully with id: {}", savedUser.getUserId());
        
        return mapToDomainEntity(savedUser);
    }

    /**
     * UsersモデルをUserドメインエンティティにマッピングする
     * @param usersModel マッピング元のUsersモデル
     * @return Userドメインエンティティ
     */
    private User mapToDomainEntity(final Users usersModel) {
        logger.info("Mapping Users model to User domain entity for userId: {}", 
            Objects.nonNull(usersModel) ? usersModel.getUserId() : "null");
        if (Objects.isNull(usersModel)) {
            return null;
        }

        return User.reBuild(
            usersModel.getUserId(),
            usersModel.getLoginId(),
            usersModel.getEmail(),
            usersModel.getLoginId(),
            usersModel.getPassword(),
            usersModel.getCreatedAt(),
            usersModel.getUpdatedAt(),
            usersModel.getDeletedAt(),
            usersModel.getIsActive(),
            usersModel.getIsDeleted()
        );
    }

    /**
     * UserドメインエンティティをUsersモデルにマッピングする
     * @param userEntity マッピング元のUserドメインエンティティ
     * @return Usersモデル
     */
    private Users mapToModelEntity(final User userEntity) {
        logger.info("Mapping User domain entity to Users model for userId: {}", 
            Objects.nonNull(userEntity) ? userEntity.getUserId() : "null");
        if (Objects.isNull(userEntity)) {
            return null;
        }

        Users usersModel = new Users();
        usersModel.setLoginId(userEntity.getLoginId().value());
        usersModel.setEmail(userEntity.getEmail().value());
        usersModel.setPassword(userEntity.getPassword().value());
        usersModel.setUsername(userEntity.getUserName().value());
        usersModel.setCreatedAt(userEntity.getCreatedAt());
        usersModel.setUpdatedAt(userEntity.getUpdatedAt());
        usersModel.setDeletedAt(userEntity.getDeletedAt());
        usersModel.setIsActive(userEntity.getIsActive());
        usersModel.setIsDeleted(userEntity.getIsDeleted());

        return usersModel;
    }
}
