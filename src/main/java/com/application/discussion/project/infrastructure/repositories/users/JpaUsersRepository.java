package com.application.discussion.project.infrastructure.repositories.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.discussion.project.infrastructure.models.users.Users;

@Repository
public interface JpaUsersRepository extends JpaRepository<Users, UUID> {

    @Query(
        value="select * from users as u where u.login_id = ?1 or u.email = ?2",
        nativeQuery=true
    )
    Optional<Users> findByEmailOrLoginId(String email,String loginId);

    Boolean existsByEmail(String email);
    Boolean existsByLoginId(String loginId);
}
