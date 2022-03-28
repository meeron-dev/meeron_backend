package com.cmc.meeron.team.adapter.in.request;

import com.cmc.meeron.team.application.port.in.request.DeleteTeamRequestDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteTeamRequest {

    @NotNull(message = "관리자의 워크스페이스 유저 ID를 입력해주세요.")
    private Long adminWorkspaceUserId;

    public DeleteTeamRequestDto toRequestDto(Long teamId) {
        return DeleteTeamRequestDto.builder()
                .teamId(teamId)
                .adminWorkspaceUserId(adminWorkspaceUserId)
                .build();
    }
}
