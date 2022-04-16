package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.topic.agenda.domain.Agenda;

import java.util.List;

public interface MeetingToAgendaQueryPort {

    List<Agenda> findByMeetingIds(List<Long> meetingIds);
}
