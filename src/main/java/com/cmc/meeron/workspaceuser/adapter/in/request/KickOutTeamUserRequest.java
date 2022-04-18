package com.cmc.meeron.workspaceuser.adapter.in.request;

import com.cmc.meeron.workspaceuser.application.port.in.request.KickOutTeamUserRequestDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KickOutTeamUserRequest {

    @NotNull(message = "관리자의 워크스페이스 유저 ID를 입력해주세요.")
    private Long adminWorkspaceUserId;

    public KickOutTeamUserRequestDto toRequestDto(Long kickOutWorkspaceUserId) {
        return KickOutTeamUserRequestDto.builder()
                .adminWorkspaceUserId(adminWorkspaceUserId)
                .kickOutWorkspaceUserId(kickOutWorkspaceUserId)
                .build();
    }
}
