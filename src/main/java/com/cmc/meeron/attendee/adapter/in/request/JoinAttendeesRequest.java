package com.cmc.meeron.attendee.adapter.in.request;

import com.cmc.meeron.attendee.application.port.in.request.JoinAttendeesRequestDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinAttendeesRequest {

    @Builder.Default
    @NotEmpty(message = "참가자의 워크스페이스 유저 ID는 반드시 하나 이상이어야 합니다.")
    private List<Long> workspaceUserIds = new ArrayList<>();

    public JoinAttendeesRequestDto toRequestDto(Long meetingId) {
        return JoinAttendeesRequestDto.builder()
                .meetingId(meetingId)
                .workspaceUserIds(workspaceUserIds)
                .build();
    }
}
