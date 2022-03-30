package com.cmc.meeron.meeting.application.port.in.request;

public class TodayMeetingRequestDtoBuilder {

    public static TodayMeetingRequestDto build() {
        return TodayMeetingRequestDto.builder()
                .workspaceId(1L)
                .workspaceUserId(2L)
                .build();
    }
}
