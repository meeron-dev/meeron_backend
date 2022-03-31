package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.MeetingResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingResponse {

    private Long meetingId;
    private String meetingName;
    private String meetingPurpose;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long operationTeamId;
    private String operationTeamName;
    @Builder.Default
    private List<MeetingAdminsResponse> admins = new ArrayList<>();

    public static MeetingResponse fromResponseDto(MeetingResponseDto meetingResponseDto) {
        return MeetingResponse.builder()
                .meetingId(meetingResponseDto.getMeetingId())
                .meetingName(meetingResponseDto.getMeetingName())
                .meetingPurpose(meetingResponseDto.getMeetingPurpose())
                .meetingDate(meetingResponseDto.getMeetingDate())
                .startTime(meetingResponseDto.getStartTime())
                .endTime(meetingResponseDto.getEndTime())
                .operationTeamId(meetingResponseDto.getOperationTeamId())
                .operationTeamName(meetingResponseDto.getOperationTeamName())
                .admins(MeetingAdminsResponse.fromResponseDto(meetingResponseDto.getAdmins()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class MeetingAdminsResponse {

        private Long workspaceUserId;
        private String nickname;

        public static List<MeetingAdminsResponse> fromResponseDto(List<MeetingResponseDto.MeetingAdminsResponseDto> adminsResponseDtos) {
            return adminsResponseDtos.stream()
                    .map(admin -> MeetingAdminsResponse.builder()
                            .workspaceUserId(admin.getWorkspaceUserId())
                            .nickname(admin.getNickname())
                            .build())
                    .collect(Collectors.toList());
        }
    }
}
