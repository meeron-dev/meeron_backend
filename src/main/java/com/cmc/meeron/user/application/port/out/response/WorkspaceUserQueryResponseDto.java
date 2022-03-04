package com.cmc.meeron.user.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WorkspaceUserQueryResponseDto {

    private Long workspaceUserId;
    private String profileImageUrl;
    private String nickname;
    private String position;

    @QueryProjection
    public WorkspaceUserQueryResponseDto(Long workspaceUserId, String profileImageUrl, String nickname, String position) {
        this.workspaceUserId = workspaceUserId;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.position = position;
    }
}
