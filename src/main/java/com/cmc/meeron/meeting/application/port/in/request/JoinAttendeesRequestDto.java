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
}
