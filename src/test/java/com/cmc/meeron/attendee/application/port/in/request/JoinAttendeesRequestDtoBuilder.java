package com.cmc.meeron.attendee.application.port.in.request;

import java.util.List;

public class JoinAttendeesRequestDtoBuilder {

    public static JoinAttendeesRequestDto build() {
        return JoinAttendeesRequestDto.builder()
                .meetingId(1L)
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }
}
