package com.cmc.meeron.workspace.adapter.in.request;

import com.cmc.meeron.workspace.application.port.in.request.JoinTeamUsersRequestDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinTeamUsersRequest {

    @NotNull(message = "관리자의 워크스페이스 유저 ID를 입력해주세요.")
    private Long adminWorkspaceUserId;

    @NotEmpty(message = "팀에 들어갈 워크스페이스 유저의 ID를 최소 하나 이상 입력해주세요.")
    private List<Long> joinTeamWorkspaceUserIds;

    public JoinTeamUsersRequestDto toRequestDto(Long teamId) {
        return JoinTeamUsersRequestDto.builder()
                .teamId(teamId)
                .adminWorkspaceUserId(adminWorkspaceUserId)
                .workspaceUserIds(joinTeamWorkspaceUserIds)
                .build();
    }
}
