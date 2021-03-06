package com.cmc.meeron.meeting.application.port.in.request;

import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateMeetingRequestDto {

    private Long workspaceId;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String meetingName;
    private String meetingPurpose;
    private Long operationTeamId;
    @Builder.Default
    private List<Long> meetingAdminIds = new ArrayList<>();

    public MeetingTime createMeetingTime() {
        return MeetingTime.builder()
                .startDate(startDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public MeetingInfo createMeetingInfo() {
        return MeetingInfo.builder()
                .name(meetingName)
                .purpose(meetingPurpose)
                .build();
    }
}
