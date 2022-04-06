package com.cmc.meeron.meeting.application.port.in.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TodayMeetingResponseDtoBuilder {

    public static List<TodayMeetingResponseDto> buildList() {
        LocalTime nowTime = LocalTime.now();
        return List.of(
                TodayMeetingResponseDto.builder()
                        .meetingResponseDto(TodayMeetingResponseDto.ImproveMeetingResponseDto.builder()
                                .meetingId(1L)
                                .startDate(LocalDate.now())
                                .startTime(nowTime)
                                .endTime(nowTime.plusHours(1))
                                .name("테스트 회의1")
                                .purpose("테스트 회의성격1")
                                .place("테스트 장소")
                                .build())
                        .teamResponseDto(TodayMeetingResponseDto.ImproveTeamResponseDto.builder()
                                .teamId(1L)
                                .teamName("테스트팀1")
                                .build())
                        .agendaResponseDtos(List.of(
                                TodayMeetingResponseDto.ImproveAgendaResponseDto.builder()
                                        .agendaId(1L)
                                        .agendaOrder(1)
                                        .agendaName("테스트아젠다1")
                                        .agendaResult("아젠다결과1")
                                        .build(),
                                TodayMeetingResponseDto.ImproveAgendaResponseDto.builder()
                                        .agendaId(2L)
                                        .agendaOrder(2)
                                        .agendaName("테스트아젠다2")
                                        .agendaResult("아젠다결과2")
                                        .build()
                        ))
                        .adminResponseDto(List.of(
                                TodayMeetingResponseDto.ImproveWorkspaceUserResponseDto.builder()
                                        .workspaceUserId(1L)
                                        .isWorkspaceAdmin(true)
                                        .nickname("1번유저")
                                        .position("개발자")
                                        .profileImageUrl("https://test.com/123123")
                                        .email("test1@test.com")
                                        .phone("010-1234-1234")
                                        .build(),
                                TodayMeetingResponseDto.ImproveWorkspaceUserResponseDto.builder()
                                        .workspaceUserId(2L)
                                        .isWorkspaceAdmin(false)
                                        .nickname("2번유저")
                                        .position("개발자")
                                        .profileImageUrl("https://test.com/1243123")
                                        .email("test2@test.com")
                                        .phone("010-2234-2234")
                                        .build()
                        ))
                        .attendCountResponseDto(TodayMeetingResponseDto.ImproveAttendCountResponseDto.builder()
                                .absent(1)
                                .attend(2)
                                .unknown(3)
                                .build())
                        .build()
        );
    }
}
