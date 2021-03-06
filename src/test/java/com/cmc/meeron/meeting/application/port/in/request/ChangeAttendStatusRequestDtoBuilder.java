package com.cmc.meeron.meeting.application.port.in.request;

import com.cmc.meeron.attendee.application.port.in.request.ChangeAttendStatusRequestDto;

public class ChangeAttendStatusRequestDtoBuilder {

    public static ChangeAttendStatusRequestDto buildFailRequest() {
        return ChangeAttendStatusRequestDto.builder()
                .meetingId(1L)
                .workspaceUserId(3140955187L)
                .status("ATTEND")
                .build();
    }

    public static ChangeAttendStatusRequestDto build() {
        return ChangeAttendStatusRequestDto.builder()
                .meetingId(1L)
                .workspaceUserId(59L)
                .status("ATTEND")
                .build();
    }
}
