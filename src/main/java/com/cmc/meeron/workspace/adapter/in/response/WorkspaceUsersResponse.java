package com.cmc.meeron.workspace.adapter.in.response;

import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUsersResponse {

    @Builder.Default
    private List<WorkspaceUserResponse> workspaceUsers = new ArrayList<>();

    public static WorkspaceUsersResponse fromWorkspaceUsers(List<MyWorkspaceUserResponseDto> myWorkspaceUsers) {
        return WorkspaceUsersResponse.builder()
                .workspaceUsers(myWorkspaceUsers
                        .stream()
                        .map(WorkspaceUserResponse::fromWorkspaceUser)
                        .collect(Collectors.toList()))
                .build();
    }
}
