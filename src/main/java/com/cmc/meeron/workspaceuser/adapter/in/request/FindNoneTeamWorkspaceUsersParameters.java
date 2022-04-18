package com.cmc.meeron.workspaceuser.adapter.in.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindNoneTeamWorkspaceUsersParameters {

    @NotNull(message = "워크스페이스 ID를 입력해주세요.")
    private Long workspaceId;
}
