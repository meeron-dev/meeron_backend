package com.cmc.meeron.workspaceuser.adapter.in.request;

import java.util.List;

public class JoinTeamUsersRequestBuilder {

    public static JoinTeamUsersRequest build() {
        return JoinTeamUsersRequest.builder()
                .adminWorkspaceUserId(1L)
                .joinTeamWorkspaceUserIds(List.of(2L, 3L))
                .build();
    }

    public static JoinTeamUsersRequest buildIntegrationSuccessCase() {
        return JoinTeamUsersRequest.builder()
                .adminWorkspaceUserId(2L)
                .joinTeamWorkspaceUserIds(List.of(6L, 7L))
                .build();
    }

    public static JoinTeamUsersRequest buildNotValid() {
        return JoinTeamUsersRequest.builder()
                .build();
    }
}
