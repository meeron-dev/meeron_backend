package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
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
public class TodayMeetingResponse {

    @Builder.Default
    private List<ImproveMeetingsResponse> meetings = new ArrayList<>();

    public static TodayMeetingResponse fromResponseDtos(List<TodayMeetingResponseDto> todayMeetingResponseDtos) {
        return TodayMeetingResponse.builder()
                .meetings(todayMeetingResponseDtos.stream()
                        .map(ImproveMeetingsResponse::fromResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveMeetingsResponse {

        private ImproveMeetingResponse meeting;
        private ImproveTeamResponse team;
        @Builder.Default
        private List<ImproveAgendaResponse> agendas = new ArrayList<>();
        @Builder.Default
        private List<ImproveWorkspaceUserResponse> admins = new ArrayList<>();
        private ImproveAttendCountResponse attendCount;

        public static ImproveMeetingsResponse fromResponseDto(TodayMeetingResponseDto todayMeetingResponseDto) {
            return ImproveMeetingsResponse.builder()
                    .meeting(ImproveMeetingResponse.fromResponseDto(todayMeetingResponseDto.getMeetingResponseDto()))
                    .team(ImproveTeamResponse.fromResponseDto(todayMeetingResponseDto.getTeamResponseDto()))
                    .agendas(ImproveAgendaResponse.fromResponseDtos(todayMeetingResponseDto.getAgendaResponseDtos()))
                    .admins(ImproveWorkspaceUserResponse.fromResponseDtos(todayMeetingResponseDto.getAdminResponseDto()))
                    .attendCount(ImproveAttendCountResponse.fromResponseDto(todayMeetingResponseDto.getAttendCountResponseDto()))
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveMeetingResponse {

        private Long meetingId;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String meetingName;
        private String purpose;
        private String place;

        public static ImproveMeetingResponse fromResponseDto(TodayMeetingResponseDto.ImproveMeetingResponseDto meetingResponseDto) {
            return ImproveMeetingResponse.builder()
                    .meetingId(meetingResponseDto.getMeetingId())
                    .startDate(meetingResponseDto.getStartDate())
                    .startTime(meetingResponseDto.getStartTime())
                    .endTime(meetingResponseDto.getEndTime())
                    .meetingName(meetingResponseDto.getName())
                    .purpose(meetingResponseDto.getPurpose())
                    .place(meetingResponseDto.getPlace())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveTeamResponse {

        private Long teamId;
        private String teamName;

        public static ImproveTeamResponse fromResponseDto(TodayMeetingResponseDto.ImproveTeamResponseDto teamResponseDto) {
            return ImproveTeamResponse.builder()
                    .teamId(teamResponseDto.getTeamId())
                    .teamName(teamResponseDto.getTeamName())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveWorkspaceUserResponse {

        private Long workspaceUserId;
        private boolean isWorkspaceAdmin;
        private String nickname;
        private String position;
        private String profileImageUrl;
        private String email;
        private String phone;

        public static List<ImproveWorkspaceUserResponse> fromResponseDtos(List<TodayMeetingResponseDto.ImproveWorkspaceUserResponseDto> adminResponseDtos) {
            return adminResponseDtos.stream()
                    .map(ImproveWorkspaceUserResponse::fromResponseDto)
                    .collect(Collectors.toList());
        }

        private static ImproveWorkspaceUserResponse fromResponseDto(TodayMeetingResponseDto.ImproveWorkspaceUserResponseDto improveWorkspaceUserResponseDto) {
            return ImproveWorkspaceUserResponse.builder()
                    .workspaceUserId(improveWorkspaceUserResponseDto.getWorkspaceUserId())
                    .isWorkspaceAdmin(improveWorkspaceUserResponseDto.isWorkspaceAdmin())
                    .nickname(improveWorkspaceUserResponseDto.getNickname())
                    .position(improveWorkspaceUserResponseDto.getPosition())
                    .profileImageUrl(improveWorkspaceUserResponseDto.getProfileImageUrl())
                    .email(improveWorkspaceUserResponseDto.getEmail())
                    .phone(improveWorkspaceUserResponseDto.getPhone())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveAgendaResponse {

        private Long agendaId;
        private int agendaOrder;
        private String agendaName;
        private String agendaResult;

        public static List<ImproveAgendaResponse> fromResponseDtos(List<TodayMeetingResponseDto.ImproveAgendaResponseDto> agendaResponseDtos) {
            return agendaResponseDtos.stream()
                    .map(ImproveAgendaResponse::fromResponseDto)
                    .collect(Collectors.toList());
        }

        private static ImproveAgendaResponse fromResponseDto(TodayMeetingResponseDto.ImproveAgendaResponseDto improveAgendaResponseDto) {
            return ImproveAgendaResponse.builder()
                    .agendaId(improveAgendaResponseDto.getAgendaId())
                    .agendaOrder(improveAgendaResponseDto.getAgendaOrder())
                    .agendaName(improveAgendaResponseDto.getAgendaName())
                    .agendaResult(improveAgendaResponseDto.getAgendaResult())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveAttendCountResponse {

        private int attend;
        private int absent;
        private int unknown;

        public static ImproveAttendCountResponse fromResponseDto(TodayMeetingResponseDto.ImproveAttendCountResponseDto attendCountResponseDtos) {
            return ImproveAttendCountResponse.builder()
                    .attend(attendCountResponseDtos.getAttend())
                    .absent(attendCountResponseDtos.getAbsent())
                    .unknown(attendCountResponseDtos.getUnknown())
                    .build();
        }
    }
}
