package com.cmc.meeron.team.adapter.in.request;

public class ModifyTeamNameRequestBuilder {

    public static ModifyTeamNameRequest notValid() {
        return ModifyTeamNameRequest.builder()
                .teamName("            ")
                .build();
    }

    public static ModifyTeamNameRequest build() {
        return ModifyTeamNameRequest.builder()
                .adminWorkspaceUserId(1L)
                .teamName("변경할팀명")
                .build();
    }

    public static ModifyTeamNameRequest buildIntegrationSuccessCase() {
        return ModifyTeamNameRequest.builder()
                .adminWorkspaceUserId(2L)
                .teamName("변경할팀명")
                .build();
    }
}
