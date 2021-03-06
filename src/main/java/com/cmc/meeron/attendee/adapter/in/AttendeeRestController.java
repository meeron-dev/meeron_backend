package com.cmc.meeron.attendee.adapter.in;

import com.cmc.meeron.attendee.adapter.in.request.AttendStatusType;
import com.cmc.meeron.attendee.adapter.in.request.ChangeAttendStatusRequest;
import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequest;
import com.cmc.meeron.attendee.adapter.in.response.*;
import com.cmc.meeron.attendee.application.port.in.AttendeeCommandUseCase;
import com.cmc.meeron.attendee.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.attendee.application.port.in.request.ChangeAttendStatusRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.MeetingAttendeeRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.MeetingTeamAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.in.response.*;
import com.cmc.meeron.common.meta.Improved;
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

    @Deprecated
    @PatchMapping("/attendees/{workspaceUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeAttendeeStatus(@PathVariable("workspaceUserId") Long workspaceUserId,
                                     @RequestBody @Valid ChangeAttendStatusRequest changeAttendStatusRequest) {
        attendeeCommandUseCase.changeAttendStatus(changeAttendStatusRequest.toRequestDto(workspaceUserId));
    }

    @Improved(originMethod = "changeAttendeeStatus")
    @PatchMapping("/attendees/{attendeeId}/{status}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeAttendeeStatusV2(@PathVariable Long attendeeId,
                                       @PathVariable AttendStatusType status) {
        attendeeCommandUseCase.changeAttendStatusV2(ChangeAttendStatusRequestDto.fromPathParameters(attendeeId, status));
    }

    @PostMapping(value = "/meetings/{meetingId}/attendees")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinAttendees(@PathVariable Long meetingId,
                              @RequestBody @Valid JoinAttendeesRequest joinAttendeesRequest) {
        attendeeCommandUseCase.joinAttendees(joinAttendeesRequest.toRequestDto(meetingId));
    }

    @Deprecated
    @GetMapping("/meetings/{meetingId}/attendees/teams")
    @ResponseStatus(HttpStatus.OK)
    public MeetingAttendeesResponse getMeetingAttendees(@PathVariable Long meetingId) {
        List<MeetingAttendeesResponseDto> responseDtos = attendeeQueryUseCase.getMeetingAttendees(meetingId);
        return MeetingAttendeesResponse.fromResponseDtos(responseDtos);
    }

    @Improved(originMethod = "getMeetingAttendees")
    @GetMapping("/meetings/{meetingId}/attendees/counts")
    @ResponseStatus(HttpStatus.OK)
    public MeetingAttendeesCountsByTeamResponse getMeetingAttendeesCountsByTeam(@PathVariable Long meetingId) {
        List<MeetingAttendeesCountsByTeamResponseDto> responseDtos = attendeeQueryUseCase
                .getMeetingAttendeesCountsByTeam(meetingId);
        return MeetingAttendeesCountsByTeamResponse.fromResponseDtos(responseDtos);
    }

    @Deprecated
    @GetMapping("/meetings/{meetingId}/attendees/teams/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public MeetingTeamAttendeesResponse getMeetingTeamAttendees(@PathVariable Long meetingId,
                                                                @PathVariable Long teamId) {
        MeetingTeamAttendeesResponseDto responseDto = attendeeQueryUseCase
                .getMeetingTeamAttendees(MeetingTeamAttendeesRequestDto.of(meetingId, teamId));
        return MeetingTeamAttendeesResponse.fromResponseDto(responseDto);
    }

    @Improved(originMethod = "getMeetingTeamAttendees")
    @GetMapping("/meetings/{meetingId}/teams/{teamId}/attendees")
    @ResponseStatus(HttpStatus.OK)
    public MeetingTeamAttendeesResponseV2 getMeetingTeamAttendeesV2(@PathVariable Long meetingId,
                                                                    @PathVariable Long teamId) {
        MeetingTeamAttendeesResponseDtoV2 responseDto = attendeeQueryUseCase
                .getMeetingTeamAttendeesV2(MeetingTeamAttendeesRequestDto.of(meetingId, teamId));
        return MeetingTeamAttendeesResponseV2.from(responseDto);
    }

    @GetMapping("/meetings/{meetingId}/admins")
    @ResponseStatus(HttpStatus.OK)
    public AttendeeResponses getMeetingAdmins(@PathVariable Long meetingId) {
        List<AttendeeResponseDto> responseDtos = attendeeQueryUseCase.getMeetingAdmins(meetingId);
        return AttendeeResponses.from(responseDtos);
    }

    @GetMapping("/meetings/{meetingId}/attendees/me")
    @ResponseStatus(HttpStatus.OK)
    public AttendeeWorkspaceUserResponse getMyMeetingAttendee(@PathVariable Long meetingId,
                                                              @RequestParam(value = "workspaceUserId") Long workspaceUserId) {
        AttendeeResponseDto responseDto = attendeeQueryUseCase
                .getMeetingAttendee(MeetingAttendeeRequestDto.from(meetingId, workspaceUserId));
        return AttendeeWorkspaceUserResponse.from(responseDto);
    }
}
