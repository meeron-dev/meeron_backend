package com.cmc.meeron.attendee.application.port.in.response;

import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.type.SortableByNickname;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeeResponseDto implements SortableByNickname {

    private Long attendeeId;
    private Long meetingId;
    private String attendStatus;
    private boolean meetingAdmin;
    private AttendeeWorkspaceUserResponseDto attendeeWorkspaceUserResponseDto;

    public static AttendeeResponseDto from(Attendee attendee) {
        return AttendeeResponseDto.builder()
                .attendeeId(attendee.getId())
                .meetingId(attendee.getMeeting().getId())
                .attendStatus(attendee.getAttendStatus().name())
                .meetingAdmin(attendee.isMeetingAdmin())
                .attendeeWorkspaceUserResponseDto(AttendeeWorkspaceUserResponseDto
                        .fromEntity(attendee.getWorkspaceUser()))
                .build();
    }

    public static List<AttendeeResponseDto> from(List<Attendee> attendees) {
        return attendees.stream()
                .map(AttendeeResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public String getNickname() {
        return this.attendeeWorkspaceUserResponseDto.getNickname();
    }
}
