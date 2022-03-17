package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.security.SecurityUtil;
import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingWorkspaceCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.WorkspaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cmc.meeron.user.UserFixture.USER;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingWorkspaceCalendarQueryServiceTest {

    @Mock MeetingWorkspaceCalendarQueryPort meetingWorkspaceCalendarQueryPort;
    @Mock UserQueryPort userQueryPort;
    @InjectMocks MeetingWorkspaceCalendarQueryService meetingWorkspaceCalendarQueryService;

    private WorkspaceUser workspaceUser;
    private final String TYPE = "WORKSPACE";

    @BeforeEach
    void setUp() {
        workspaceUser = WORKSPACE_USER_1;
    }

    @DisplayName("'워크스페이스' 회의 날짜 조회 - 성공")
    @Test
    void get_meeting_days_success() throws Exception {

        // given
        List<Integer> days = List.of(1, 10, 20);
        YearMonth now = YearMonth.now();
        try (MockedStatic<SecurityUtil> securityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getUserId).thenReturn(USER.getId());
            when(userQueryPort.findByUserWorkspaceId(any(), any()))
                    .thenReturn(Optional.of(WORKSPACE_USER_1));
            when(meetingWorkspaceCalendarQueryPort.findWorkspaceMeetingDays(any(), any()))
                    .thenReturn(days);

            // when
            List<Integer> meetingDays = meetingWorkspaceCalendarQueryService.getMeetingDays(1L, now);
        }

        // then
        verify(meetingWorkspaceCalendarQueryPort).findWorkspaceMeetingDays(WORKSPACE_USER_1.getId(), now);
    }

    @DisplayName("'워크스페이스' 지정한 날짜의 회의 조회 - 성공")
    @Test
    void get_day_meetings_success() throws Exception {

        // given
        List<Meeting> meetings = createDayMeetings();
        LocalDate now = LocalDate.now();

        try (MockedStatic<SecurityUtil> securityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getUserId).thenReturn(USER.getId());
            when(userQueryPort.findByUserWorkspaceId(any(), any()))
                    .thenReturn(Optional.of(WORKSPACE_USER_1));
            when(meetingWorkspaceCalendarQueryPort.findWorkspaceDayMeetings(any(), any()))
                    .thenReturn(meetings);
            // when
            List<DayMeetingResponseDto> responseDtos = meetingWorkspaceCalendarQueryService.getDayMeetings(1L, now);

            // then
            assertAll(
                    () -> verify(meetingWorkspaceCalendarQueryPort).findWorkspaceDayMeetings(WORKSPACE_USER_1.getId(), now),
                    () -> assertEquals(responseDtos.stream().map(DayMeetingResponseDto::getWorkspaceId).collect(Collectors.toList()),
                            List.of(0L, 0L))
            );
        }
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
                        .workspace(WORKSPACE_1)
                        .build(),
                Meeting.builder()
                        .id(2L)
                        .meetingInfo(MeetingInfo.builder().name("테스트미팅2").build())
                        .meetingTime(MeetingTime.builder()
                                .startTime(now.plusHours(2))
                                .endTime(now.plusHours(4))
                                .build())
                        .workspace(WORKSPACE_1)
                        .build()
        );
    }

    @DisplayName("'워크스페이스' 캘린더에서 년도별 회의 카운트 - 성공")
    @Test
    void get_year_meetings_count_success() throws Exception {

        // given
        List<YearMeetingsCountQueryDto> responseDtos = createYearMeetingsCountDto();
        Long workspaceId = 1L;
        try (MockedStatic<SecurityUtil> securityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getUserId).thenReturn(USER.getId());
            when(userQueryPort.findByUserWorkspaceId(any(), any()))
                    .thenReturn(Optional.of(WORKSPACE_USER_1));
            when(meetingWorkspaceCalendarQueryPort.findWorkspaceYearMeetingsCount(any()))
                    .thenReturn(responseDtos);
            // when
            List<YearMeetingsCountResponseDto> countDtos = meetingWorkspaceCalendarQueryService.getMeetingCountPerYear(workspaceId);

            // then
            assertAll(
                    () -> verify(meetingWorkspaceCalendarQueryPort).findWorkspaceYearMeetingsCount(WORKSPACE_USER_1.getId()),
                    () -> assertEquals(responseDtos.size(), countDtos.size()),
                    () -> assertEquals(responseDtos.get(0).getYear(), countDtos.get(0).getYear()),
                    () -> assertEquals(responseDtos.get(0).getCount(), countDtos.get(0).getCount())
            );
        }
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
        Long workspaceId = 1L;
        Year now = Year.now();
        try (MockedStatic<SecurityUtil> securityUtil = Mockito.mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getUserId).thenReturn(USER.getId());
            when(userQueryPort.findByUserWorkspaceId(any(), any()))
                    .thenReturn(Optional.of(WORKSPACE_USER_1));
            when(meetingWorkspaceCalendarQueryPort.findWorkspaceMonthMeetingsCount(any(), any()))
                    .thenReturn(responseDtos);

            // when
            List<MonthMeetingsCountResponseDto> countDtos = meetingWorkspaceCalendarQueryService.getMeetingCountPerMonth(workspaceId, now);

            // then
            assertAll(
                    () -> verify(meetingWorkspaceCalendarQueryPort).findWorkspaceMonthMeetingsCount(WORKSPACE_USER_1.getId(), now),
                    () -> assertEquals(12, countDtos.size())
            );
        }
    }

    private List<MonthMeetingsCountQueryDto> createMonthMeetingsCountDto() {
        return List.of(
                MonthMeetingsCountQueryDto.builder().month(4).count(1L).build(),
                MonthMeetingsCountQueryDto.builder().month(7).count(2L).build(),
                MonthMeetingsCountQueryDto.builder().month(9).count(3L).build()
        );
    }
}
