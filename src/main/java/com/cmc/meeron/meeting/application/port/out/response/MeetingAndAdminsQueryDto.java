package com.cmc.meeron.meeting.application.port.out.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAndAdminsQueryDto {

    private MeetingQueryDto meetingQueryDto;
    private List<AdminQueryDto> adminQueryDtos;

    public static MeetingAndAdminsQueryDto fromQueryDto(MeetingQueryDto meetingQueryDto, List<AdminQueryDto> adminQueryDtos) {
        return MeetingAndAdminsQueryDto.builder()
                .meetingQueryDto(meetingQueryDto)
                .adminQueryDtos(adminQueryDtos)
                .build();
    }
}
