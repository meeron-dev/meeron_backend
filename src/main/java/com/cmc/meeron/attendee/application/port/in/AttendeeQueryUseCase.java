package com.cmc.meeron.attendee.application.port.in;

import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesCountsByTeamResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDtoV2;

import java.util.List;

public interface AttendeeQueryUseCase {

    List<MeetingAttendeesResponseDto> getMeetingAttendees(Long meetingId);

    List<MeetingAttendeesCountsByTeamResponseDto> getMeetingAttendeesCountsByTeam(Long meetingId);

    MeetingTeamAttendeesResponseDto getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto meetingTeamAttendeesRequestDto);

    MeetingTeamAttendeesResponseDtoV2 getMeetingTeamAttendeesV2(MeetingTeamAttendeesRequestDto requestDto);
}
