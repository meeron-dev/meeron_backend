package com.cmc.meeron.team.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserAuthorityCheckable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteTeamRequestDto implements WorkspaceUserAuthorityCheckable {

    private Long adminWorkspaceUserId;
    private Long teamId;

    @Override
    public Long getWorkspaceUserId() {
        return adminWorkspaceUserId;
    }
}
