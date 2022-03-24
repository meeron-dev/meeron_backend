package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.common.exception.meeting.AttendeeDuplicateException;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Attendees {

    @OneToMany(
            mappedBy = "meeting",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Attendee> values = new ArrayList<>();

    public void addAdmins(List<WorkspaceUser> meetingAdmins, Meeting meeting) {
        List<Attendee> admins = Attendee.createMeetingAdmins(meetingAdmins, meeting);
        addAll(admins);
    }

    public void addAttendees(List<WorkspaceUser> attendees, Meeting meeting) {
        checkDuplicateAttendees(attendees);
        List<Attendee> meetingAttendee = Attendee.createAttendees(attendees, meeting);
        addAll(meetingAttendee);
    }

    private void checkDuplicateAttendees(List<WorkspaceUser> attendees) {
        List<Long> attendeesWorkspaceUserIds = attendees.stream().map(WorkspaceUser::getId).collect(Collectors.toList());
        if (containsAttendee(attendeesWorkspaceUserIds)) {
            throw new AttendeeDuplicateException();
        }
    }

    private boolean containsAttendee(List<Long> attendeesWorkspaceUserIds) {
        return values.stream().anyMatch(value -> attendeesWorkspaceUserIds.contains(value.getWorkspaceUser().getId()));
    }

    public void addAll(List<Attendee> attendees) {
        values.addAll(attendees);
    }

    public int size() {
        return values.size();
    }

    public Attendee findByWorkspaceUserId(Long workspaceUserId) {
        return values.stream()
                .filter(attendee -> attendee.equalsWorkspaceUserId(workspaceUserId))
                .findFirst()
                .orElseThrow(AttendeeNotFoundException::new);
    }
}
