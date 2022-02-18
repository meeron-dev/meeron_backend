package com.cmc.meeron.support.restdocs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
//@Builder
public class ApiResponseDto<T> {

    private T data;

//    public ApiResponseDto(T data) {
//        this.data = data;
//    }

//    public static <T> ApiResponseDto<T> of(T data) {
//        return new ApiResponseDto<>();
//    }
}
