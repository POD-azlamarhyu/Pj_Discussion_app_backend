package com.application.discussion.project.infrastructure.repositories.users;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.domain.repositories.users.RolesRepositoryInterface;
import com.application.discussion.project.infrastructure.models.users.Roles;

@Repository
public class RolesRepositoryImpl implements RolesRepositoryInterface {
    
    @Autowired
    private JpaUsersRolesRepository jpaUsersRolesRepository;


    @Override
    public Set<Roles> findUserRolesById(UUID userId) {
        List<Object[]> roles = jpaUsersRolesRepository.findUserRolesByUUID(userId);
        Set<Roles> rolesSet = roles.stream()
                            .map(role -> (Roles) role[0])
                            .collect(Collectors.toCollection(HashSet::new));
        
        return rolesSet;
    }

}
