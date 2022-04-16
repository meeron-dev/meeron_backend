package com.cmc.meeron.attendee.application.port.out;

import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesQueryDto;

import java.util.List;

public interface AttendeeQueryPort {

    List<MeetingAttendeesQueryDto> findMeetingAttendees(Long meetingId);

    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId);
}
