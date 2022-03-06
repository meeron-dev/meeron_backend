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

    public void addAll(List<WorkspaceUser> meetingAdmins, Meeting meeting) {
        List<Attendee> attendees = Attendee.createMeetingAdmins(meetingAdmins, meeting);
        values.addAll(attendees);
    }
}
