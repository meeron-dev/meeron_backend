package com.cmc.meeron.workspace.adapter.in.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkspaceRequest {

    @NotBlank(message = "워크스페이스 이름을 10자 이하로 입력해주세요.")
    @Length(min = 1, max = 10, message = "워크스페이스 이름을 10자 이하로 입력해주세요.")
    private String name;
}
