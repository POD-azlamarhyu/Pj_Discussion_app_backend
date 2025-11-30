package com.application.discussion.project.application.services.security;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;
import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;
import com.application.discussion.project.infrastructure.models.users.Roles;
import com.application.discussion.project.infrastructure.models.users.Users;


@Service
public class JWTAuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepositoryInterface usersRepository;
    
    @Autowired
    private RolesRepositoryInterface rolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthUserDetailsService.class);

    /**
     * メールアドレスまたはログインIDからユーザー詳細情報をロードする
     * 
     * @param emailOrLoginId メールアドレスまたはログインID
     * @return ユーザー詳細情報を含むUserDetailsオブジェクト
     * @throws RuntimeException ユーザーが見つからない場合にスローされる例外
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailOrLoginId) {
        logger.info("Loading user details by email or login ID: {}", emailOrLoginId);
        Users user = usersRepository.findByEmailOrLoginId(emailOrLoginId)
            .orElseThrow(() -> new ApplicationLayerException(
                "ユーザーが見つかりません", 
                HttpStatus.NOT_FOUND, 
                HttpStatusCode.valueOf(404)
            ));
        
        Set<Roles> roles = rolesRepository.findUserRolesById(user.getUserId());
        logger.info("User found: {}, Roles: {}", user.getUserId(), roles);
        return JWTAuthUserDetails.build(user, roles);
    }
}
