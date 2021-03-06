package com.cmc.meeron.workspaceuser.adapter.in.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckDuplicateNicknameResponse {

    private boolean isDuplicate;

    public static CheckDuplicateNicknameResponse isNotDuplicate() {
        return CheckDuplicateNicknameResponse.builder()
                .isDuplicate(false)
                .build();
    }
}
