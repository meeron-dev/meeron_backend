package com.cmc.meeron.user.application.port.in.response;

import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserResponseDto {

    private Long workspaceUserId;
    private String profileImageUrl;
    private String nickname;
    private String position;

    public static List<WorkspaceUserResponseDto> ofList(List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos) {
        return workspaceUserQueryResponseDtos.stream()
                .map(WorkspaceUserResponseDto::of)
                .collect(Collectors.toList());
    }

    private static WorkspaceUserResponseDto of(WorkspaceUserQueryResponseDto workspaceUserQueryResponseDto) {
        return WorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUserQueryResponseDto.getWorkspaceUserId())
                .profileImageUrl(workspaceUserQueryResponseDto.getProfileImageUrl())
                .nickname(workspaceUserQueryResponseDto.getNickname())
                .position(workspaceUserQueryResponseDto.getPosition())
                .build();
    }
}
