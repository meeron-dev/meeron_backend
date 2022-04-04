package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.request.*;

import java.util.List;

public interface MeetingCommandUseCase {

    Long createMeeting(CreateMeetingRequestDto createMeetingRequestDto, AuthUser authUser);

    void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto);

    List<Long> createAgendas(CreateAgendaRequestDto createAgendaRequestDtos);

    void changeAttendStatus(ChangeAttendStatusRequestDto changeAttendStatusRequestDto);

    void deleteMeeting(DeleteMeetingRequestDto deleteMeetingRequestDto);
}
