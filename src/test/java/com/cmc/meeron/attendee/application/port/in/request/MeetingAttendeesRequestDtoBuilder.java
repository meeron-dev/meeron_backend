package com.cmc.meeron.attendee.application.port.in.request;

public class MeetingAttendeesRequestDtoBuilder {

    public static MeetingTeamAttendeesRequestDto build() {
        return MeetingTeamAttendeesRequestDto.builder()
                .meetingId(1L)
                .teamId(1L)
                .build();
    }
}
