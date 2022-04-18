package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE MEETING SET DELETED = true WHERE MEETING_ID=?")
@Where(clause = "DELETED=false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Meeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEETING_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    @Embedded
    private MeetingTime meetingTime;

    @Embedded
    private Attendees attendees;

    @Embedded
    private MeetingInfo meetingInfo;

    @Column(length = 200)
    private String place;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean deleted;

    public static Meeting create(Team operationTeam,
                                 Workspace workspace,
                                 MeetingTime meetingTime,
                                 MeetingInfo meetingInfo) {
        return Meeting.builder()
                .team(operationTeam)
                .workspace(workspace)
                .meetingTime(meetingTime)
                .meetingInfo(meetingInfo)
                .build();
    }

    public void addAdmins(List<WorkspaceUser> meetingAdmins) {
        ifAttendeesNull();
        attendees.addAdmins(meetingAdmins, this);
    }

    private void ifAttendeesNull() {
        if (attendees == null) {
            attendees = new Attendees();
        }
    }

    public void addAttendees(List<WorkspaceUser> attendees) {
        ifAttendeesNull();
        this.attendees.addAttendees(attendees, this);
    }
}
