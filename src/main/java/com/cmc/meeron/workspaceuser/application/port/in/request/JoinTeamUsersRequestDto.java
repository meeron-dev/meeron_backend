package com.cmc.meeron.workspaceuser.application.port.in.request;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserAuthorityCheckable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinTeamUsersRequestDto implements WorkspaceUserAuthorityCheckable {

    private Long teamId;
    private Long adminWorkspaceUserId;
    @Builder.Default
    private List<Long> workspaceUserIds = new ArrayList<>();

    @Override
    public Long getWorkspaceUserId() {
        return adminWorkspaceUserId;
    }
}
