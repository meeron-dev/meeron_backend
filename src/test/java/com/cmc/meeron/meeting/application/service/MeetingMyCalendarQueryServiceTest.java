package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingMyCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Collectors;

import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingMyCalendarQueryServiceTest {

    @Mock MeetingMyCalendarQueryPort meetingMyCalendarQueryPort;
    @Mock WorkspaceUserQueryPort workspaceUserQueryPort;
    @InjectMocks MeetingMyCalendarQueryService meetingMyCalendarQueryService;

    private final String TYPE = "WORKSPACE_USER";
    private WorkspaceUser workspaceUser;

    @BeforeEach
    void setUp() {
        workspaceUser = WORKSPACE_USER_1;
    }

    @DisplayName("'나의 미론' 회의 날짜 조회 - 성공")
    @Test
    void get_meeting_days_success() throws Exception {

        // given
        List<Integer> days = List.of(1, 10, 20);
        findMyWorkspaceUsersStub();
        when(meetingMyCalendarQueryPort.findMyMeetingDays(any(), any()))
                .thenReturn(days);
        YearMonth now = YearMonth.now();

        // when
        List<Integer> meetingDays = meetingMyCalendarQueryService.getMeetingDays(1L, now);

        // then
        verify(meetingMyCalendarQueryPort).findMyMeetingDays(List.of(WORKSPACE_USER_1.getId()), now);
    }

    private void findMyWorkspaceUsersStub() {
        when(workspaceUserQueryPort.findMyWorkspaceUsers(any())).thenReturn(List.of(WORKSPACE_USER_1));
    }

    @DisplayName("'나의 미론' 지정한 날짜의 회의 조회 - 성공")
    @Test
    void get_day_meetings_success() throws Exception {

        // given
        List<Meeting> meetings = createDayMeetingsContainsWorkspace();
        findMyWorkspaceUsersStub();
        when(meetingMyCalendarQueryPort.findMyDayMeetings(any(), any()))
                .thenReturn(meetings);

        // when
        List<DayMeetingResponseDto> responseDtos = meetingMyCalendarQueryService.getDayMeetings(1L, LocalDate.now());

        // then
        assertAll(
                () -> assertEquals(responseDtos.stream().map(DayMeetingResponseDto::getMeetingId).collect(Collectors.toList()),
                        meetings.stream().map(Meeting::getId).collect(Collectors.toList())),
                () -> assertEquals(responseDtos.stream().map(DayMeetingResponseDto::getWorkspaceId).collect(Collectors.toList()),
                        meetings.stream().map(meeting -> meeting.getWorkspace().getId()).collect(Collectors.toList()))
        );
    }

    private List<Meeting> createDayMeetingsContainsWorkspace() {
        LocalTime now = LocalTime.now();
        return List.of(
                Meeting.builder()
                        .id(1L)
                        .meetingInfo(MeetingInfo.builder().name("테스트미팅1").build())
                        .meetingTime(MeetingTime.builder()
                                .startTime(now)
                                .endTime(now.plusHours(2))
                                .build())
                        .workspace(Workspace.builder().id(3L).name("테스트워크스페이스3").build())
                        .build(),
                Meeting.builder()
                        .id(2L)
                        .meetingInfo(MeetingInfo.builder().name("테스트미팅2").build())
                        .meetingTime(MeetingTime.builder()
                                .startTime(now.plusHours(2))
                                .endTime(now.plusHours(4))
                                .build())
                        .workspace(Workspace.builder().id(4L).name("테스트워크스페이스4").build())
                        .build()
        );
    }

    @DisplayName("'나의 미론' 캘린더에서 년도별 회의 카운트 - 성공")
    @Test
    void get_year_meetings_count_success() throws Exception {

        // given
        List<YearMeetingsCountQueryDto> responseDtos = createYearMeetingsCountDto();
        findMyWorkspaceUsersStub();
        when(meetingMyCalendarQueryPort.findMyYearMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when
        List<YearMeetingsCountResponseDto> countDtos = meetingMyCalendarQueryService.getMeetingCountPerYear(1L);

        // then
        assertAll(
                () -> verify(meetingMyCalendarQueryPort).findMyYearMeetingsCount(List.of(WORKSPACE_USER_1.getId())),
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

    @DisplayName("'나의 미론' 캘린더에서 월별 회의 카운트 - 성공")
    @Test
    void get_month_meetings_count_success() throws Exception {

        // given
        List<MonthMeetingsCountQueryDto> responseDtos = createMonthMeetingsCountDto();
        findMyWorkspaceUsersStub();
        when(meetingMyCalendarQueryPort.findMyMonthMeetingsCount(any(), any()))
                .thenReturn(responseDtos);
        Year now = Year.now();

        // when
        List<MonthMeetingsCountResponseDto> countDtos = meetingMyCalendarQueryService.getMeetingCountPerMonth(1L, now);

        // then
        assertAll(
                () -> verify(meetingMyCalendarQueryPort).findMyMonthMeetingsCount(List.of(WORKSPACE_USER_1.getId()), now),
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
