package com.cmc.meeron.support.restdocs;

import com.cmc.meeron.common.type.EnumType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class DocsController {

    @PostMapping("/enums")
    public ApiResponseDto<EnumDocs> findEnums() {

        // TODO: 2022/02/17 kobeomseok95 문서화하고 싶은 Enum들 리턴 후 반환
        return ApiResponseDto.of(null);
    }

    private Map<String, String> getDocs(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .collect(Collectors.toMap(EnumType::getName, EnumType::getDescription));
    }
}
