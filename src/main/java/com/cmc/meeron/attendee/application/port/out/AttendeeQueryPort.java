package com.cmc.meeron.attendee.application.port.out;

import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.attendee.domain.Attendee;

import java.util.List;
import java.util.Optional;

public interface AttendeeQueryPort {

    List<MeetingAttendeesCountsByTeamQueryDto> countsMeetingAttendeesByTeam(Long meetingId);

    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId);

    Optional<Attendee> findById(Long attendeeId);

    List<Attendee> findMeetingAdminsWithWorkspaceUserByMeetingId(Long meetingId);

    Optional<Attendee> findWithWorkspaceUserByMeetingIdWorkspaceUserId(Long meetingId, Long workspaceUserId);
}
