package com.cmc.meeron.meeting.adapter.in.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMeetingResponse {

    private Long meetingId;

    public static CreateMeetingResponse of(Long meetingId) {
        return CreateMeetingResponse.builder()
                .meetingId(meetingId)
                .build();
    }
}
