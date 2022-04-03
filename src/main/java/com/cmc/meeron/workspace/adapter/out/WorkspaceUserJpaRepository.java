package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.domain.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface WorkspaceUserJpaRepository extends JpaRepository<WorkspaceUser, Long> {

    List<WorkspaceUser> findByUserId(Long userId);

    @Query(
            "select wu" +
            " from WorkspaceUser wu" +
            " where wu.user.id = :userId" +
            " and wu.workspace.id = :workspaceId")
    Optional<WorkspaceUser> findByUserWorkspaceId(@Param("userId") Long userId,
                                                  @Param("workspaceId") Long workspaceId);

    List<WorkspaceUser> findByTeamId(Long teamId);

    List<WorkspaceUser> findByWorkspaceIdAndTeamIsNull(Long workspaceId);

    @Query(
            "select wu" +
                    " from WorkspaceUser wu" +
                    " join fetch wu.workspace" +
                    " where wu.user.id = :userId")
    List<WorkspaceUser> findWithWorkspaceByUserId(@Param("userId") Long userId);

    List<WorkspaceUser> findByWorkspaceId(Long workspaceId);
}
