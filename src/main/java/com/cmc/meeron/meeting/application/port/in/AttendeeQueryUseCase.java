package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;

public interface AttendeeQueryUseCase {

    MeetingAttendeesResponseDto getMeetingAttendees(MeetingAttendeesRequestDto meetingAttendeesRequestDto);
}
