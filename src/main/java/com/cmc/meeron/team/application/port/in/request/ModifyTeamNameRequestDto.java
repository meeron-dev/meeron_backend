package com.cmc.meeron.team.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserAuthorityCheckable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyTeamNameRequestDto implements WorkspaceUserAuthorityCheckable {

    private Long adminWorkspaceUserId;
    private Long teamId;
    private String name;

    @Override
    public Long getWorkspaceUserId() {
        return adminWorkspaceUserId;
    }
}
