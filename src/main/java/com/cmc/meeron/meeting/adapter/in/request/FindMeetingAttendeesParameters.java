package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.meeting.application.port.in.request.MeetingAttendeesRequestDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindMeetingAttendeesParameters {

    @NotNull(message = "회의 참가자가 속한 팀 ID를 입력해주세요.")
    private Long teamId;

    public MeetingAttendeesRequestDto toRequestDto(Long meetingId) {
        return MeetingAttendeesRequestDto.builder()
                .teamId(teamId)
                .meetingId(meetingId)
                .build();
    }
}
