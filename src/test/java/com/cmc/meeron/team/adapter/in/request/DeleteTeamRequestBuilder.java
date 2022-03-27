package com.cmc.meeron.team.adapter.in.request;

public class DeleteTeamRequestBuilder {

    public static DeleteTeamRequest build() {
        return DeleteTeamRequest.builder()
                .adminWorkspaceUserId(1L)
                .build();
    }

    public static DeleteTeamRequest buildIntegrationSuccessCase() {
        return DeleteTeamRequest.builder()
                .adminWorkspaceUserId(2L)
                .build();
    }

    public static DeleteTeamRequest buildNotValid() {
        return DeleteTeamRequest.builder()
                .build();
    }
}
