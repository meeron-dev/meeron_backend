package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.adapter.in.request.*;
import com.cmc.meeron.meeting.adapter.in.response.CreateAgendaResponse;
import com.cmc.meeron.meeting.adapter.in.response.CreateMeetingResponse;
import com.cmc.meeron.meeting.application.port.in.MeetingCommandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeetingCommandRestController {

    private final MeetingCommandUseCase meetingCommandUseCase;

    @PostMapping("/meetings")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMeetingResponse createMeeting(@RequestBody @Valid CreateMeetingRequest createMeetingRequest,
                                               @AuthenticationPrincipal AuthUser authUser) {
        Long createdTeamId = meetingCommandUseCase.createMeeting(createMeetingRequest.toRequestDto(), authUser);
        return CreateMeetingResponse.of(createdTeamId);
    }

    @PostMapping(value = "/meetings/{meetingId}/attendees")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinAttendees(@PathVariable Long meetingId,
                              @RequestBody @Valid JoinAttendeesRequest joinAttendeesRequest) {
        meetingCommandUseCase.joinAttendees(joinAttendeesRequest.toRequestDto(meetingId));
    }

    @PostMapping("/meetings/{meetingId}/agendas")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAgendaResponse createAgendas(@PathVariable Long meetingId,
                                              @RequestBody @Valid CreateAgendaRequest createAgendaRequest) {
        List<Long> responseDtos =
                meetingCommandUseCase.createAgendas(createAgendaRequest.toRequestDtoAndSortByAgendaOrder(meetingId));
        return CreateAgendaResponse.of(responseDtos);
    }

    @PatchMapping("/attendees/{workspaceUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeAttendeeStatus(@PathVariable("workspaceUserId") Long workspaceUserId,
                                     @RequestBody @Valid ChangeAttendStatusRequest changeAttendStatusRequest) {
        meetingCommandUseCase.changeAttendStatus(changeAttendStatusRequest.toRequestDto(workspaceUserId));
    }

    @PostMapping("/meetings/{meetingId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeeting(@PathVariable Long meetingId,
                              @RequestBody DeleteMeetingRequest deleteMeetingRequest) {
        meetingCommandUseCase.deleteMeeting(deleteMeetingRequest.toRequestDto(meetingId));
    }
}
