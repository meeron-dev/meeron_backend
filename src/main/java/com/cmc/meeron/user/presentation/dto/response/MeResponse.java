package com.cmc.meeron.user.presentation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeResponse {

    private Long userId;
    private String loginEmail;
    private String contactEmail;
    private String name;
    private String profileImageUrl;
    private String phone;
}
