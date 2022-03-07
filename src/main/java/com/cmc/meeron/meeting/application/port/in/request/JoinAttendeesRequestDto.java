package com.cmc.meeron.meeting.application.port.in.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinAttendeesRequestDto {

    private Long meetingId;
    private List<Long> workspaceUserIds;

    public static JoinAttendeesRequestDto of(Long meetingId, List<Long> workspaceUserIds) {
        return JoinAttendeesRequestDto.builder()
                .meetingId(meetingId)
                .workspaceUserIds(workspaceUserIds)
                .build();
    }
}
