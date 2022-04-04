package com.cmc.meeron.meeting.application.port.in.request;

public class DeleteMeetingRequestDtoBuilder {

    public static DeleteMeetingRequestDto build() {
        return DeleteMeetingRequestDto.builder()
                .meetingId(1L)
                .attendeeWorkspaceUserId(1L)
                .build();
    }
}
