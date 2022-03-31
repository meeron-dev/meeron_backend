package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAttendeesQueryDto;
import com.cmc.meeron.meeting.domain.Attendee;

import java.util.List;

public interface AttendeeQueryPort {

    List<Attendee> getWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId);

    List<AttendStatusCountQueryDto> countAttendStatusByMeetingIds(List<Long> meetingIds);

    List<MeetingAttendeesQueryDto> getMeetingAttendees(Long meetingId);
}
