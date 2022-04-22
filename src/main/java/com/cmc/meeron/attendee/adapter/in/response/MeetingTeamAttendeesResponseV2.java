package com.cmc.meeron.attendee.adapter.in.response;

import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDtoV2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTeamAttendeesResponseV2 {

    @Builder.Default
    private List<AttendeeWorkspaceUserResponse> attends = new ArrayList<>();

    @Builder.Default
    private List<AttendeeWorkspaceUserResponse> absents = new ArrayList<>();

    @Builder.Default
    private List<AttendeeWorkspaceUserResponse> unknowns = new ArrayList<>();

    public static MeetingTeamAttendeesResponseV2 from(MeetingTeamAttendeesResponseDtoV2 responseDto) {
        return MeetingTeamAttendeesResponseV2.builder()
                .attends(responseDto.getAttends()
                        .stream()
                        .map(AttendeeWorkspaceUserResponse::from)
                        .collect(Collectors.toList()))
                .absents(responseDto.getAbsents()
                        .stream()
                        .map(AttendeeWorkspaceUserResponse::from)
                        .collect(Collectors.toList()))
                .unknowns(responseDto.getUnknowns()
                        .stream()
                        .map(AttendeeWorkspaceUserResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
