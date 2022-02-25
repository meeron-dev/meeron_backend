package com.cmc.meeron.workspace.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceResponseDto {

    private Long workspaceId;
    private String workspaceName;
    private String workspaceLogoUrl;
}
