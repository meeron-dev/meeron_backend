package com.cmc.meeron.workspaceuser.adapter.in.response;

import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserQueryResponseDto;
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

    public static WorkspaceUsersResponse fromWorkspaceUsers(List<WorkspaceUserQueryResponseDto> myWorkspaceUsers) {
        return WorkspaceUsersResponse.builder()
                .workspaceUsers(myWorkspaceUsers
                        .stream()
                        .map(WorkspaceUserResponse::fromResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
