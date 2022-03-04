package com.cmc.meeron.workspace.adapter.in.response;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceResponse {

    private Long workspaceId;
    private String workspaceName;
    private String workspaceLogoUrl;

    public static WorkspaceResponse of(WorkspaceResponseDto response) {
        return WorkspaceResponse.builder()
                .workspaceId(response.getWorkspaceId())
                .workspaceName(response.getWorkspaceName())
                .workspaceLogoUrl(response.getWorkspaceLogoUrl())
                .build();
    }
}
