package com.cmc.meeron.meeting.application.port.in.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeesRequestDto {

    private Long meetingId;
    private Long teamId;
}
