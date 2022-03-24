package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.request.ChangeAttendStatusRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.CreateAgendaRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.JoinAttendeesRequestDto;

import java.util.List;

public interface MeetingCommandUseCase {

    Long createMeeting(CreateMeetingRequestDto createMeetingRequestDto, AuthUser authUser);

    void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto);

    List<Long> createAgendas(CreateAgendaRequestDto createAgendaRequestDtos);

    void changeAttendStatus(ChangeAttendStatusRequestDto changeAttendStatusRequestDto);
}
