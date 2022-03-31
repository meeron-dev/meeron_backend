package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAttendeesQueryDto;
import com.cmc.meeron.meeting.domain.Attendee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class AttendeeQueryRepository implements AttendeeQueryPort {

    private final AttendeeJpaRepository attendeeJpaRepository;
    private final AttendeeQuerydslRepository attendeeQuerydslRepository;

    @Override
    public List<Attendee> getWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId) {
        return attendeeJpaRepository.findWithWorkspaceUserByMeetingIdTeamId(meetingId, teamId);
    }

    @Override
    public List<AttendStatusCountQueryDto> countAttendStatusByMeetingIds(List<Long> meetingIds) {
        return attendeeQuerydslRepository.countAttendStatusByMeetingIds(meetingIds);
    }

    @Override
    public List<MeetingAttendeesQueryDto> getMeetingAttendees(Long meetingId) {
        return attendeeQuerydslRepository.getMeetingAttendees(meetingId);
    }
}
