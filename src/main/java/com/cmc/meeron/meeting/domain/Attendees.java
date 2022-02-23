package com.cmc.meeron.meeting.domain;

import lombok.*;

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
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Attendee> values = new ArrayList<>();
}
