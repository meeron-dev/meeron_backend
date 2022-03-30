package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.domain.Attendee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.cmc.meeron.meeting.AttendeeFixture.ATTENDEE_1;
import static com.cmc.meeron.meeting.AttendeeFixture.ATTENDEE_2;
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
        Attendee attendee1 = ATTENDEE_1;
        Attendee attendee2 = ATTENDEE_2;
        List<Attendee> attendees = List.of(attendee1, attendee2);
        when(attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(any(), any()))
                .thenReturn(attendees);
        MeetingAttendeesRequestDto requestDto = MeetingAttendeesRequestDtoBuilder.build();

        // when
        MeetingAttendeesResponseDto responseDto = attendeeQueryService.getMeetingAttendees(requestDto);

        // then
        assertAll(
                () -> verify(attendeeQueryPort).findWithWorkspaceUserByMeetingIdTeamId(
                        requestDto.getMeetingId(), requestDto.getTeamId()),
                () -> assertEquals(1, responseDto.getUnknowns().size()),
                () -> assertEquals(1, responseDto.getAttends().size()),
                () -> assertEquals(0, responseDto.getAbsents().size())
        );
    }
}