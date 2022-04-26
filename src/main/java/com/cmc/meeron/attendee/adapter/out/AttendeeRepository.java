package com.cmc.meeron.attendee.adapter.out;

import com.cmc.meeron.attendee.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingToAttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.attendee.domain.Attendee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class AttendeeRepository implements AttendeeQueryPort, MeetingToAttendeeQueryPort {

    private final AttendeeJpaRepository attendeeJpaRepository;
    private final AttendeeQuerydslRepository attendeeQuerydslRepository;

    @Override
    public List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId) {
        return attendeeJpaRepository.findWithWorkspaceUserByMeetingIdTeamId(meetingId, teamId);
    }

    @Override
    public Optional<Attendee> findById(Long attendeeId) {
        return attendeeJpaRepository.findById(attendeeId);
    }

    @Override
    public List<AttendStatusCountQueryDto> countAttendStatusByMeetingIds(List<Long> meetingIds) {
        return attendeeQuerydslRepository.countAttendStatusByMeetingIds(meetingIds);
    }

    @Override
    public List<MeetingAttendeesCountsByTeamQueryDto> countsMeetingAttendeesByTeam(Long meetingId) {
        return attendeeQuerydslRepository.getMeetingAttendees(meetingId);
    }

    @Override
    public Optional<Attendee> findByMeetingIdAndWorkspaceUserId(Long meetingId, Long workspaceUserId) {
        return attendeeJpaRepository.findByMeetingIdAndWorkspaceUserId(meetingId, workspaceUserId);
    }

    @Override
    public List<Attendee> findMeetingAdminsWithWorkspaceUserByMeetingIds(List<Long> meetingIds) {
        return attendeeJpaRepository.findMeetingAdminsByMeetingIds(meetingIds);
    }

    @Override
    public List<Attendee> findMeetingAdminsWithWorkspaceUserByMeetingId(Long meetingId) {
        return attendeeJpaRepository.findMeetingAdminsWithWorkspaceUserByMeetingId(meetingId);
    }

    @Override
    public Optional<Attendee> findWithWorkspaceUserByMeetingIdWorkspaceUserId(Long meetingId, Long workspaceUserId) {
        return attendeeJpaRepository.findWithWorkspaceUserByMeetingIdWorkspaceUserId(meetingId, workspaceUserId);
    }
}
