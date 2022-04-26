package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.in.request.MeetingAttendeeRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.MeetingAttendeesRequestDtoBuilder;
import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.MyMeetingAttendeeRequestDtoBuilder;
import com.cmc.meeron.attendee.application.port.in.response.*;
import com.cmc.meeron.attendee.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDtoBuilder;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.support.TestImproved;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.attendee.AttendeeFixture.ADMIN_ATTENDEE;
import static com.cmc.meeron.attendee.AttendeeFixture.NOT_ADMIN_ATTENDEE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendeeQueryServiceTest {

    @Mock
    AttendeeQueryPort attendeeQueryPort;
    @InjectMocks
    AttendeeQueryService attendeeQueryService;

    @DisplayName("회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_success() throws Exception {

        // given
        List<MeetingAttendeesCountsByTeamQueryDto> queryDtos = MeetingAttendeesCountsByTeamQueryDtoBuilder.buildList();
        when(attendeeQueryPort.countsMeetingAttendeesByTeam(any()))
                .thenReturn(queryDtos);

        // when
        List<MeetingAttendeesResponseDto> responseDtos = attendeeQueryService.getMeetingAttendees(1L);

        // then
        MeetingAttendeesResponseDto one = responseDtos.get(0);
        MeetingAttendeesResponseDto two = responseDtos.get(1);
        MeetingAttendeesResponseDto three = responseDtos.get(2);
        assertAll(
                () -> assertEquals(1, one.getAttends()),
                () -> assertEquals(2, one.getUnknowns()),
                () -> assertEquals(3, two.getUnknowns()),
                () -> assertEquals(1, two.getAbsents()),
                () -> assertEquals(3, three.getAttends())
        );
    }

    @TestImproved(originMethod = "get_meeting_attendees_success")
    @DisplayName("Improved 회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_counts_by_team_success() throws Exception {

        // given
        List<MeetingAttendeesCountsByTeamQueryDto> meetingAttendeesCountsByTeamQueryDtos =
                MeetingAttendeesCountsByTeamQueryDtoBuilder.buildList();
        when(attendeeQueryPort.countsMeetingAttendeesByTeam(any()))
                .thenReturn(meetingAttendeesCountsByTeamQueryDtos);

        // when
        List<MeetingAttendeesCountsByTeamResponseDto> responseDto = attendeeQueryService.getMeetingAttendeesCountsByTeam(1L);

        // then
        assertAll(
                () -> verify(attendeeQueryPort).countsMeetingAttendeesByTeam(1L),
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(MeetingAttendeesCountsByTeamResponseDto.fromQueryDtos(meetingAttendeesCountsByTeamQueryDtos))
        );
    }

    @DisplayName("회의에 참여하는 팀의 참가자 조회 - 성공")
    @Test
    void get_meeting_team_attendees_success() throws Exception {

        // given
        Attendee attendee1 = ADMIN_ATTENDEE;
        Attendee attendee2 = NOT_ADMIN_ATTENDEE;
        List<Attendee> attendees = List.of(attendee1, attendee2);
        when(attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(any(), any()))
                .thenReturn(attendees);
        MeetingTeamAttendeesRequestDto requestDto = MeetingAttendeesRequestDtoBuilder.build();

        // when
        MeetingTeamAttendeesResponseDto responseDto = attendeeQueryService.getMeetingTeamAttendees(requestDto);

        // then
        assertAll(
                () -> verify(attendeeQueryPort).findWithWorkspaceUserByMeetingIdTeamId(
                        requestDto.getMeetingId(), requestDto.getTeamId()),
                () -> assertEquals(1, responseDto.getUnknowns().size()),
                () -> assertEquals(1, responseDto.getAttends().size()),
                () -> assertEquals(0, responseDto.getAbsents().size())
        );
    }

    @TestImproved(originMethod = "get_meeting_team_attendees_success")
    @DisplayName("Improved 회의 참가자 팀원들 조회 - 성공")
    @Test
    void get_meeting_team_attendees_success_v2() throws Exception {

        // given
        Attendee attendee1 = ADMIN_ATTENDEE;
        Attendee attendee2 = NOT_ADMIN_ATTENDEE;
        List<Attendee> attendees = List.of(attendee1, attendee2);
        when(attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(any(), any()))
                .thenReturn(attendees);
        MeetingTeamAttendeesRequestDto requestDto = MeetingAttendeesRequestDtoBuilder.build();

        // when
        MeetingTeamAttendeesResponseDtoV2 responseDto = attendeeQueryService.getMeetingTeamAttendeesV2(requestDto);

        // then
        assertAll(
                () -> verify(attendeeQueryPort).findWithWorkspaceUserByMeetingIdTeamId(
                        requestDto.getMeetingId(), requestDto.getTeamId()),
                () -> assertEquals(1, responseDto.getUnknowns().size()),
                () -> assertEquals(1, responseDto.getAttends().size()),
                () -> assertEquals(0, responseDto.getAbsents().size())
        );
    }

    @DisplayName("회의 관리자 조회 - 성공")
    @Test
    void get_meeting_admins_success() throws Exception {

        // given
        List<Attendee> attendees = List.of(ADMIN_ATTENDEE);
        when(attendeeQueryPort.findMeetingAdminsWithWorkspaceUserByMeetingId(any()))
                .thenReturn(attendees);

        // when
        List<AttendeeResponseDto> responseDtos = attendeeQueryService.getMeetingAdmins(1L);

        // then
        assertAll(
                () -> verify(attendeeQueryPort).findMeetingAdminsWithWorkspaceUserByMeetingId(1L),
                () -> assertThat(responseDtos).isNotEmpty(),
                () -> assertThat(responseDtos)
                        .usingRecursiveComparison()
                        .isEqualTo(AttendeeResponseDto.from(attendees))
        );
    }

    @DisplayName("내가 참여한 회의의 참가자 정보 조회 - 실패 / 존재하지 않을 경우")
    @Test
    void get_meeting_attendee_fail_not_found() throws Exception {

        // given
        MeetingAttendeeRequestDto requestDto = MyMeetingAttendeeRequestDtoBuilder.build();
        when(attendeeQueryPort.findWithWorkspaceUserByMeetingIdWorkspaceUserId(any(), any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(AttendeeNotFoundException.class,
                () -> attendeeQueryService.getMeetingAttendee(requestDto));
    }

    @DisplayName("내가 참여한 회의의 참가자 정보 조회 - 성공")
    @Test
    void get_meeting_attendee_success() throws Exception {

        // given
        MeetingAttendeeRequestDto requestDto = MyMeetingAttendeeRequestDtoBuilder.build();
        when(attendeeQueryPort.findWithWorkspaceUserByMeetingIdWorkspaceUserId(any(), any()))
                .thenReturn(Optional.of(NOT_ADMIN_ATTENDEE));

        // when
        AttendeeResponseDto responseDto = attendeeQueryService.getMeetingAttendee(requestDto);

        // then
        assertAll(
                () -> verify(attendeeQueryPort).findWithWorkspaceUserByMeetingIdWorkspaceUserId(requestDto.getMeetingId()
                        , requestDto.getWorkspaceUserId()),
                () -> assertThat(responseDto)
                        .usingRecursiveComparison()
                        .isEqualTo(AttendeeResponseDto.from(NOT_ADMIN_ATTENDEE))
        );
    }
}
