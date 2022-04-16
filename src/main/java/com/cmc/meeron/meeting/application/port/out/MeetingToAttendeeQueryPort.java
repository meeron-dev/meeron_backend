package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;

import java.util.List;
import java.util.Optional;

public interface MeetingToAttendeeQueryPort {

    List<AttendStatusCountQueryDto> countAttendStatusByMeetingIds(List<Long> meetingIds);

    Optional<Attendee> findByMeetingIdAndWorkspaceUserId(Long meetingId, Long workspaceUserId);

    List<Attendee> findMeetingAdminsWithWorkspaceUserByMeetingIds(List<Long> meetingIds);
}
