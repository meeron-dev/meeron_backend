package com.cmc.meeron.team.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserAuthorityCheckable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjectTeamMemberRequestDto implements WorkspaceUserAuthorityCheckable {

    private Long ejectWorkspaceUserId;
    private Long adminWorkspaceUserId;

    @Override
    public Long getWorkspaceUserId() {
        return adminWorkspaceUserId;
    }
}
