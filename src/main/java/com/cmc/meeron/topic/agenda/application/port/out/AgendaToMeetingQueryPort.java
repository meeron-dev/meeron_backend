package com.cmc.meeron.topic.agenda.application.port.out;

import com.cmc.meeron.meeting.domain.Meeting;

import java.util.Optional;

public interface AgendaToMeetingQueryPort {

    Optional<Meeting> findById(Long meetingId);
}
