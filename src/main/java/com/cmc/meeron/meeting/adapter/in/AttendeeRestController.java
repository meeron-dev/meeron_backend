package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.request.FindMeetingAttendeesParameters;
import com.cmc.meeron.meeting.adapter.in.response.MeetingAttendeesResponse;
import com.cmc.meeron.meeting.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttendeeRestController {

    private final AttendeeQueryUseCase attendeeQueryUseCase;

    @GetMapping("/meetings/{meetingId}/attendees")
    @ResponseStatus(HttpStatus.OK)
    public MeetingAttendeesResponse getMeetingAttendees(@PathVariable Long meetingId,
                                                        @Valid FindMeetingAttendeesParameters findMeetingAttendeesParameters) {
        MeetingAttendeesResponseDto responseDto = attendeeQueryUseCase
                .getMeetingAttendees(findMeetingAttendeesParameters.toRequestDto(meetingId));
        return MeetingAttendeesResponse.fromResponseDto(responseDto);
    }
}
