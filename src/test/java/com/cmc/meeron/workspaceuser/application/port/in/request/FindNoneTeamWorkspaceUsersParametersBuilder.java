package com.cmc.meeron.workspaceuser.application.port.in.request;

import com.cmc.meeron.workspaceuser.adapter.in.request.FindNoneTeamWorkspaceUsersParameters;

public class FindNoneTeamWorkspaceUsersParametersBuilder {

    public static FindNoneTeamWorkspaceUsersParameters build() {
        return FindNoneTeamWorkspaceUsersParameters.builder()
                .workspaceId(1L)
                .build();
    }
}
