package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingTeamCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingTeamCalendarQueryServiceTest {

    @Mock MeetingTeamCalendarQueryPort meetingTeamCalendarQueryPort;
    @InjectMocks MeetingTeamCalendarQueryService meetingTeamCalendarQueryService;

    private final String TYPE = "TEAM";

    @DisplayName("'팀' 회의 날짜 조회 - 성공")
    @Test
    void get_meeting_days_success() throws Exception {

        // given
        List<Integer> days = List.of(1, 10, 20);
        when(meetingTeamCalendarQueryPort.findTeamMeetingDays(any(), any()))
                .thenReturn(days);
        YearMonth now = YearMonth.now();
        Long teamId = 1L;

        // when
        List<Integer> meetingDays = meetingTeamCalendarQueryService.getMeetingDays(teamId, now);

        // then
        verify(meetingTeamCalendarQueryPort).findTeamMeetingDays(teamId, now);
    }

    @DisplayName("'팀' 지정한 날짜의 회의 조회 - 성공")
    @Test
    void get_day_meetings_success() throws Exception {

        // given
        List<Meeting> meetings = createDayMeetings();
        when(meetingTeamCalendarQueryPort.findTeamDayMeetings(any(), any()))
                .thenReturn(meetings);
        Long teamId = 1L;

        // when
        List<DayMeetingResponseDto> responseDtos = meetingTeamCalendarQueryService.getDayMeetings(teamId, LocalDate.now());

        // then
        DayMeetingResponseDto one = responseDtos.get(0);
        DayMeetingResponseDto two = responseDtos.get(1);
        assertAll(
                () -> assertNull(one.getWorkspaceId()),
                () -> assertNull(two.getWorkspaceId()),
                () -> assertEquals(meetings.get(0).getId(), one.getMeetingId()),
                () -> assertEquals(meetings.get(1).getId(), two.getMeetingId())
        );
    }

    private List<Meeting> createDayMeetings() {
        LocalTime now = LocalTime.now();
        return List.of(
                Meeting.builder()
                        .id(1L)
                        .meetingInfo(MeetingInfo.builder().name("테스트미팅1").build())
                        .meetingTime(MeetingTime.builder()
                                .startTime(now)
                                .endTime(now.plusHours(2))
                                .build())
                        .build(),
                Meeting.builder()
                        .id(2L)
                        .meetingInfo(MeetingInfo.builder().name("테스트미팅2").build())
                        .meetingTime(MeetingTime.builder()
                                .startTime(now.plusHours(2))
                                .endTime(now.plusHours(4))
                                .build())
                        .build()
        );
    }

    @DisplayName("'팀' 캘린더에서 년도별 회의 카운트 - 성공")
    @Test
    void get_year_meetings_count_success() throws Exception {

        // given
        List<YearMeetingsCountQueryDto> responseDtos = createYearMeetingsCountDto();
        when(meetingTeamCalendarQueryPort.findTeamYearMeetingsCount(any()))
                .thenReturn(responseDtos);
        Long teamId = 1L;

        // when
        List<YearMeetingsCountResponseDto> countDtos = meetingTeamCalendarQueryService.getMeetingCountPerYear(teamId);

        // then
        assertAll(
                () -> verify(meetingTeamCalendarQueryPort).findTeamYearMeetingsCount(teamId),
                () -> assertEquals(responseDtos.size(), countDtos.size()),
                () -> assertEquals(responseDtos.get(0).getYear(), countDtos.get(0).getYear()),
                () -> assertEquals(responseDtos.get(0).getCount(), countDtos.get(0).getCount())
        );
    }

    private List<YearMeetingsCountQueryDto> createYearMeetingsCountDto() {
        return List.of(
                YearMeetingsCountQueryDto.builder().year(2022).count(10L).build(),
                YearMeetingsCountQueryDto.builder().year(2021).count(2L).build()
        );
    }

    @DisplayName("'팀' 캘린더에서 월별 회의 카운트 - 성공")
    @Test
    void get_month_meetings_count_success() throws Exception {

        // given
        List<MonthMeetingsCountQueryDto> responseDtos = createMonthMeetingsCountDto();
        when(meetingTeamCalendarQueryPort.findTeamMonthMeetingsCount(any(), any()))
                .thenReturn(responseDtos);
        Long teamId = 1L;
        Year now = Year.now();

        // when
        List<MonthMeetingsCountResponseDto> countDtos = meetingTeamCalendarQueryService.getMeetingCountPerMonth(teamId, now);

        // then
        assertAll(
                () -> verify(meetingTeamCalendarQueryPort).findTeamMonthMeetingsCount(teamId, now),
                () -> assertEquals(12, countDtos.size())
        );
    }

    private List<MonthMeetingsCountQueryDto> createMonthMeetingsCountDto() {
        return List.of(
                MonthMeetingsCountQueryDto.builder().month(4).count(1L).build(),
                MonthMeetingsCountQueryDto.builder().month(7).count(2L).build(),
                MonthMeetingsCountQueryDto.builder().month(9).count(3L).build()
        );
    }
}
