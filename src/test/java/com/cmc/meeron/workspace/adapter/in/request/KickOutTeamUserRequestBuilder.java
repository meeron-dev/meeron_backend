package com.cmc.meeron.workspace.adapter.in.request;

public class KickOutTeamUserRequestBuilder {

    public static KickOutTeamUserRequest buildInvalid() {
        return KickOutTeamUserRequest.builder()
                .build();
    }

    public static KickOutTeamUserRequest build() {
        return KickOutTeamUserRequest.builder()
                .adminWorkspaceUserId(1L)
                .build();
    }

    public static KickOutTeamUserRequest buildIntegrationSuccessCase() {
        return KickOutTeamUserRequest.builder()
                .adminWorkspaceUserId(2L)
                .build();
    }
}
