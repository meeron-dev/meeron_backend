package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendStatusCountResponseDtoBuilder;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingQueryServiceTest {

    @Mock
    MeetingQueryPort meetingQueryPort;
    @Mock
    AttendeeQueryPort attendeeQueryPort;
    @InjectMocks
    MeetingQueryService meetingQueryService;

    @DisplayName("오늘 예정된 회의 가져오기 - 성공")
    @Test
    void today_expected_meeting_success() throws Exception {

        // given
        TodayMeetingRequestDto request = TodayMeetingRequestDtoBuilder.build();
        List<Meeting> response = getTodayMeetingResponse();
        when(meetingQueryPort.findTodayMeetings(any(), any()))
                .thenReturn(response);
        List<AttendStatusCountResponseDto> countResponseDtos = AttendStatusCountResponseDtoBuilder.buildList();
        when(attendeeQueryPort.countAttendStatusByMeetingIds(any()))
                .thenReturn(countResponseDtos);

        // when
        List<TodayMeetingResponseDto> result = meetingQueryService.getTodayMeetings(request);

        // then
        TodayMeetingResponseDto one = result.stream().filter(res -> res.getMeetingId().equals(1L)).findFirst().orElseThrow();
        TodayMeetingResponseDto two = result.stream().filter(res -> res.getMeetingId().equals(2L)).findFirst().orElseThrow();
        assertAll(
                () -> verify(meetingQueryPort).findTodayMeetings(request.getWorkspaceId(), request.getWorkspaceUserId()),
                () -> verify(attendeeQueryPort).countAttendStatusByMeetingIds(response
                        .stream()
                        .map(Meeting::getId)
                        .collect(Collectors.toList())),
                () -> assertEquals(response.size(), result.size()),
                () -> assertEquals(3, one.getAttends()),
                () -> assertEquals(2, one.getUnknowns()),
                () -> assertEquals(5, one.getAbsents()),
                () -> assertEquals(1, two.getAttends()),
                () -> assertEquals(1, two.getUnknowns())
        );
    }

    private List<Meeting> getTodayMeetingResponse() {
        return List.of(
                Meeting.builder()
                        .id(1L)
                        .workspace(Workspace.builder().id(1L).build())
                        .meetingInfo(MeetingInfo.builder()
                                .name("테스트 회의1")
                                .purpose("목적1")
                                .build())
                        .meetingTime(MeetingTime.builder()
                                .startDate(LocalDate.now())
                                .startTime(LocalTime.now().minusHours(3))
                                .endTime(LocalTime.now().minusHours(2))
                                .build())
                        .place("테스트 장소1")
                        .team(Team.builder().id(1L).name("테스트팀1").build())
                        .build(),
                Meeting.builder()
                        .id(2L)
                        .workspace(Workspace.builder().id(1L).build())
                        .meetingInfo(MeetingInfo.builder()
                                .name("테스트 회의2")
                                .purpose("목적2")
                                .build())
                        .meetingTime(MeetingTime.builder()
                                .startDate(LocalDate.now())
                                .startTime(LocalTime.now().plusHours(1))
                                .endTime(LocalTime.now().plusHours(2))
                                .build())
                        .place("테스트 장소2")
                        .team(Team.builder().id(2L).name("테스트팀2").build())
                        .build()
        );
    }
}
