package com.cmc.meeron.workspace.adapter.in.request;

public class ModifyWorkspaceUserRequestBuilder {

    public static ModifyWorkspaceUserRequest buildInvalid() {
        return ModifyWorkspaceUserRequest.builder()
                .build();
    }

    public static ModifyWorkspaceUserRequest build() {
        return ModifyWorkspaceUserRequest.builder()
                .email("change@change.com")
                .nickname("변경닉네임")
                .phone("010-1234-1234")
                .position("변경됨")
                .build();
    }
}
