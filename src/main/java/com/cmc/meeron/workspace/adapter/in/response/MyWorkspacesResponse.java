package com.cmc.meeron.workspace.adapter.in.response;

import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWorkspacesResponse {

    @Builder.Default
    private List<WorkspaceResponse> myWorkspaces = new ArrayList<>();

    public static MyWorkspacesResponse of(List<WorkspaceResponseDto> myWorkspaces) {
        return MyWorkspacesResponse.builder()
                .myWorkspaces(myWorkspaces.stream()
                        .map(WorkspaceResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
