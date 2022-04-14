package com.cmc.meeron.attendee.adapter.in.request;

import java.util.List;

public class JoinAttendeesRequestBuilder {

    public static JoinAttendeesRequest buildInvalid() {
        return JoinAttendeesRequest.builder().build();
    }

    public static JoinAttendeesRequest build() {
        return JoinAttendeesRequest.builder()
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }
}
