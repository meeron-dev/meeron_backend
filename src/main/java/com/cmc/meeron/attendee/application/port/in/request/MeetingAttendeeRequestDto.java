package com.cmc.meeron.attendee.application.port.in.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeeRequestDto {

    private Long meetingId;
    private Long workspaceUserId;

    public static MeetingAttendeeRequestDto from(Long meetingId, Long workspaceUserId) {
        return MeetingAttendeeRequestDto.builder()
                .meetingId(meetingId)
                .workspaceUserId(workspaceUserId)
                .build();
    }
}
