package com.cmc.meeron.team.adapter.out;

import com.cmc.meeron.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface TeamJpaRepository extends JpaRepository<Team, Long> {

    Long countByWorkspaceId(Long workspaceId);

    @Query("select t from Team t join Meeting m on t.id = m.team.id where m.id = :meetingId")
    Optional<Team> findByMeetingId(@Param("meetingId") Long meetingId);
}
