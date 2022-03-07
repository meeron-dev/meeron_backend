package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.JoinAttendeesRequestDto;

public interface MeetingCommandUseCase {

    Long creteMeeting(CreateMeetingRequestDto createMeetingRequestDto);

    void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto);
}
