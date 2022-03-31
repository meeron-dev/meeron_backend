package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.application.port.out.response.AdminQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import lombok.*;

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
public class MeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private String meetingPurpose;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long operationTeamId;
    private String operationTeamName;
    @Builder.Default
    private List<MeetingAdminsResponseDto> admins = new ArrayList<>();

    public static MeetingResponseDto fromQueryDto(MeetingAndAdminsQueryDto meetingAndAdminsQueryDto) {
        return MeetingResponseDto.builder()
                .meetingId(meetingAndAdminsQueryDto.getMeetingQueryDto().getMeetingId())
                .meetingName(meetingAndAdminsQueryDto.getMeetingQueryDto().getMeetingName())
                .meetingPurpose(meetingAndAdminsQueryDto.getMeetingQueryDto().getMeetingPurpose())
                .meetingDate(meetingAndAdminsQueryDto.getMeetingQueryDto().getMeetingDate())
                .startTime(meetingAndAdminsQueryDto.getMeetingQueryDto().getStartTime())
                .endTime(meetingAndAdminsQueryDto.getMeetingQueryDto().getEndTime())
                .operationTeamId(meetingAndAdminsQueryDto.getMeetingQueryDto().getOperationTeamId())
                .operationTeamName(meetingAndAdminsQueryDto.getMeetingQueryDto().getOperationTeamName())
                .admins(meetingAndAdminsQueryDto.getAdminQueryDtos()
                        .stream()
                        .map(MeetingAdminsResponseDto::fromAdminQueryDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MeetingAdminsResponseDto {

        private Long workspaceUserId;
        private String nickname;

        public static MeetingAdminsResponseDto fromAdminQueryDto(AdminQueryDto adminQueryDto) {
            return MeetingAdminsResponseDto.builder()
                    .workspaceUserId(adminQueryDto.getWorkspaceUserId())
                    .nickname(adminQueryDto.getNickname())
                    .build();
        }
    }
}
