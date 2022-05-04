package com.cmc.meeron.file.application.port.in.response;

import java.util.List;

public class AgendaFileResponseDtoBuilder {

    public static List<AgendaFileResponseDto> buildList() {
        return List.of(
                AgendaFileResponseDto.builder()
                        .fileId(1L)
                        .fileName("원본이미지1.jpeg")
                        .fileUrl("https://test.com/123")
                        .build(),
                AgendaFileResponseDto.builder()
                        .fileId(2L)
                        .fileName("원본이미지2.jpeg")
                        .fileUrl("https://test.com/124")
                        .build()
        );
    }
}
