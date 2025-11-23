package com.application.discussion.project.application.services.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailOrLoginId) {
        Users user = usersRepository.findByEmailOrLoginId(emailOrLoginId)
            .orElseThrow(() -> new RuntimeException("User not found with email or loginId: " + emailOrLoginId));
        
        Set<Roles> roles = rolesRepository.findUserRolesById(user.getUserId());

        return JWTAuthUserDetails.build(user, roles);
    }
}
