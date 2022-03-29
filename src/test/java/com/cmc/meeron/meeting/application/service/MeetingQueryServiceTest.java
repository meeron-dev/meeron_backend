package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Attendee;
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

import static com.cmc.meeron.meeting.AttendeeFixture.ATTENDEE_1;
import static com.cmc.meeron.meeting.AttendeeFixture.ATTENDEE_2;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingQueryServiceTest {

    @Mock
    MeetingQueryPort meetingQueryPort;
    @InjectMocks
    MeetingQueryService meetingQueryService;

    @DisplayName("오늘 예정된 회의 가져오기 - 성공")
    @Test
    void today_expected_meeting_success() throws Exception {

        // given
        TodayMeetingRequestDto request = createTodayExpectedMeetingRequest();
        List<Meeting> response = getTodayMeetingResponse();
        when(meetingQueryPort.findTodayMeetings(any(), any()))
                .thenReturn(response);

        // when
        List<TodayMeetingResponseDto> result = meetingQueryService.getTodayMeetings(request);

        // then
        assertAll(
                () -> verify(meetingQueryPort).findTodayMeetings(request.getWorkspaceId(), request.getWorkspaceUserId()),
                () -> assertEquals(response.size(), result.size())
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

    private TodayMeetingRequestDto createTodayExpectedMeetingRequest() {
        return TodayMeetingRequestDto.builder()
                .workspaceId(1L)
                .workspaceUserId(2L)
                .build();
    }

    @DisplayName("회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_success() throws Exception {

        // given
        Attendee attendee1 = ATTENDEE_1;
        Attendee attendee2 = ATTENDEE_2;
        List<Attendee> attendees = List.of(attendee1, attendee2);
        when(meetingQueryPort.findWithWorkspaceUserByMeetingIdTeamId(any(), any()))
                .thenReturn(attendees);
        MeetingAttendeesRequestDto requestDto = MeetingAttendeesRequestDtoBuilder.build();

        // when
        MeetingAttendeesResponseDto responseDto = meetingQueryService.getMeetingAttendees(requestDto);

        // then
        assertAll(
                () -> verify(meetingQueryPort).findWithWorkspaceUserByMeetingIdTeamId(
                        requestDto.getMeetingId(), requestDto.getTeamId()),
                () -> assertEquals(1, responseDto.getUnknowns().size()),
                () -> assertEquals(1, responseDto.getAttends().size()),
                () -> assertEquals(0, responseDto.getAbsents().size())
        );
    }
}
