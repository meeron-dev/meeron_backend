package com.cmc.meeron.meeting.application.port.in.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TodayMeetingResponseDtoBuilder {

    public static List<TodayMeetingResponseDto> buildList() {
        LocalTime now = LocalTime.now();
        return List.of(
                TodayMeetingResponseDto.builder()
                        .meetingId(1L)
                        .meetingName("테스트 회의 1")
                        .meetingDate(LocalDate.now())
                        .startTime(LocalTime.of(now.plusHours(2).getHour(), 0, 0))
                        .endTime(LocalTime.of(now.plusHours(3).getHour(), 0, 0))
                        .operationTeamId(1L)
                        .operationTeamName("테스트 팀 1")
                        .attends(3)
                        .absents(2)
                        .unknowns(1)
                        .build(),
                TodayMeetingResponseDto.builder()
                        .meetingId(2L)
                        .meetingName("테스트 회의 2")
                        .meetingDate(LocalDate.now())
                        .startTime(LocalTime.of(now.minusHours(3).getHour(), 0, 0))
                        .endTime(LocalTime.of(now.minusHours(2).getHour(), 0, 0))
                        .operationTeamId(2L)
                        .operationTeamName("테스트 팀 2")
                        .attends(1)
                        .absents(2)
                        .unknowns(3)
                        .build()
        );
    }
}
