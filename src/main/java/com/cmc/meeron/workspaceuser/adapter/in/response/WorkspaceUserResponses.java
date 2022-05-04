package com.cmc.meeron.workspaceuser.adapter.in.response;

import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserResponses {

    @Builder.Default
    private List<WorkspaceUserResponse> workspaceUsers = new ArrayList<>();

    public static WorkspaceUserResponses fromWorkspaceUsers(List<WorkspaceUserResponseDto> myWorkspaceUsers) {
        return WorkspaceUserResponses.builder()
                .workspaceUsers(myWorkspaceUsers
                        .stream()
                        .map(WorkspaceUserResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
