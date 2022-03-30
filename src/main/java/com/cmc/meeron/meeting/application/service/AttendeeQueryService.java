package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.domain.Attendee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AttendeeQueryService implements AttendeeQueryUseCase {

    private final AttendeeQueryPort attendeeQueryPort;

    @Override
    public MeetingAttendeesResponseDto getMeetingAttendees(MeetingAttendeesRequestDto meetingAttendeesRequestDto) {
        List<Attendee> attendees = attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(
                meetingAttendeesRequestDto.getMeetingId(),
                meetingAttendeesRequestDto.getTeamId());
        return MeetingAttendeesResponseDto.fromEntities(attendees);
    }
}
