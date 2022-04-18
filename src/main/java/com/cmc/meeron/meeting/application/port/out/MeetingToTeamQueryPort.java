package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.team.domain.Team;

import java.util.Optional;

public interface MeetingToTeamQueryPort {

    Optional<Team> findById(Long operationTeamId);
}
