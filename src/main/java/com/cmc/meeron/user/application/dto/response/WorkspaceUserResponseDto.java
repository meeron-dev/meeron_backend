package com.cmc.meeron.user.application.dto.response;

import com.cmc.meeron.user.domain.dto.WorkspaceUserQueryResponseDto;
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

    public static List<WorkspaceUserResponseDto> toList(List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos) {
        return workspaceUserQueryResponseDtos.stream()
                .map(queryResponse -> WorkspaceUserResponseDto.builder()
                        .workspaceUserId(queryResponse.getWorkspaceUserId())
                        .profileImageUrl(queryResponse.getProfileImageUrl())
                        .nickname(queryResponse.getNickname())
                        .position(queryResponse.getPosition())
                        .build())
                .collect(Collectors.toList());
    }
}
