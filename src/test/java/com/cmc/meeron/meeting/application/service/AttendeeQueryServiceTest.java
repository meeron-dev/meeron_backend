package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingTeamAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAttendeesQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAttendeesQueryDtoBuilder;
import com.cmc.meeron.meeting.domain.Attendee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.cmc.meeron.meeting.AttendeeFixture.ADMIN_ATTENDEE;
import static com.cmc.meeron.meeting.AttendeeFixture.NOT_ADMIN_ATTENDEE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @DisplayName("회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_success() throws Exception {

        // given
        List<MeetingAttendeesQueryDto> queryDtos = MeetingAttendeesQueryDtoBuilder.buildList();
        when(attendeeQueryPort.findMeetingAttendees(any()))
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
}