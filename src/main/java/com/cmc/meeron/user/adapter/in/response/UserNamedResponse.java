package com.cmc.meeron.user.adapter.in.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNamedResponse {

    private boolean isNamed;

    public static UserNamedResponse of(boolean isNamed) {
        return UserNamedResponse.builder()
                .isNamed(isNamed)
                .build();
    }
}
