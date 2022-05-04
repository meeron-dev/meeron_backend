package com.cmc.meeron.attendee.application.port.in;

import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.MeetingAttendeeRequestDto;
import com.cmc.meeron.attendee.application.port.in.response.*;

import java.util.List;

public interface AttendeeQueryUseCase {

    List<MeetingAttendeesResponseDto> getMeetingAttendees(Long meetingId);

    List<MeetingAttendeesCountsByTeamResponseDto> getMeetingAttendeesCountsByTeam(Long meetingId);

    MeetingTeamAttendeesResponseDto getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto meetingTeamAttendeesRequestDto);

    MeetingTeamAttendeesResponseDtoV2 getMeetingTeamAttendeesV2(MeetingTeamAttendeesRequestDto meetingTeamAttendeesRequestDto);

    List<AttendeeResponseDto> getMeetingAdmins(Long meetingId);

    AttendeeResponseDto getMeetingAttendee(MeetingAttendeeRequestDto meetingAttendeeRequestDto);
}
