package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.MeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountResponseDtoBuilder;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDtoBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.meeting.AgendaFixture.AGENDA1;
import static com.cmc.meeron.meeting.AgendaFixture.AGENDA2;
import static com.cmc.meeron.meeting.AttendeeFixture.ADMIN_ATTENDEE;
import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingQueryServiceTest {

    @Mock
    MeetingQueryPort meetingQueryPort;
    @Mock
    AttendeeQueryPort attendeeQueryPort;
    @Mock
    AgendaQueryPort agendaQueryPort;
    @InjectMocks
    MeetingQueryService meetingQueryService;

    @DisplayName("오늘 예정된 회의 가져오기 - 성공 / 오늘의 회의가 없을 경우")
    @Test
    void today_expected_meeting_success_empty_today_meetings() throws Exception {

        // given
        TodayMeetingRequestDto requestDto = TodayMeetingRequestDtoBuilder.build();
        when(meetingQueryPort.findTodayMeetingsWithOperationTeam(any(), any()))
                .thenReturn(Collections.emptyList());

        // when, then
        List<TodayMeetingResponseDto> responseDtos = meetingQueryService.getTodayMeetings(requestDto);

        // then
        assertAll(
                () -> assertTrue(responseDtos.isEmpty()),
                () -> verify(meetingQueryPort).findTodayMeetingsWithOperationTeam(requestDto.getWorkspaceId(), requestDto.getWorkspaceUserId()),
                () -> verify(agendaQueryPort, times(0)).findByMeetingIds(any()),
                () -> verify(attendeeQueryPort, times(0)).findMeetingAdminsWithWorkspaceUserByMeetingIds(any()),
                () -> verify(attendeeQueryPort, times(0)).countAttendStatusByMeetingIds(any())
        );
    }

    @DisplayName("오늘 예정된 회의 가져오기 - 성공 / 오늘의 회의가 있을 경우")
    @Test
    void today_expected_meeting_success() throws Exception {

        // given
        TodayMeetingRequestDto requestDto = TodayMeetingRequestDtoBuilder.build();
        when(meetingQueryPort.findTodayMeetingsWithOperationTeam(any(), any())).thenReturn(List.of(MEETING));
        when(agendaQueryPort.findByMeetingIds(any())).thenReturn(List.of(AGENDA1, AGENDA2));
        when(attendeeQueryPort.findMeetingAdminsWithWorkspaceUserByMeetingIds(any())).thenReturn(List.of(ADMIN_ATTENDEE));
        List<AttendStatusCountQueryDto> attendStatusCountQueryDtos = AttendStatusCountResponseDtoBuilder.buildList();
        when(attendeeQueryPort.countAttendStatusByMeetingIds(any())).thenReturn(attendStatusCountQueryDtos);

        // when
        List<TodayMeetingResponseDto> responseDtos = meetingQueryService.getTodayMeetings(requestDto);

        // then
        assertAll(
                () -> verify(meetingQueryPort).findTodayMeetingsWithOperationTeam(requestDto.getWorkspaceId(), requestDto.getWorkspaceUserId()),
                () -> verify(agendaQueryPort).findByMeetingIds(List.of(MEETING.getId())),
                () -> verify(attendeeQueryPort).findMeetingAdminsWithWorkspaceUserByMeetingIds(List.of(MEETING.getId())),
                () -> verify(attendeeQueryPort).countAttendStatusByMeetingIds(List.of(MEETING.getId())),
                () -> assertThat(responseDtos).usingRecursiveComparison()
                        .isEqualTo(TodayMeetingResponseDto.fromEntities(List.of(MEETING),
                                List.of(AGENDA1, AGENDA2),
                                List.of(ADMIN_ATTENDEE),
                                attendStatusCountQueryDtos))
        );
    }

    @DisplayName("회의 상세 조회 - 성공")
    @Test
    void get_meeting_success() throws Exception {

        // given
        MeetingAndAdminsQueryDto queryDto = MeetingAndAdminsQueryDtoBuilder.build();
        when(meetingQueryPort.findWithTeamAndAdminsById(any()))
                .thenReturn(Optional.of(queryDto));

        // when
        MeetingResponseDto responseDto = meetingQueryService.getMeeting(1L);

        // then
        assertAll(
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingId(), responseDto.getMeetingId()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingName(), responseDto.getMeetingName()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingPurpose(), responseDto.getMeetingPurpose()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingDate(), responseDto.getMeetingDate()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getStartTime(), responseDto.getStartTime()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getEndTime(), responseDto.getEndTime()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getOperationTeamId(), responseDto.getOperationTeamId()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getOperationTeamName(), responseDto.getOperationTeamName()),
                () -> assertEquals(queryDto.getAdminQueryDtos().get(0).getWorkspaceUserId(),
                        responseDto.getAdmins().get(0).getWorkspaceUserId()),
                () -> assertEquals(queryDto.getAdminQueryDtos().get(1).getWorkspaceUserId(),
                        responseDto.getAdmins().get(1).getWorkspaceUserId())
        );
    }
}
