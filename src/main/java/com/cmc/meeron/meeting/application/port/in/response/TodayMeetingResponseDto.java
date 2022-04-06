package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.AttendStatus;
import com.cmc.meeron.meeting.domain.Attendee;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
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
public class TodayMeetingResponseDto {

    private ImproveMeetingResponseDto meetingResponseDto;
    private ImproveTeamResponseDto teamResponseDto;
    @Builder.Default
    private List<ImproveWorkspaceUserResponseDto> adminResponseDto = new ArrayList<>();
    @Builder.Default
    private List<ImproveAgendaResponseDto> agendaResponseDtos = new ArrayList<>();
    private ImproveAttendCountResponseDto attendCountResponseDto;

    public static List<TodayMeetingResponseDto> fromEntities(List<Meeting> todayMeetings,
                                                             List<Agenda> agendas,
                                                             List<Attendee> admins,
                                                             List<AttendStatusCountQueryDto> countsQueryDtos) {
        return todayMeetings.stream()
                .map(meeting -> TodayMeetingResponseDto.builder()
                        .meetingResponseDto(ImproveMeetingResponseDto.fromEntity(meeting))
                        .teamResponseDto(ImproveTeamResponseDto.fromEntity(meeting.getTeam()))
                        .adminResponseDto(ImproveWorkspaceUserResponseDto
                                .fromEntities(admins.stream()
                                        .filter(admin -> admin.getMeeting().getId().equals(meeting.getId()))
                                        .collect(Collectors.toList())))
                        .agendaResponseDtos(ImproveAgendaResponseDto
                                .fromEntities(agendas.stream()
                                        .filter(agenda -> agenda.getMeeting().getId().equals(meeting.getId()))
                                        .collect(Collectors.toList())))
                        .attendCountResponseDto(ImproveAttendCountResponseDto
                                .fromCountDtos(countsQueryDtos.stream()
                                        .filter(count -> count.getMeetingId().equals(meeting.getId()))
                                        .collect(Collectors.toList())))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<TodayMeetingResponseDto> empty() {
        return new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveMeetingResponseDto {

        private Long meetingId;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String name;
        private String purpose;
        private String place;

        public static ImproveMeetingResponseDto fromEntity(Meeting meeting) {
            return ImproveMeetingResponseDto.builder()
                    .meetingId(meeting.getId())
                    .startDate(meeting.getMeetingTime().getStartDate())
                    .startTime(meeting.getMeetingTime().getStartTime())
                    .endTime(meeting.getMeetingTime().getEndTime())
                    .name(meeting.getMeetingInfo().getName())
                    .purpose(meeting.getMeetingInfo().getPurpose())
                    .place(meeting.getPlace())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveTeamResponseDto {

        private Long teamId;
        private String teamName;

        public static ImproveTeamResponseDto fromEntity(Team team) {
            return ImproveTeamResponseDto.builder()
                    .teamId(team.getId())
                    .teamName(team.getName())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveWorkspaceUserResponseDto {

        private Long workspaceUserId;
        private Long workspaceId;
        private boolean isWorkspaceAdmin;
        private String nickname;
        private String position;
        private String profileImageUrl;
        private String email;
        private String phone;

        public static List<ImproveWorkspaceUserResponseDto> fromEntities(List<Attendee> admins) {
            return admins.stream()
                    .map(ImproveWorkspaceUserResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }

        private static ImproveWorkspaceUserResponseDto fromEntity(Attendee admin) {
            WorkspaceUser workspaceUser = admin.getWorkspaceUser();
            return ImproveWorkspaceUserResponseDto.builder()
                    .workspaceUserId(workspaceUser.getId())
                    .workspaceId(workspaceUser.getWorkspace().getId())
                    .isWorkspaceAdmin(workspaceUser.isAdmin())
                    .nickname(workspaceUser.getWorkspaceUserInfo().getNickname())
                    .position(workspaceUser.getWorkspaceUserInfo().getPosition())
                    .profileImageUrl(workspaceUser.getWorkspaceUserInfo().getProfileImageUrl())
                    .email(workspaceUser.getWorkspaceUserInfo().getContactMail())
                    .phone(workspaceUser.getWorkspaceUserInfo().getPhone())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveAgendaResponseDto {

        private Long agendaId;
        private int agendaOrder;
        private String agendaName;
        private String agendaResult;

        public static List<ImproveAgendaResponseDto> fromEntities(List<Agenda> agendas) {
            return agendas.stream()
                    .map(ImproveAgendaResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }

        private static ImproveAgendaResponseDto fromEntity(Agenda agenda) {
            return ImproveAgendaResponseDto.builder()
                    .agendaId(agenda.getId())
                    .agendaOrder(agenda.getAgendaOrder())
                    .agendaName(agenda.getName())
                    .agendaResult(agenda.getAgendaResult())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImproveAttendCountResponseDto {

        private int attend;
        private int absent;
        private int unknown;

        public static ImproveAttendCountResponseDto fromCountDtos(List<AttendStatusCountQueryDto> countQueryDtos) {
            int attend = 0;
            int absent = 0;
            int unknown = 0;
            for (AttendStatusCountQueryDto countQueryDto : countQueryDtos) {
                if (countQueryDto.getMeetingStatus().equals(AttendStatus.ATTEND.name())) {
                    attend = countQueryDto.getCount();
                } else if (countQueryDto.getMeetingStatus().equals(AttendStatus.ABSENT.name())) {
                    absent = countQueryDto.getCount();
                } else {
                    unknown = countQueryDto.getCount();
                }
            }
            return ImproveAttendCountResponseDto.builder()
                    .attend(attend)
                    .absent(absent)
                    .unknown(unknown)
                    .build();
        }
    }
}
