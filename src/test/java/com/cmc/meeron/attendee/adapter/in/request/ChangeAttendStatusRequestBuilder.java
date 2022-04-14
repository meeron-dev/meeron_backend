package com.cmc.meeron.attendee.adapter.in.request;

public class ChangeAttendStatusRequestBuilder {

    public static ChangeAttendStatusRequest build() {
        return ChangeAttendStatusRequest.builder()
                .meetingId(1L)
                .status("attend")
                .build();
    }

    public static ChangeAttendStatusRequest buildNotValid() {
        return ChangeAttendStatusRequest.builder()
                .meetingId(null)
                .status("    ")
                .build();
    }
}
