package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.response.*;
import com.cmc.meeron.attendee.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.common.meta.Improved;
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
        List<MeetingAttendeesCountsByTeamQueryDto> queryDtos = attendeeQueryPort.countsMeetingAttendeesByTeam(meetingId);
        return MeetingAttendeesResponseDto.fromQueryDtos(queryDtos);
    }

    @Improved(originMethod = "getMeetingAttendees")
    @Override
    public List<MeetingAttendeesCountsByTeamResponseDto> getMeetingAttendeesCountsByTeam(Long meetingId) {
        List<MeetingAttendeesCountsByTeamQueryDto> queryDtos = attendeeQueryPort.countsMeetingAttendeesByTeam(meetingId);
        return MeetingAttendeesCountsByTeamResponseDto.fromQueryDtos(queryDtos);
    }

    @Override
    public MeetingTeamAttendeesResponseDto getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto meetingTeamAttendeesRequestDto) {
        List<Attendee> attendees = attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(
                meetingTeamAttendeesRequestDto.getMeetingId(),
                meetingTeamAttendeesRequestDto.getTeamId());
        return MeetingTeamAttendeesResponseDto.fromEntities(attendees);
    }

    @Improved(originMethod = "getMeetingTeamAttendees")
    @Override
    public MeetingTeamAttendeesResponseDtoV2 getMeetingTeamAttendeesV2(MeetingTeamAttendeesRequestDto meetingTeamAttendeesRequestDto) {
        List<Attendee> attendees = attendeeQueryPort.findWithWorkspaceUserByMeetingIdTeamId(
                meetingTeamAttendeesRequestDto.getMeetingId(),
                meetingTeamAttendeesRequestDto.getTeamId());
        return MeetingTeamAttendeesResponseDtoV2.fromEntities(attendees);
    }

    @Override
    public List<AttendeeResponseDto> getMeetingAdmins(Long meetingId) {
        List<Attendee> admins = attendeeQueryPort.findMeetingAdminsWithWorkspaceUserByMeetingId(meetingId);
        return AttendeeResponseDto.from(admins);
    }
}
