package com.cmc.meeron.workspace.application.port.in.response;

import com.cmc.meeron.common.type.SortableByNickname;
import com.cmc.meeron.common.util.NicknameOrderByKoreanEnglishNumberSpecial;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQuerydslResponseDto;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserQueryResponseDto implements SortableByNickname {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String profileImageUrl;
    private String position;
    private String email;

    public static List<WorkspaceUserQueryResponseDto> fromEntities(List<WorkspaceUser> myWorkspaceUsers) {
        List<WorkspaceUserQueryResponseDto> responseDtos = myWorkspaceUsers.stream()
                .map(WorkspaceUserQueryResponseDto::fromEntity)
                .collect(Collectors.toList());
        return sortByNickname(responseDtos);
    }

    private static List<WorkspaceUserQueryResponseDto> sortByNickname(List<WorkspaceUserQueryResponseDto> responseDto) {
        responseDto.sort(NicknameOrderByKoreanEnglishNumberSpecial.getComparator());
        return responseDto;
    }

    public static WorkspaceUserQueryResponseDto fromEntity(WorkspaceUser workspaceUser) {
        return WorkspaceUserQueryResponseDto.builder()
                .workspaceUserId(workspaceUser.getId())
                .workspaceId(workspaceUser.getWorkspace().getId())
                .isWorkspaceAdmin(workspaceUser.getWorkspaceUserInfo().isWorkspaceAdmin())
                .nickname(workspaceUser.getWorkspaceUserInfo().getNickname())
                .profileImageUrl(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl())
                .position(workspaceUser.getWorkspaceUserInfo().getPosition())
                .email(workspaceUser.getWorkspaceUserInfo().getContactMail())
                .build();
    }

    public static List<WorkspaceUserQueryResponseDto> fromQueryResponseDtos(List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos) {
        List<WorkspaceUserQueryResponseDto> responseDtos = workspaceUserQuerydslResponseDtos.stream()
                .map(WorkspaceUserQueryResponseDto::fromQueryResponseDto)
                .collect(Collectors.toList());
        return sortByNickname(responseDtos);
    }

    private static WorkspaceUserQueryResponseDto fromQueryResponseDto(WorkspaceUserQuerydslResponseDto workspaceUserQuerydslResponseDto) {
        return WorkspaceUserQueryResponseDto.builder()
                .workspaceUserId(workspaceUserQuerydslResponseDto.getWorkspaceUserId())
                .workspaceId(workspaceUserQuerydslResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(workspaceUserQuerydslResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserQuerydslResponseDto.getNickname())
                .profileImageUrl(workspaceUserQuerydslResponseDto.getProfileImageUrl())
                .position(workspaceUserQuerydslResponseDto.getPosition())
                .email(workspaceUserQuerydslResponseDto.getEmail())
                .build();
    }
}
