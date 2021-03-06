package com.cmc.meeron.team.application.port.out;

import com.cmc.meeron.team.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamQueryPort {

    List<Team> findByWorkspaceId(Long workspaceId);

    Optional<Team> findById(Long teamId);

    long countByWorkspaceId(Long workspaceId);

    Optional<Team> findByMeetingId(Long meetingId);
}
