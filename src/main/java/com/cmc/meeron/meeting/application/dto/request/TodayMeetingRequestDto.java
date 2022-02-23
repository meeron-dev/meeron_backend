package com.cmc.meeron.meeting.application.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayMeetingRequestDto {

    private Long workspaceId;
    private Long workspaceUserId;
}
