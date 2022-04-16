package com.cmc.meeron.attendee.application.port.in.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTeamAttendeesRequestDto {

    private Long meetingId;
    private Long teamId;

    public static MeetingTeamAttendeesRequestDto of(Long meetingId, Long teamId) {
        return MeetingTeamAttendeesRequestDto.builder()
                .meetingId(meetingId)
                .teamId(teamId)
                .build();
    }
}
