package com.cmc.meeron.team.adapter.in.request;

import com.cmc.meeron.team.application.port.in.request.ModifyTeamNameRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyTeamNameRequest {

    @NotNull(message = "관리자의 워크스페이스 유저 ID를 입력해주세요.")
    private Long adminWorkspaceUserId;

    @NotBlank(message = "팀 이름을 10자 이하로 입력해주세요.")
    @Length(min = 1, max = 10, message = "팀 이름을 10자 이하로 입력해주세요.")
    private String teamName;

    public ModifyTeamNameRequestDto toRequestDto(Long teamId) {
        return ModifyTeamNameRequestDto.builder()
                .name(teamName)
                .teamId(teamId)
                .adminWorkspaceUserId(adminWorkspaceUserId)
                .build();
    }
}
