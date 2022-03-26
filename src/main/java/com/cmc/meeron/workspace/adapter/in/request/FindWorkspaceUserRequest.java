package com.cmc.meeron.workspace.adapter.in.request;

import com.cmc.meeron.workspace.application.port.in.request.FindWorkspaceUserRequestDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindWorkspaceUserRequest {

    @NotNull(message = "찾을 워크스페이스 ID를 입력해주세요.")
    private Long workspaceId;

    @NotEmpty(message = "검색할 워크스페이스 유저의 닉네임을 입력해주세요.")
    private String nickname;

    public FindWorkspaceUserRequestDto toRequestDto() {
        return FindWorkspaceUserRequestDto.builder()
                .workspaceId(workspaceId)
                .nickname(nickname)
                .build();
    }
}
