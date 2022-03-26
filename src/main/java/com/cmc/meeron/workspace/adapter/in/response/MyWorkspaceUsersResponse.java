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
public class MyWorkspaceUsersResponse {

    @Builder.Default
    private List<WorkspaceUserResponse> myWorkspaceUsers = new ArrayList<>();

    public static MyWorkspaceUsersResponse fromWorkspaceUsers(List<MyWorkspaceUserResponseDto> myWorkspaceUsers) {
        return MyWorkspaceUsersResponse.builder()
                .myWorkspaceUsers(myWorkspaceUsers
                        .stream()
                        .map(WorkspaceUserResponse::fromWorkspaceUser)
                        .collect(Collectors.toList()))
                .build();
    }
}
