package com.cmc.meeron.workspaceuser.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserAuthorityCheckable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KickOutTeamUserRequestDto implements WorkspaceUserAuthorityCheckable {

    private Long kickOutWorkspaceUserId;
    private Long adminWorkspaceUserId;

    @Override
    public Long getWorkspaceUserId() {
        return adminWorkspaceUserId;
    }
}
