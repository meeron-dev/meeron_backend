package com.cmc.meeron.user.application.port.in.request;

import com.cmc.meeron.user.domain.WorkspaceUserInfo;
import lombok.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkspaceUserRequestDto {

    private Long workspaceId;
    private Long userId;
    private MultipartFile profileImage;
    private String nickname;
    private String position;
    private String email;
    private String phone;
    private boolean isAdmin;

    public String getOriginalFilename() {
        return profileImage.getOriginalFilename();
    }

    public WorkspaceUserInfo toWorkspaceUserInfo(String profileImageUrl) {
        return WorkspaceUserInfo.builder()
                .position(position)
                .nickname(nickname)
                .contactMail(StringUtils.hasText(email) ? email : "")
                .phone(StringUtils.hasText(phone) ? phone : "")
                .profileImageUrl(profileImageUrl)
                .isWorkspaceAdmin(isAdmin)
                .build();
    }
}
