package com.cmc.meeron.team.adapter.in.request;

import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjectTeamMemberRequestV2 {

    @NotNull(message = "관리자의 워크스페이스 유저 ID를 입력해주세요.")
    private Long adminWorkspaceUserId;
    @NotNull(message = "추방할 워크스페이스 유저 ID를 입력해주세요.")
    private Long ejectWorkspaceUserId;

    public EjectTeamMemberRequestDto toRequestDto() {
        return EjectTeamMemberRequestDto.builder()
                .adminWorkspaceUserId(adminWorkspaceUserId)
                .ejectWorkspaceUserId(ejectWorkspaceUserId)
                .build();
    }
}
