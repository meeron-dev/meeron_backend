package com.cmc.meeron.workspaceuser.adapter.in.request;

import com.cmc.meeron.workspaceuser.application.port.in.request.ModifyWorkspaceUserRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyWorkspaceUserRequest {

    @NotEmpty(message = "닉네임을 5자 이하로 입력해주세요.")
    @Length(min = 1, max = 5, message = "닉네임을 5자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "직책을 5자 이하로 입력해주세요.")
    @Length(min = 1, max = 5, message = "직책을 5자 이하로 입력해주세요.")
    private String position;

    private String email;
    private String phone;

    public ModifyWorkspaceUserRequestDto toRequestDto(Long workspaceUserId,
                                                      MultipartFile file) {
        return ModifyWorkspaceUserRequestDto.builder()
                .workspaceUserId(workspaceUserId)
                .profileImage(file)
                .nickname(nickname)
                .position(position)
                .email(StringUtils.hasText(email) ? email : "")
                .phone(StringUtils.hasText(phone) ? phone : "")
                .build();
    }
}
