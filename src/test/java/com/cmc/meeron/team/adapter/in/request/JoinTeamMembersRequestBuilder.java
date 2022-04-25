package com.cmc.meeron.team.adapter.in.request;

import java.util.List;

public class JoinTeamMembersRequestBuilder {

    public static JoinTeamMembersRequest build() {
        return JoinTeamMembersRequest.builder()
                .adminWorkspaceUserId(1L)
                .joinTeamWorkspaceUserIds(List.of(2L, 3L))
                .build();
    }

    public static JoinTeamMembersRequest buildIntegrationSuccessCase() {
        return JoinTeamMembersRequest.builder()
                .adminWorkspaceUserId(2L)
                .joinTeamWorkspaceUserIds(List.of(6L, 7L))
                .build();
    }

    public static JoinTeamMembersRequest buildNotValid() {
        return JoinTeamMembersRequest.builder()
                .build();
    }
}
