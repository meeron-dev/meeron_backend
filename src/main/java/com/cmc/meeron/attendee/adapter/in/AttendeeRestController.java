package com.cmc.meeron.attendee.adapter.in;

import com.cmc.meeron.attendee.application.port.in.AttendeeCommandUseCase;
import com.cmc.meeron.attendee.adapter.in.request.ChangeAttendStatusRequest;
import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttendeeRestController {

    private final AttendeeCommandUseCase attendeeCommandUseCase;

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
}
