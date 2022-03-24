package com.cmc.meeron.meeting.application.port.in.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeAttendStatusRequestDto {

    private Long meetingId;
    private Long workspaceUserId;
    private String status;
}
