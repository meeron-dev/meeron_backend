package com.cmc.meeron.workspace.domain.repository;

import com.cmc.meeron.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {

    @Query("select w" +
            " from Workspace w" +
            " join WorkspaceUser wu on w.id = wu.workspace.id" +
            " where wu.user.id = :userId")
    List<Workspace> findByUserId(@Param("userId") Long userId);
}
