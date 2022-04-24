package com.cmc.meeron.user.adapter.out;

import com.cmc.meeron.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(
            "select u" +
            " from User u left join WorkspaceUser wu on u.id = wu.user.id " +
            " where wu.id = :workspaceUserId"
    )
    Optional<User> findByWorkspaceUserId(@Param("workspaceUserId") Long workspaceUserId);
}
