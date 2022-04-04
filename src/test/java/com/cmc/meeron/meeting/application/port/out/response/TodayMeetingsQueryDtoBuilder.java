package com.cmc.meeron.meeting.application.port.out.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TodayMeetingsQueryDtoBuilder {

    public static List<TodayMeetingsQueryDto> buildList() {
        return List.of(
                TodayMeetingsQueryDto.builder()
                        .meetingId(1L)
                        .meetingName("테스트미팅1")
                        .meetingPurpose("회의성격1")
                        .meetingDate(LocalDate.now())
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(2))
                        .operationTeamId(1L)
                        .operationTeamName("1번팀")
                        .build(),
                TodayMeetingsQueryDto.builder()
                        .meetingId(2L)
                        .meetingName("테스트미팅2")
                        .meetingPurpose("회의성격2")
                        .meetingDate(LocalDate.now())
                        .startTime(LocalTime.now().plusHours(3))
                        .endTime(LocalTime.now().plusHours(5))
                        .operationTeamId(2L)
                        .operationTeamName("2번팀")
                        .build());
    }
}
