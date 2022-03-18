package com.cmc.meeron.user.adapter.in.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetNameRequest {

    @NotBlank(message = "성함을 입력해주세요.")
    @Length(min = 1, max = 5, message = "성함은 5자 이하로 작성해주세요.")
    private String name;
}
