package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAttendeesQueryDto;
import com.cmc.meeron.attendee.domain.Attendee;

import java.util.List;
import java.util.Optional;

public interface AttendeeQueryPort {

    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId);

    List<AttendStatusCountQueryDto> countAttendStatusByMeetingIds(List<Long> meetingIds);

    List<MeetingAttendeesQueryDto> findMeetingAttendees(Long meetingId);

    Optional<Attendee> findByMeetingIdAndWorkspaceUserId(Long meetingId, Long workspaceUserId);

    List<Attendee> findMeetingAdminsWithWorkspaceUserByMeetingIds(List<Long> meetingIds);
}
