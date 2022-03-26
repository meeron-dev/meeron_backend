package com.cmc.meeron.user.adapter.in.request;

import com.cmc.meeron.workspace.adapter.in.request.FindWorkspaceUserRequest;

public class FindWorkspaceUserRequestBuilder {

    public static FindWorkspaceUserRequest build() {
        return FindWorkspaceUserRequest.builder()
                .workspaceId(1L)
                .nickname("test")
                .build();
    }
}
