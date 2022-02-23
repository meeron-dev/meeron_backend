package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;

import java.util.List;
import java.util.stream.Collectors;

class MeetingApplicationAssembler {

    public static List<TodayMeetingResponseDto> fromMeetings(List<Meeting> todayMeetings) {
        return todayMeetings.stream()
                .map(meeting -> TodayMeetingResponseDto.builder()
                        .meetingId(meeting.getId())
                        .meetingName(meeting.getName())
                        .meetingDate(meeting.getStartDate())
                        .startTime(meeting.getStartTime())
                        .endTime(meeting.getEndTime())
                        // TODO: 2022/02/23 kobeomseok95 팀 매핑 후 올바르게 설정하기
                        .operationTeamId(null)
                        .operationTeamName(null)
                        .meetingStatus(meeting.getMeetingStatus().name())
                        .build())
                .collect(Collectors.toList());
    }
}
