package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class AdminQueryDto {

    private Long workspaceUserId;
    private String nickname;

    @QueryProjection
    public AdminQueryDto(Long workspaceUserId, String nickname) {
        this.workspaceUserId = workspaceUserId;
        this.nickname = nickname;
    }
}
