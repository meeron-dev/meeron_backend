package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToMeetingQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MeetingQueryRepository implements MeetingQueryPort, AgendaToMeetingQueryPort {

    private final MeetingJpaRepository meetingJpaRepository;
    private final MeetingQuerydslRepository meetingQuerydslRepository;

    @Override
    public List<Meeting> findTodayMeetingsWithOperationTeam(Long workspaceId, Long workspaceUserId) {
        return meetingJpaRepository.findTodayMeetingsWithOperationTeam(workspaceId, workspaceUserId, LocalDate.now());
    }

    @Override
    public Optional<Meeting> findById(Long meetingId) {
        return meetingJpaRepository.findById(meetingId);
    }

    @Override
    public Optional<Meeting> findWithAttendeesById(Long meetingId) {
        return meetingJpaRepository.findWithAttendeesById(meetingId);
    }

    @Override
    public Optional<MeetingAndAdminsQueryDto> findWithTeamAndAdminsById(Long meetingId) {
        return meetingQuerydslRepository.findWithTeamAndAdminsById(meetingId);
    }
}
