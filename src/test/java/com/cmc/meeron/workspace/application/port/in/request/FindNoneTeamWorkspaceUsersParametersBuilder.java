package com.cmc.meeron.workspace.application.port.in.request;

import com.cmc.meeron.workspace.adapter.in.request.FindNoneTeamWorkspaceUsersParameters;

public class FindNoneTeamWorkspaceUsersParametersBuilder {

    public static FindNoneTeamWorkspaceUsersParameters build() {
        return FindNoneTeamWorkspaceUsersParameters.builder()
                .workspaceId(1L)
                .build();
    }
}
