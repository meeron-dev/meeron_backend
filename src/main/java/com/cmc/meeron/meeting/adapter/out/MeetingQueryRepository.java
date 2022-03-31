package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.TodayMeetingsQueryDto;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MeetingQueryRepository implements MeetingQueryPort {

    private final MeetingJpaRepository meetingJpaRepository;
    private final AgendaJpaRepository agendaJpaRepository;
    private final MeetingQuerydslRepository meetingQuerydslRepository;

    @Override
    public List<TodayMeetingsQueryDto> findTodayMeetings(Long workspaceId, Long workspaceUserId) {
        return meetingQuerydslRepository.findTodayMeetingsQuery(workspaceId, workspaceUserId);
    }

    @Override
    public Optional<Meeting> findById(Long meetingId) {
        return meetingJpaRepository.findById(meetingId);
    }

    @Override
    public Optional<Agenda> findAgendaById(Long agendaId) {
        return agendaJpaRepository.findById(agendaId);
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
