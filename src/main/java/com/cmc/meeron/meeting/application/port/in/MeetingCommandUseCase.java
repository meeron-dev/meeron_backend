package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;

public interface MeetingCommandUseCase {

    Long creteMeeting(CreateMeetingRequestDto createMeetingRequestDto);
}
