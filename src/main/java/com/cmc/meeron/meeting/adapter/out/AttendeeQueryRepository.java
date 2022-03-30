package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.domain.Attendee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class AttendeeQueryRepository implements AttendeeQueryPort {

    private final AttendeeJpaRepository attendeeJpaRepository;

    @Override
    public List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId) {
        return attendeeJpaRepository.findWithWorkspaceUserByMeetingIdTeamId(meetingId, teamId);
    }
}
