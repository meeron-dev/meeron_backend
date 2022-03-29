package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Attendee;
import com.cmc.meeron.meeting.domain.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingQueryPort {

    List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId);

    Optional<Meeting> findById(Long meetingId);

    Optional<Agenda> findAgendaById(Long agendaId);

    Optional<Meeting> findWithAttendeesById(Long meetingId);

    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(Long meetingId, Long teamId);
}
