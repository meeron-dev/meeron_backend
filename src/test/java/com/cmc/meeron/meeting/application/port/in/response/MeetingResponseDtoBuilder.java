package com.cmc.meeron.meeting.application.port.in.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MeetingResponseDtoBuilder {

    public static MeetingResponseDto build() {
        LocalTime time = LocalTime.now();
        return MeetingResponseDto.builder()
                .meetingId(1L)
                .meetingName("테스트회의")
                .meetingPurpose("테스트목적")
                .meetingDate(LocalDate.now())
                .startTime(time)
                .endTime(time.plusHours(2))
                .operationTeamId(1L)
                .operationTeamName("테스트팀")
                .admins(List.of(
                        MeetingResponseDto.MeetingAdminsResponseDto.builder()
                                .workspaceUserId(1L)
                                .nickname("테스트유저")
                                .build()
                ))
                .build();
    }
}
