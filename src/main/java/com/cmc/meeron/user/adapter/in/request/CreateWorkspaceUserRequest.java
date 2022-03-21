package com.cmc.meeron.user.adapter.in.request;

import com.cmc.meeron.user.application.port.in.request.CreateWorkspaceUserRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkspaceUserRequest {

    @NotNull(message = "워크스페이스 ID를 입력해주세요.")
    private Long workspaceId;

    @NotEmpty(message = "닉네임을 5자 이하로 입력해주세요.")
    @Length(min = 1, max = 5, message = "닉네임을 5자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "직책을 5자 이하로 입력해주세요.")
    @Length(min = 1, max = 5, message = "직책을 5자 이하로 입력해주세요.")
    private String position;

    private String email;
    private String phone;

    public CreateWorkspaceUserRequestDto toAdminRequestDto(MultipartFile file,
                                                           Long userId) {
        return CreateWorkspaceUserRequestDto.builder()
                .workspaceId(workspaceId)
                .userId(userId)
                .profileImage(file)
                .nickname(nickname)
                .position(position)
                .email(StringUtils.hasText(email) ? email : "")
                .phone(StringUtils.hasText(phone) ? phone : "")
                .isAdmin(true)
                .build();
    }

    public CreateWorkspaceUserRequestDto toRequestDto(MultipartFile file,
                                                           Long userId) {
        return CreateWorkspaceUserRequestDto.builder()
                .workspaceId(workspaceId)
                .userId(userId)
                .profileImage(file)
                .nickname(nickname)
                .position(position)
                .email(StringUtils.hasText(email) ? email : "")
                .phone(StringUtils.hasText(phone) ? phone : "")
                .isAdmin(false)
                .build();
    }
}
