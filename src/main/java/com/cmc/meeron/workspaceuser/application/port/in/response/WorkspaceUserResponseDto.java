package com.cmc.meeron.workspaceuser.application.port.in.response;

import com.cmc.meeron.common.type.SortableByNickname;
import com.cmc.meeron.common.util.NicknameOrderByKoreanEnglishNumberSpecial;
import com.cmc.meeron.workspaceuser.application.port.out.response.WorkspaceUserQuerydslResponseDto;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserResponseDto implements SortableByNickname {

    private Long workspaceUserId;
    private Long workspaceId;
    private boolean isWorkspaceAdmin;
    private String nickname;
    private String profileImageUrl;
    private String position;
    private String email;
    private String phone;

    public static List<WorkspaceUserResponseDto> fromEntities(List<WorkspaceUser> myWorkspaceUsers) {
        List<WorkspaceUserResponseDto> responseDtos = myWorkspaceUsers.stream()
                .map(WorkspaceUserResponseDto::from)
                .collect(Collectors.toList());
        return sortByNickname(responseDtos);
    }

    private static List<WorkspaceUserResponseDto> sortByNickname(List<WorkspaceUserResponseDto> responseDto) {
        responseDto.sort(NicknameOrderByKoreanEnglishNumberSpecial.getComparator());
        return responseDto;
    }

    public static WorkspaceUserResponseDto from(WorkspaceUser workspaceUser) {
        return WorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUser.getId())
                .workspaceId(workspaceUser.getWorkspace().getId())
                .isWorkspaceAdmin(workspaceUser.getWorkspaceUserInfo().isWorkspaceAdmin())
                .nickname(workspaceUser.getWorkspaceUserInfo().getNickname())
                .profileImageUrl(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl())
                .position(workspaceUser.getWorkspaceUserInfo().getPosition())
                .email(workspaceUser.getWorkspaceUserInfo().getContactMail())
                .phone(workspaceUser.getWorkspaceUserInfo().getPhone())
                .build();
    }

    public static List<WorkspaceUserResponseDto> from(List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos) {
        List<WorkspaceUserResponseDto> responseDtos = workspaceUserQuerydslResponseDtos.stream()
                .map(WorkspaceUserResponseDto::from)
                .collect(Collectors.toList());
        return sortByNickname(responseDtos);
    }

    private static WorkspaceUserResponseDto from(WorkspaceUserQuerydslResponseDto workspaceUserQuerydslResponseDto) {
        return WorkspaceUserResponseDto.builder()
                .workspaceUserId(workspaceUserQuerydslResponseDto.getWorkspaceUserId())
                .workspaceId(workspaceUserQuerydslResponseDto.getWorkspaceId())
                .isWorkspaceAdmin(workspaceUserQuerydslResponseDto.isWorkspaceAdmin())
                .nickname(workspaceUserQuerydslResponseDto.getNickname())
                .profileImageUrl(workspaceUserQuerydslResponseDto.getProfileImageUrl())
                .position(workspaceUserQuerydslResponseDto.getPosition())
                .email(workspaceUserQuerydslResponseDto.getEmail())
                .phone(workspaceUserQuerydslResponseDto.getPhone())
                .build();
    }
}
