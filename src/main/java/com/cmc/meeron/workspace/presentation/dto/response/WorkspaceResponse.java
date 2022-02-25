package com.cmc.meeron.workspace.presentation.dto.response;

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
}
