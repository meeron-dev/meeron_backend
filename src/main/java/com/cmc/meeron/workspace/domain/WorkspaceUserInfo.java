package com.cmc.meeron.workspace.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class WorkspaceUserInfo {

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isWorkspaceAdmin;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(length = 20)
    private String position;

    @Column(length = 200)
    private String profileImageUrl;

    @Column(length = 200)
    private String contactMail;

    @Column(length = 20)
    private String phone;
}
