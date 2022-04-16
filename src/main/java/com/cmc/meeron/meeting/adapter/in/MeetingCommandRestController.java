package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.DeleteMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.response.CreateMeetingResponse;
import com.cmc.meeron.meeting.application.port.in.MeetingCommandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/meetings/{meetingId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeeting(@PathVariable Long meetingId,
                              @RequestBody DeleteMeetingRequest deleteMeetingRequest) {
        meetingCommandUseCase.deleteMeeting(deleteMeetingRequest.toRequestDto(meetingId));
    }
}
