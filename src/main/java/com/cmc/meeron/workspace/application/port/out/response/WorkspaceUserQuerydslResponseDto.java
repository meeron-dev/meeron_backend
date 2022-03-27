package com.cmc.meeron.workspace.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WorkspaceUserQuerydslResponseDto {

    private Long workspaceId;
    private Long workspaceUserId;
    private String profileImageUrl;
    private String nickname;
    private String position;
    private boolean workspaceAdmin;
    private String email;

    @QueryProjection
    public WorkspaceUserQuerydslResponseDto(Long workspaceId, Long workspaceUserId, String profileImageUrl, String nickname, String position, boolean workspaceAdmin, String email) {
        this.workspaceId = workspaceId;
        this.workspaceUserId = workspaceUserId;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.position = position;
        this.workspaceAdmin = workspaceAdmin;
        this.email = email;
    }
}
