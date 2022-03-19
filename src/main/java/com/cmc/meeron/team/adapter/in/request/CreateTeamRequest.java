package com.cmc.meeron.team.adapter.in.request;

import com.cmc.meeron.team.application.port.in.request.CreateTeamRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTeamRequest {

    @NotNull(message = "워크스페이스 ID를 입력해주세요.")
    private Long workspaceId;

    @NotBlank(message = "팀 이름을 10자 이하로 입력해주세요.")
    @Length(min = 1, max = 10, message = "팀 이름을 10자 이하로 입력해주세요.")
    private String teamName;

    public CreateTeamRequestDto toRequestDto() {
        return CreateTeamRequestDto.builder()
                .workspaceId(workspaceId)
                .teamName(teamName)
                .build();
    }
}
