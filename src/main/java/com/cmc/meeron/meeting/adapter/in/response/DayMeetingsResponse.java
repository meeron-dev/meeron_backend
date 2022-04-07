package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayMeetingsResponse {

    @Builder.Default
    private List<DayMeeting> meetings = new ArrayList<>();

    public static DayMeetingsResponse fromResponseDtos(List<DayMeetingResponseDto> responseDtos) {
        return DayMeetingsResponse.builder()
                .meetings(responseDtos.stream()
                        .map(dto -> DayMeeting.builder()
                                .meetingId(dto.getMeetingId())
                                .startDate(dto.getStartDate())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .meetingName(dto.getMeetingName())
                                .purpose(dto.getPurpose())
                                .place(dto.getPlace())
                                .workspaceId(dto.getWorkspaceId() != null ? dto.getWorkspaceId() : 0L)
                                .workspaceName(StringUtils.hasText(dto.getWorkspaceName())
                                        ? dto.getWorkspaceName()
                                        : "")
                                .workspaceLogoUrl(StringUtils.hasText(dto.getWorkspaceLogoUrl())
                                        ? dto.getWorkspaceLogoUrl()
                                        : "")
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DayMeeting {

        private Long meetingId;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String meetingName;
        private String purpose;
        private String place;

        private Long workspaceId;
        private String workspaceName;
        private String workspaceLogoUrl;
    }
}
