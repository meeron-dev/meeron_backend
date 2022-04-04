package com.cmc.meeron.meeting.adapter.in.request;

public class DeleteMeetingRequestBuilder {

    public static DeleteMeetingRequest build() {
        return DeleteMeetingRequest.builder()
                .attendeeWorkspaceUserId(1L)
                .build();
    }

    public static DeleteMeetingRequest buildIntegrationSuccessCase() {
        return DeleteMeetingRequest.builder()
                .attendeeWorkspaceUserId(2L)
                .build();
    }
}
