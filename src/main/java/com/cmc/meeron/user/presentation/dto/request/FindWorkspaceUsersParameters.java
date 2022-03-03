package com.cmc.meeron.user.presentation.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindWorkspaceUsersParameters {

    @NotNull(message = "찾을 워크스페이스 ID를 입력해주세요.")
    private Long workspaceId;

    @NotEmpty(message = "검색할 워크스페이스 유저의 닉네임을 입력해주세요.")
    private String nickname;
}
