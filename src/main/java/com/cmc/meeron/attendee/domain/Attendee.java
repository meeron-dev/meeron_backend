package com.cmc.meeron.attendee.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.common.exception.meeting.NotMeetingAdminException;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Attendee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTENDEE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEETING_ID", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_USER_ID", nullable = false)
    private WorkspaceUser workspaceUser;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AttendStatus attendStatus;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isMeetingAdmin;

    public static List<Attendee> createMeetingAdmins(List<WorkspaceUser> meetingAdmins, Meeting meeting) {
        return meetingAdmins.stream()
                .map(admin -> Attendee.createAttendee(admin, meeting, true, AttendStatus.ATTEND))
                .collect(Collectors.toList());
    }

    private static Attendee createAttendee(WorkspaceUser workspaceUser,
                                           Meeting meeting,
                                           boolean isAdmin,
                                           AttendStatus attendStatus) {
        return Attendee.builder()
                .workspaceUser(workspaceUser)
                .meeting(meeting)
                .isMeetingAdmin(isAdmin)
                .attendStatus(attendStatus)
                .build();
    }

    public static List<Attendee> createAttendees(List<WorkspaceUser> attendees, Meeting meeting) {
        return attendees.stream()
                .map(attendee -> Attendee.createAttendee(attendee, meeting, false, AttendStatus.UNKNOWN))
                .collect(Collectors.toList());
    }

    public void changeStatus(AttendStatus status) {
        this.attendStatus = status;
    }

    public boolean equalsWorkspaceUserId(Long workspaceUserId) {
        return workspaceUser.getId()
                .equals(workspaceUserId);
    }

    public void isAdminOrThrow() {
        if (!isMeetingAdmin) {
            throw new NotMeetingAdminException();
        }
    }
}
