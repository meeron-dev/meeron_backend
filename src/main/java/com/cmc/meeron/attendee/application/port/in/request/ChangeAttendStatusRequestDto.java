package com.cmc.meeron.attendee.application.port.in.request;

import com.cmc.meeron.attendee.adapter.in.request.AttendStatusType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeAttendStatusRequestDto {

    private Long attendeeId;
    private String status;
    @Deprecated
    private Long meetingId;
    @Deprecated
    private Long workspaceUserId;


    public static ChangeAttendStatusRequestDto fromPathParameters(Long attendeeId, AttendStatusType attendStatusType) {
        return ChangeAttendStatusRequestDto.builder()
                .attendeeId(attendeeId)
                .status(attendStatusType.name())
                .build();
    }
}
