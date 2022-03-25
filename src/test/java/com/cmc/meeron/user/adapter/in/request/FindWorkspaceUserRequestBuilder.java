package com.cmc.meeron.user.adapter.in.request;

public class FindWorkspaceUserRequestBuilder {

    public static FindWorkspaceUserRequest build() {
        return FindWorkspaceUserRequest.builder()
                .workspaceId(1L)
                .nickname("test")
                .build();
    }
}
