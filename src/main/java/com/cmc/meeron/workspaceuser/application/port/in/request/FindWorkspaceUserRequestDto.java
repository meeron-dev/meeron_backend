package com.cmc.meeron.workspaceuser.application.port.in.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindWorkspaceUserRequestDto {

    private Long workspaceId;
    private String nickname;
}
