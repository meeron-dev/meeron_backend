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
public class MyWorkspaceUsersResponse {

    @Builder.Default
    private List<WorkspaceUserResponse> myWorkspaceUsers = new ArrayList<>();

    public static MyWorkspaceUsersResponse fromWorkspaceUsers(List<WorkspaceUserResponseDto> myWorkspaceUsers) {
        return MyWorkspaceUsersResponse.builder()
                .myWorkspaceUsers(myWorkspaceUsers
                        .stream()
                        .map(WorkspaceUserResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
