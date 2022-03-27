package com.cmc.meeron.workspace.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserIdCheckable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KickOutTeamUserRequestDto implements WorkspaceUserIdCheckable {

    private Long kickOutWorkspaceUserId;
    private Long adminWorkspaceUserId;
}
