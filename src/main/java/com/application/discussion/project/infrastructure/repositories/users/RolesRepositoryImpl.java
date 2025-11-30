package com.application.discussion.project.infrastructure.repositories.users;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.infrastructure.models.users.Roles;

@Repository
public class RolesRepositoryImpl implements RolesRepositoryInterface {
    
    @Autowired
    private JpaUsersRolesRepository jpaUsersRolesRepository;

    private static final Logger logger = LoggerFactory.getLogger(RolesRepositoryImpl.class);

    @Override
    public Set<Roles> findUserRolesById(UUID userId) {
        logger.info("Fetching roles for user ID: {}", userId);
        List<Object[]> roles = jpaUsersRolesRepository.findUserRolesByUUID(userId);

        logger.info("Roles fetched: {}", roles);
        Set<Roles> rolesSet = roles.stream()
                            .map(role -> (Roles) role[0])
                            .collect(Collectors.toCollection(HashSet::new));
        
        return rolesSet;
    }

}
