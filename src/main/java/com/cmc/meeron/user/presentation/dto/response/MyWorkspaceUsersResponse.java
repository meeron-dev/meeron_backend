package com.cmc.meeron.user.presentation.dto.response;

import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
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
    private List<MyWorkspaceUserResponse> myWorkspaceUsers = new ArrayList<>();

    public static MyWorkspaceUsersResponse fromWorkspaceUsers(List<MyWorkspaceUserResponseDto> myWorkspaceUsers) {
        return MyWorkspaceUsersResponse.builder()
                .myWorkspaceUsers(myWorkspaceUsers
                        .stream()
                        .map(MyWorkspaceUserResponse::fromWorkspaceUser)
                        .collect(Collectors.toList()))
                .build();
    }
}
