package com.cmc.meeron.team.adapter.out;

import com.cmc.meeron.meeting.application.port.out.MeetingToTeamQueryPort;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TeamQueryRepository implements TeamQueryPort, MeetingToTeamQueryPort {

    private final TeamJpaRepository teamJpaRepository;

    @Override
    public List<Team> findByWorkspaceId(Long workspaceId) {
        return teamJpaRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return teamJpaRepository.findById(teamId);
    }

    @Override
    public long countByWorkspaceId(Long workspaceId) {
        return teamJpaRepository.countByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<Team> findByMeetingId(Long meetingId) {
        return teamJpaRepository.findByMeetingId(meetingId);
    }
}
