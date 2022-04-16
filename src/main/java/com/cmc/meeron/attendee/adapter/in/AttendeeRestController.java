package com.cmc.meeron.attendee.adapter.in;

import com.cmc.meeron.attendee.application.port.in.AttendeeCommandUseCase;
import com.cmc.meeron.attendee.adapter.in.request.ChangeAttendStatusRequest;
import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequest;
import com.cmc.meeron.attendee.adapter.in.response.MeetingAttendeesResponse;
import com.cmc.meeron.attendee.adapter.in.response.MeetingTeamAttendeesResponse;
import com.cmc.meeron.attendee.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttendeeRestController {

    private final AttendeeCommandUseCase attendeeCommandUseCase;
    private final AttendeeQueryUseCase attendeeQueryUseCase;

    // fixme /attendees/{id} ?
    @PatchMapping("/attendees/{workspaceUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeAttendeeStatus(@PathVariable("workspaceUserId") Long workspaceUserId,
                                     @RequestBody @Valid ChangeAttendStatusRequest changeAttendStatusRequest) {
        attendeeCommandUseCase.changeAttendStatus(changeAttendStatusRequest.toRequestDto(workspaceUserId));
    }

    @PostMapping(value = "/meetings/{meetingId}/attendees")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinAttendees(@PathVariable Long meetingId,
                              @RequestBody @Valid JoinAttendeesRequest joinAttendeesRequest) {
        attendeeCommandUseCase.joinAttendees(joinAttendeesRequest.toRequestDto(meetingId));
    }

    // FIXME: 2022/04/14 kobeomseok95 /api/meetings/:id/attendees/count ?
    @GetMapping("/meetings/{meetingId}/attendees/teams")
    @ResponseStatus(HttpStatus.OK)
    public MeetingAttendeesResponse getMeetingAttendees(@PathVariable Long meetingId) {
        List<MeetingAttendeesResponseDto> responseDtos = attendeeQueryUseCase.getMeetingAttendees(meetingId);
        return MeetingAttendeesResponse.fromResponseDtos(responseDtos);
    }

    // FIXME: 2022/04/14 kobeomseok95 /api/meetings/:id/teams/:id/attendees ?
    @GetMapping("/meetings/{meetingId}/attendees/teams/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public MeetingTeamAttendeesResponse getMeetingTeamAttendees(@PathVariable Long meetingId,
                                                                @PathVariable Long teamId) {
        MeetingTeamAttendeesResponseDto responseDto = attendeeQueryUseCase
                .getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto.of(meetingId, teamId));
        return MeetingTeamAttendeesResponse.fromResponseDto(responseDto);
    }
}
