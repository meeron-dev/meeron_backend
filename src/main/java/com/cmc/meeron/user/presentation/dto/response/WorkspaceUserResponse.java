package com.cmc.meeron.user.presentation.dto.response;

import com.cmc.meeron.user.application.dto.response.WorkspaceUserResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserResponse {

    @Builder.Default
    private List<WorkspaceUser> workspaceUsers = new ArrayList<>();

    public static WorkspaceUserResponse fromList(List<WorkspaceUserResponseDto> workspaceUserResponseDtos) {
        return WorkspaceUserResponse.builder()
                .workspaceUsers(workspaceUserResponseDtos.stream()
                        .map(responseDto -> WorkspaceUser.builder()
                                .workspaceUserId(responseDto.getWorkspaceUserId())
                                .profileImageUrl(responseDto.getProfileImageUrl())
                                .nickname(responseDto.getNickname())
                                .position(responseDto.getPosition())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkspaceUser {

        private Long workspaceUserId;
        private String profileImageUrl;
        private String nickname;
        private String position;
    }
}
