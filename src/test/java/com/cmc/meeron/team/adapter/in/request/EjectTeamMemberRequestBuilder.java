package com.cmc.meeron.team.adapter.in.request;

public class EjectTeamMemberRequestBuilder {

    public static EjectTeamMemberRequest buildInvalid() {
        return EjectTeamMemberRequest.builder()
                .build();
    }

    public static EjectTeamMemberRequest build() {
        return EjectTeamMemberRequest.builder()
                .adminWorkspaceUserId(1L)
                .build();
    }

    public static EjectTeamMemberRequest buildIntegrationSuccessCase() {
        return EjectTeamMemberRequest.builder()
                .adminWorkspaceUserId(2L)
                .build();
    }

    public static EjectTeamMemberRequestV2 buildInvalidV2() {
        return EjectTeamMemberRequestV2.builder()
                .build();
    }

    public static EjectTeamMemberRequestV2 buildV2() {
        return EjectTeamMemberRequestV2.builder()
                .adminWorkspaceUserId(1L)
                .ejectWorkspaceUserId(1L)
                .build();
    }

    public static EjectTeamMemberRequestV2 buildV2InvalidIntegrationTest() {
        return EjectTeamMemberRequestV2.builder()
                .adminWorkspaceUserId(1L)
                .ejectWorkspaceUserId(1L)
                .build();
    }

    public static EjectTeamMemberRequestV2 buildV2SuccessIntegrationTest() {
        return EjectTeamMemberRequestV2.builder()
                .adminWorkspaceUserId(2L)
                .ejectWorkspaceUserId(14L)
                .build();
    }
}
