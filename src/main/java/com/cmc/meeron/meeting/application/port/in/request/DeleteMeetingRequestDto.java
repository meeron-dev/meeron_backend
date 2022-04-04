package com.cmc.meeron.meeting.application.port.in.request;

import com.cmc.meeron.common.advice.attendee.AttendeeAuthorityCheckable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteMeetingRequestDto implements AttendeeAuthorityCheckable {

    private Long meetingId;
    private Long attendeeWorkspaceUserId;

    @Override
    public Long getWorkspaceUserId() {
        return attendeeWorkspaceUserId;
    }
}
