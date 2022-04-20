package com.cmc.meeron.attendee.adapter.in.response;

import com.cmc.meeron.attendee.application.port.in.response.AttendeeResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.AttendeeWorkspaceUserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeeWorkspaceUserResponse {

    private Long attendeeId;
    private Long meetingId;
    private String attendStatus;
    private boolean meetingAdmin;
    private WorkspaceUserResponse workspaceUser;

    public static AttendeeWorkspaceUserResponse from(AttendeeResponseDto attendeeResponseDto) {
        return AttendeeWorkspaceUserResponse.builder()
                .attendeeId(attendeeResponseDto.getAttendeeId())
                .meetingId(attendeeResponseDto.getMeetingId())
                .attendStatus(attendeeResponseDto.getAttendStatus())
                .meetingAdmin(attendeeResponseDto.isMeetingAdmin())
                .workspaceUser(WorkspaceUserResponse.from(attendeeResponseDto.getAttendeeWorkspaceUserResponseDto()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkspaceUserResponse {

        private Long workspaceUserId;
        private Long workspaceId;
        private boolean workspaceAdmin;
        private String nickname;
        private String position;
        private String profileImageUrl;
        private String email;
        private String phone;

        public static WorkspaceUserResponse from(AttendeeWorkspaceUserResponseDto attendeeWorkspaceUserResponseDto) {
            return WorkspaceUserResponse.builder()
                    .workspaceUserId(attendeeWorkspaceUserResponseDto.getWorkspaceUserId())
                    .workspaceId(attendeeWorkspaceUserResponseDto.getWorkspaceId())
                    .workspaceAdmin(attendeeWorkspaceUserResponseDto.isWorkspaceAdmin())
                    .nickname(attendeeWorkspaceUserResponseDto.getNickname())
                    .position(attendeeWorkspaceUserResponseDto.getPosition())
                    .profileImageUrl(attendeeWorkspaceUserResponseDto.getProfileImageUrl())
                    .email(attendeeWorkspaceUserResponseDto.getEmail())
                    .phone(attendeeWorkspaceUserResponseDto.getPhone())
                    .build();
        }
    }
}
