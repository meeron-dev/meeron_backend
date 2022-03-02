package com.cmc.meeron.meeting.application.dto.request;

import com.cmc.meeron.meeting.presentation.dto.request.TodayMeetingRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayMeetingRequestDto {

    private Long workspaceId;
    private Long workspaceUserId;

    public static TodayMeetingRequestDto of(TodayMeetingRequest todayMeetingRequest) {
        return TodayMeetingRequestDto.builder()
            .workspaceId(todayMeetingRequest.getWorkspaceId())
            .workspaceUserId(todayMeetingRequest.getWorkspaceUserId())
            .build();
    }
}
