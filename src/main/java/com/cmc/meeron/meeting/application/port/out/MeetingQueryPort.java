package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingQueryPort {

    List<Meeting> findTodayMeetingsWithOperationTeam(Long workspaceId, Long workspaceUserId);

    Optional<Meeting> findById(Long meetingId);

    Optional<Agenda> findAgendaById(Long agendaId);

    Optional<Meeting> findWithAttendeesById(Long meetingId);

    Optional<MeetingAndAdminsQueryDto> findWithTeamAndAdminsById(Long meetingId);
}
