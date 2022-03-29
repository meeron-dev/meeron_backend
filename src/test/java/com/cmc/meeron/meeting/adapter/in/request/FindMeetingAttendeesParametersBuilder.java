package com.cmc.meeron.meeting.adapter.in.request;

public class FindMeetingAttendeesParametersBuilder {

    public static FindMeetingAttendeesParameters build() {
        return FindMeetingAttendeesParameters.builder()
                .teamId(1L)
                .build();
    }
}
