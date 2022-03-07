package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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
        List<Attendee> meetingAttendee = Attendee.createAttendees(attendees, meeting);
        addAll(meetingAttendee);
    }

    public void addAll(List<Attendee> attendees) {
        values.addAll(attendees);
    }

    public int size() {
        return values.size();
    }
}
