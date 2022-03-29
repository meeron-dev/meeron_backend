package com.cmc.meeron.meeting.application.port.in.request;

public class MeetingAttendeesRequestDtoBuilder {

    public static MeetingAttendeesRequestDto build() {
        return MeetingAttendeesRequestDto.builder()
                .meetingId(1L)
                .teamId(1L)
                .build();
    }
}
