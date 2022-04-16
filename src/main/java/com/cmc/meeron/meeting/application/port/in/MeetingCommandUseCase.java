package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.DeleteMeetingRequestDto;

public interface MeetingCommandUseCase {

    Long createMeeting(CreateMeetingRequestDto createMeetingRequestDto, AuthUser authUser);

    void deleteMeeting(DeleteMeetingRequestDto deleteMeetingRequestDto);
}
