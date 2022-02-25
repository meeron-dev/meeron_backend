package com.cmc.meeron.workspace.presentation.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWorkspacesResponse {

    @Builder.Default
    private List<WorkspaceResponse> myWorkspaces = new ArrayList<>();
}
