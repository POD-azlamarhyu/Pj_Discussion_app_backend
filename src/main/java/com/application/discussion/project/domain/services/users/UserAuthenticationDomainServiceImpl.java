package com.application.discussion.project.domain.services.users;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.application.discussion.project.application.services.security.JWTAuthUserDetails;
import com.application.discussion.project.domain.entities.users.User;
import com.application.discussion.project.domain.exceptions.DomainLayerErrorException;
import com.application.discussion.project.domain.repositories.users.UsersRepositoryInterface;

@Service
public class UserAuthenticationDomainServiceImpl implements UserAuthenticationDomainService {

    @Autowired
    private UsersRepositoryInterface usersRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationDomainServiceImpl.class);

    @Override
    public User getAuthenticatedUser() {
        logger.info("Retrieving authenticated user from security context");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("No authenticated user found in security context");
            throw new DomainLayerErrorException("認証されたユーザーが見つかりません", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401));
        }
        logger.info("Authenticated user found: {}", authentication.toString());

        JWTAuthUserDetails userDetails = (JWTAuthUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.getUserId();
        User user = usersRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("Authenticated user with ID {} not found in repository", userId);
                return new DomainLayerErrorException("認証されたユーザーが見つかりません", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401));
            });
        logger.info("Authenticated user retrieved successfully: {}", user.getLoginId());
        return user;
    }

}
