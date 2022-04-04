package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.meeting.application.port.in.request.DeleteMeetingRequestDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteMeetingRequest {

    private Long attendeeWorkspaceUserId;

    public DeleteMeetingRequestDto toRequestDto(Long meetingId) {
        return DeleteMeetingRequestDto.builder()
                .meetingId(meetingId)
                .attendeeWorkspaceUserId(attendeeWorkspaceUserId)
                .build();
    }
}
