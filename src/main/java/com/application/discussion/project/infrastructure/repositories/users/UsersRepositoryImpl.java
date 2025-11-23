package com.application.discussion.project.infrastructure.repositories.users;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
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
}
