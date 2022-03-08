package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.response.CreateAgendaResponseDto;
import com.cmc.meeron.meeting.application.port.in.request.CreateAgendaRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.JoinAttendeesRequestDto;

import java.util.List;

public interface MeetingCommandUseCase {

    Long createMeeting(CreateMeetingRequestDto createMeetingRequestDto);

    void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto);

    List<CreateAgendaResponseDto> createAgendas(CreateAgendaRequestDto createAgendaRequestDtos);
}
