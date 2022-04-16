package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesQueryDto;
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
    public List<MeetingAttendeesResponseDto> getMeetingAttendees(Long meetingId) {
        List<MeetingAttendeesQueryDto> queryDtos = attendeeQueryPort.findMeetingAttendees(meetingId);
        return MeetingAttendeesResponseDto.fromQueryDtos(queryDtos);
    }

    @Override
    public MeetingTeamAttendeesResponseDto getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto meetingTeamAttendeesRequestDto) {
        List<Attendee> attendees = attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(
                meetingTeamAttendeesRequestDto.getMeetingId(),
                meetingTeamAttendeesRequestDto.getTeamId());
        return MeetingTeamAttendeesResponseDto.fromEntities(attendees);
    }
}
