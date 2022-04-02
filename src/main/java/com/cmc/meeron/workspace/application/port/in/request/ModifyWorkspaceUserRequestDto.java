package com.cmc.meeron.workspace.application.port.in.request;

import com.cmc.meeron.workspace.domain.WorkspaceUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyWorkspaceUserRequestDto {

    private Long workspaceUserId;
    private MultipartFile profileImage;
    private String nickname;
    private String position;
    private String email;
    private String phone;

    public WorkspaceUserInfo toWorkspaceUserInfo(String imageUrl) {
        return WorkspaceUserInfo.builder()
                .profileImageUrl(imageUrl)
                .nickname(nickname)
                .position(position)
                .contactMail(email)
                .phone(phone)
                .build();
    }
}
