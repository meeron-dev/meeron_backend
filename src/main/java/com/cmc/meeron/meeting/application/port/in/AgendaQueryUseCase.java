package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;

public interface AgendaQueryUseCase {

    AgendaCountResponseDto getAgendaCountsByMeetingId(Long meetingId);
}
