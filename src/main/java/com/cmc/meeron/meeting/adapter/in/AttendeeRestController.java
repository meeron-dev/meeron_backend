package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.response.MeetingAttendeesResponse;
import com.cmc.meeron.meeting.adapter.in.response.MeetingTeamAttendeesResponse;
import com.cmc.meeron.meeting.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingTeamAttendeesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}")
public class AttendeeRestController {

    private final AttendeeQueryUseCase attendeeQueryUseCase;

    @GetMapping("/attendees/teams")
    @ResponseStatus(HttpStatus.OK)
    public MeetingAttendeesResponse getMeetingAttendees(@PathVariable Long meetingId) {
        List<MeetingAttendeesResponseDto> responseDtos = attendeeQueryUseCase.getMeetingAttendees(meetingId);
        return MeetingAttendeesResponse.fromResponseDtos(responseDtos);
    }

    @GetMapping("/attendees/teams/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public MeetingTeamAttendeesResponse getMeetingTeamAttendees(@PathVariable Long meetingId,
                                                                @PathVariable Long teamId) {
        MeetingTeamAttendeesResponseDto responseDto = attendeeQueryUseCase
                .getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto.of(meetingId, teamId));
        return MeetingTeamAttendeesResponse.fromResponseDto(responseDto);
    }
}
