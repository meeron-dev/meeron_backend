package com.cmc.meeron.meeting.application.port.out;

public interface AgendaQueryPort {

    boolean existsByMeetingId(Long meetingId);
}
