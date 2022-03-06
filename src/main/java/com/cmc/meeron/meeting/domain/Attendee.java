package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.user.domain.WorkspaceUser;
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
    @Column(nullable = false, length = 20)
    private AttendStatus attendStatus;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isMeetingAdmin;

    public static List<Attendee> createMeetingAdmins(List<WorkspaceUser> meetingAdmins, Meeting meeting) {
        return meetingAdmins.stream()
                .map(admin -> Attendee.createMeetingAdmin(admin, meeting))
                .collect(Collectors.toList());
    }

    private static Attendee createMeetingAdmin(WorkspaceUser workspaceUser, Meeting meeting) {
        return Attendee.builder()
                .workspaceUser(workspaceUser)
                .meeting(meeting)
                .attendStatus(AttendStatus.ATTEND)
                .isMeetingAdmin(true)
                .build();
    }
}
