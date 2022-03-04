package com.cmc.meeron.workspace.application.port.in.response;

import com.cmc.meeron.workspace.domain.Workspace;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceResponseDto {

    private Long workspaceId;
    private String workspaceName;
    private String workspaceLogoUrl;

    public static List<WorkspaceResponseDto> ofList(List<Workspace> workspaces) {
        return workspaces.stream()
                .map(WorkspaceResponseDto::of)
                .collect(Collectors.toList());
    }

    public static WorkspaceResponseDto of(Workspace workspace) {
        return WorkspaceResponseDto.builder()
                .workspaceId(workspace.getId())
                .workspaceName(workspace.getName())
                .workspaceLogoUrl(workspace.getWorkspaceLogoUrl())
                .build();
    }
}
