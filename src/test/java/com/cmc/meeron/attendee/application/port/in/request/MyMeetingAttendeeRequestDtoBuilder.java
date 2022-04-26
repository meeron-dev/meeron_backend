package com.cmc.meeron.attendee.application.port.in.request;

public class MyMeetingAttendeeRequestDtoBuilder {

    public static MeetingAttendeeRequestDto build() {
        return MeetingAttendeeRequestDto.builder()
                .meetingId(1L)
                .workspaceUserId(2L)
                .build();
    }
}
