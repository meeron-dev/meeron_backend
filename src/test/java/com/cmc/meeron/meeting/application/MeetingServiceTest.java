package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.DayMeetingsRequestDto;
import com.cmc.meeron.meeting.application.dto.request.MeetingDaysRequestDto;
import com.cmc.meeron.meeting.application.dto.request.MeetingSearchRequestDto;
import com.cmc.meeron.meeting.application.dto.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceAndTeamDayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceUserDayMeetingResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingRepository;
import com.cmc.meeron.meeting.domain.MeetingStatus;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock MeetingRepository meetingRepository;
    @InjectMocks MeetingService meetingService;

    @DisplayName("오늘 예정된 회의 가져오기 - 성공")
    @Test
    void today_expected_meeting_success() throws Exception {

        // given
        TodayMeetingRequestDto request = createTodayExpectedMeetingRequest();
        List<Meeting> response = getTodayMeetingResponse();
        when(meetingRepository.findTodayMeetings(any(), any()))
                .thenReturn(response);

        // when
        List<TodayMeetingResponseDto> result = meetingService.getTodayMeetings(request);

        // then
        assertAll(
                () -> verify(meetingRepository).findTodayMeetings(request.getWorkspaceId(), request.getWorkspaceUserId()),
                () -> assertEquals(response.size(), result.size())
        );
    }

    private List<Meeting> getTodayMeetingResponse() {
        return List.of(
                Meeting.builder()
                        .id(1L)
                        .workspace(Workspace.builder().id(1L).build())
                        .name("테스트 회의1")
                        .purpose("목적1")
                        .startDate(LocalDate.now())
                        .startTime(LocalTime.now().minusHours(3))
                        .endTime(LocalTime.now().minusHours(2))
                        .place("테스트 장소1")
                        .privateMeeting(false)
                        .meetingStatus(MeetingStatus.END)
                        .team(Team.builder().id(1L).name("테스트팀1").build())
                        .build(),
                Meeting.builder()
                        .id(2L)
                        .workspace(Workspace.builder().id(1L).build())
                        .name("테스트 회의2")
                        .purpose("목적2")
                        .startDate(LocalDate.now())
                        .startTime(LocalTime.now().plusHours(1))
                        .endTime(LocalTime.now().plusHours(2))
                        .place("테스트 장소2")
                        .privateMeeting(false)
                        .meetingStatus(MeetingStatus.EXPECT)
                        .team(Team.builder().id(2L).name("테스트팀2").build())
                        .build()
        );
    }

    private TodayMeetingRequestDto createTodayExpectedMeetingRequest() {
        return TodayMeetingRequestDto.builder()
                .workspaceId(1L)
                .workspaceUserId(2L)
                .build();
    }

    private List<Integer> createResponseDays() {
        return List.of(1, 10, 12, 14, 20, 30);
    }

    @DisplayName("회의가 있는 날짜 조회 - 성공")
    @ParameterizedTest
    @MethodSource("createMeetingDaysRequestDtos")
    void get_meeting_days_success(MeetingDaysRequestDto requestDto) throws Exception {

        // given
        List<Integer> days = List.of(1, 10, 20);
        when(meetingRepository.findMeetingDays(any(), any(), any()))
                .thenReturn(days);

        // when
        List<Integer> meetingDays = meetingService.getMeetingDays(requestDto);

        // then
        assertAll(
                () -> verify(meetingRepository).findMeetingDays(requestDto.getSearchType(), requestDto.getSearchIds(), requestDto.getYearMonth()),
                () -> assertEquals(days, meetingDays)
        );
    }

    private static Stream<Arguments> createMeetingDaysRequestDtos() {
        MeetingDaysRequestDto workspaceUserDays = MeetingDaysRequestDto.builder()
                .yearMonth(YearMonth.now())
                .meetingSearch(MeetingSearchRequestDto.builder()
                        .searchType("WORKSPACEUSER")
                        .searchIds(List.of(1L, 2L))
                        .build())
                .build();
        MeetingDaysRequestDto workspaceDays = MeetingDaysRequestDto.builder()
                .yearMonth(YearMonth.now())
                .meetingSearch(MeetingSearchRequestDto.builder()
                        .searchType("WORKSPACE")
                        .searchIds(List.of(1L))
                        .build())
                .build();
        MeetingDaysRequestDto teamDays = MeetingDaysRequestDto.builder()
                .yearMonth(YearMonth.now())
                .meetingSearch(MeetingSearchRequestDto.builder()
                        .searchType("TEAM")
                        .searchIds(List.of(1L))
                        .build())
                .build();
        return Stream.of(
                Arguments.of(workspaceUserDays),
                Arguments.of(workspaceDays),
                Arguments.of(teamDays)
        );
    }

    @DisplayName("지정한 날짜의 회의 조회 - 성공 / 워크스페이스, 팀의 경우")
    @ParameterizedTest
    @MethodSource("createDayMeetingsRequestDtos")
    void get_day_meetings_success_workspace_team(DayMeetingsRequestDto requestDto) throws Exception {

        // given
        List<Meeting> meetings = createDayMeetings();
        when(meetingRepository.findDayMeetings(any(), any(), any()))
                .thenReturn(meetings);

        // when
        List<WorkspaceAndTeamDayMeetingResponseDto> responseDtos = meetingService.getWorkspaceAndTeamDayMeetings(requestDto);

        // then
        assertAll(
                () -> verify(meetingRepository).findDayMeetings(requestDto.getSearchType(), requestDto.getSearchIds(), requestDto.getLocalDate()),
                () -> assertEquals(responseDtos.stream().map(WorkspaceAndTeamDayMeetingResponseDto::getMeetingId).collect(Collectors.toList()),
                        meetings.stream().map(Meeting::getId).collect(Collectors.toList()))
        );
    }

    private static Stream<Arguments> createDayMeetingsRequestDtos() {
        return Stream.of(
                Arguments.of(DayMeetingsRequestDto.builder().meetingSearch(
                        MeetingSearchRequestDto.builder()
                                .searchType("WORKSPACE")
                                .searchIds(List.of(1L))
                        .build())
                        .localDate(LocalDate.now()).build()),
                Arguments.of(DayMeetingsRequestDto.builder().meetingSearch(
                        MeetingSearchRequestDto.builder()
                                .searchType("TEAM")
                                .searchIds(List.of(1L))
                        .build())
                        .localDate(LocalDate.now()).build())
        );
    }

    private List<Meeting> createDayMeetings() {
        LocalTime now = LocalTime.now();
        return List.of(
                Meeting.builder().id(1L).name("테스트미팅1").startTime(now).endTime(now.plusHours(2)).build(),
                Meeting.builder().id(2L).name("테스트미팅2").startTime(now.plusHours(2)).endTime(now.plusHours(4)).build()
        );
    }

    @DisplayName("지정한 날짜의 회의 조회 - 성공 / 나의 미론(워크스페이스 유저)의 경우")
    @Test
    void get_day_meetings_success_workspace_user() throws Exception {

        // given
        DayMeetingsRequestDto requestDto = createDayMeetingsRequestDto();
        List<Meeting> meetings = createDayMeetingsContainsWorkspace();
        when(meetingRepository.findDayMeetings(any(), any(), any()))
                .thenReturn(meetings);

        // when
        List<WorkspaceUserDayMeetingResponseDto> responseDtos = meetingService.getWorkspaceUserDayMeetings(requestDto);

        // then
        assertAll(
                () -> verify(meetingRepository).findDayMeetings(requestDto.getSearchType(), requestDto.getSearchIds(), requestDto.getLocalDate()),
                () -> assertEquals(responseDtos.stream().map(WorkspaceUserDayMeetingResponseDto::getMeetingId).collect(Collectors.toList()),
                        meetings.stream().map(Meeting::getId).collect(Collectors.toList())),
                () -> assertEquals(responseDtos.stream().map(WorkspaceUserDayMeetingResponseDto::getWorkspaceId).collect(Collectors.toList()),
                        meetings.stream().map(meeting -> meeting.getWorkspace().getId()).collect(Collectors.toList()))
        );
    }

    private DayMeetingsRequestDto createDayMeetingsRequestDto() {
        return DayMeetingsRequestDto.builder().meetingSearch(
                MeetingSearchRequestDto.builder()
                        .searchType("WORKSPACE_USER")
                        .searchIds(List.of(1L))
                        .build())
                .localDate(LocalDate.now()).build();
    }

    private List<Meeting> createDayMeetingsContainsWorkspace() {
        LocalTime now = LocalTime.now();
        return List.of(
                Meeting.builder().id(1L).name("테스트미팅1").startTime(now).endTime(now.plusHours(2))
                        .workspace(Workspace.builder().id(3L).name("테스트워크스페이스3").build())
                        .build(),
                Meeting.builder().id(2L).name("테스트미팅2").startTime(now.plusHours(2)).endTime(now.plusHours(4))
                        .workspace(Workspace.builder().id(4L).name("테스트워크스페이스4").build())
                        .build()
        );
    }
}
