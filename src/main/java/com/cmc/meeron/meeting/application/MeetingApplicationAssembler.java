package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceAndTeamDayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceUserDayMeetingResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.util.LocalDateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

class MeetingApplicationAssembler {

    static List<TodayMeetingResponseDto> toTodayMeetingResponseDtos(List<Meeting> todayMeetings) {
        return todayMeetings.stream()
                .map(meeting -> TodayMeetingResponseDto.builder()
                        .meetingId(meeting.getId())
                        .meetingName(meeting.getName())
                        .meetingDate(meeting.getStartDate())
                        .startTime(meeting.getStartTime())
                        .endTime(meeting.getEndTime())
                        .operationTeamId(meeting.getTeam().getId())
                        .operationTeamName(meeting.getTeam().getName())
                        .meetingStatus(meeting.getMeetingStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    static List<WorkspaceAndTeamDayMeetingResponseDto> toWorkspaceAndTeamMeetingResponseDtos(List<Meeting> dayMeetings) {
        return dayMeetings.stream()
                .map(meeting -> WorkspaceAndTeamDayMeetingResponseDto.builder()
                        .meetingId(meeting.getId())
                        .meetingName(meeting.getName())
                        .startTime(LocalDateTimeUtil.convertTime(meeting.getStartTime()))
                        .endTime(LocalDateTimeUtil.convertTime(meeting.getEndTime()))
                        .build())
                .collect(Collectors.toList());
    }

    static List<WorkspaceUserDayMeetingResponseDto> toWorkspaceUserDayMeetingResponseDtos(List<Meeting> dayMeetings) {
        return dayMeetings.stream()
                .map(meeting -> WorkspaceUserDayMeetingResponseDto.builder()
                        .meetingId(meeting.getId())
                        .meetingName(meeting.getName())
                        .startTime(LocalDateTimeUtil.convertTime(meeting.getStartTime()))
                        .endTime(LocalDateTimeUtil.convertTime(meeting.getEndTime()))
                        .workspaceId(meeting.getWorkspace().getId())
                        .workspaceName(meeting.getWorkspace().getName())
                        .build())
                .collect(Collectors.toList());
    }
}
