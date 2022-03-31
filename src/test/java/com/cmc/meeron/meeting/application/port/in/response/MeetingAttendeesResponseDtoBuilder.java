package com.cmc.meeron.meeting.application.port.in.response;

import java.util.List;

public class MeetingAttendeesResponseDtoBuilder {

    public static List<MeetingAttendeesResponseDto> build() {
        return List.of(
                MeetingAttendeesResponseDto.builder()
                        .teamId(1L)
                        .teamName("1팀")
                        .attends(0)
                        .absents(2)
                        .unknowns(1)
                        .build(),
                MeetingAttendeesResponseDto.builder()
                        .teamId(2L)
                        .teamName("2팀")
                        .attends(1)
                        .absents(3)
                        .unknowns(0)
                        .build());
    }
}
