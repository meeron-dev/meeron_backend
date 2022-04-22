package com.cmc.meeron.attendee.application.port.in;

import com.cmc.meeron.attendee.application.port.in.request.ChangeAttendStatusRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.JoinAttendeesRequestDto;

public interface AttendeeCommandUseCase {

    void joinAttendees(JoinAttendeesRequestDto toRequestDto);

    void changeAttendStatus(ChangeAttendStatusRequestDto toRequestDto);

    void changeAttendStatusV2(ChangeAttendStatusRequestDto changeAttendStatusRequestDto);
}
