package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.domain.Attendee;

import java.util.List;

public interface AttendeeQueryPort {

    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId);
}
