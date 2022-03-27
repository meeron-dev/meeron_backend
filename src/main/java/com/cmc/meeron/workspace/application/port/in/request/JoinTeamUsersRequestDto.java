package com.cmc.meeron.workspace.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserIdCheckable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinTeamUsersRequestDto implements WorkspaceUserIdCheckable {

    private Long teamId;
    private Long adminWorkspaceUserId;
    @Builder.Default
    private List<Long> workspaceUserIds = new ArrayList<>();
}
