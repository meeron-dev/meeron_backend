package com.cmc.meeron.user.application.port.in.response;

import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyWorkspaceUserResponseDto {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String profileImageUrl;
    private String position;
    private String email;

    public static List<MyWorkspaceUserResponseDto> fromEntities(List<WorkspaceUser> myWorkspaceUsers) {
        return myWorkspaceUsers.stream()
                .map(MyWorkspaceUserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static MyWorkspaceUserResponseDto fromEntity(WorkspaceUser workspaceUser) {
        return MyWorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUser.getId())
                .workspaceId(workspaceUser.getWorkspace().getId())
                .isWorkspaceAdmin(workspaceUser.getWorkspaceUserInfo().isWorkspaceAdmin())
                .nickname(workspaceUser.getWorkspaceUserInfo().getNickname())
                .profileImageUrl(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl())
                .position(workspaceUser.getWorkspaceUserInfo().getPosition())
                .email(workspaceUser.getWorkspaceUserInfo().getContactMail())
                .build();
    }

    public static List<MyWorkspaceUserResponseDto> fromQueryResponseDtos(List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos) {
        return workspaceUserQueryResponseDtos.stream()
                .map(MyWorkspaceUserResponseDto::fromQueryResponseDto)
                .collect(Collectors.toList());
    }

    private static MyWorkspaceUserResponseDto fromQueryResponseDto(WorkspaceUserQueryResponseDto workspaceUserQueryResponseDto) {
        return MyWorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUserQueryResponseDto.getWorkspaceUserId())
                .workspaceId(workspaceUserQueryResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(workspaceUserQueryResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserQueryResponseDto.getNickname())
                .profileImageUrl(workspaceUserQueryResponseDto.getProfileImageUrl())
                .position(workspaceUserQueryResponseDto.getPosition())
                .email(workspaceUserQueryResponseDto.getEmail())
                .build();
    }
}
