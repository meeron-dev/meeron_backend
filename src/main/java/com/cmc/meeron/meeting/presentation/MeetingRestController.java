package com.cmc.meeron.meeting.presentation;

import com.cmc.meeron.meeting.application.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.dto.request.DayMeetingsRequestDto;
import com.cmc.meeron.meeting.application.dto.request.MeetingDaysRequestDto;
import com.cmc.meeron.meeting.application.dto.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceAndTeamDayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceUserDayMeetingResponseDto;
import com.cmc.meeron.meeting.presentation.dto.request.DayMeetingsRequest;
import com.cmc.meeron.meeting.presentation.dto.request.MeetingDaysRequest;
import com.cmc.meeron.meeting.presentation.dto.request.MeetingDaysSearchType;
import com.cmc.meeron.meeting.presentation.dto.request.TodayMeetingRequest;
import com.cmc.meeron.meeting.presentation.dto.response.DayMeetingsResponse;
import com.cmc.meeron.meeting.presentation.dto.response.MeetingDaysResponse;
import com.cmc.meeron.meeting.presentation.dto.response.TodayMeetingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingRestController {

    private final MeetingQueryUseCase meetingQueryUseCase;

    @GetMapping("/today")
    @ResponseStatus(HttpStatus.OK)
    public TodayMeetingResponse todayMeetings(@Valid TodayMeetingRequest todayMeetingRequest) {
        List<TodayMeetingResponseDto> todayMeetings = meetingQueryUseCase
                .getTodayMeetings(TodayMeetingRequestDto.of(todayMeetingRequest));
        return TodayMeetingResponse.fromDto(todayMeetings);
    }

    @GetMapping("/days")
    @ResponseStatus(HttpStatus.OK)
    public MeetingDaysResponse getMeetingDays(@Valid MeetingDaysRequest meetingDaysRequest) {
        List<Integer> days = meetingQueryUseCase.getMeetingDays(MeetingDaysRequestDto.of(meetingDaysRequest));
        return MeetingDaysResponse.of(days);
    }

    // FIXME: 2022/03/02 kobeomseok95 refactoring
    @GetMapping("/day")
    @ResponseStatus(HttpStatus.OK)
    public DayMeetingsResponse getDayMeetings(@Valid DayMeetingsRequest dayMeetingsRequest) {
        if (!isWorkspaceUserType(dayMeetingsRequest)) {
            List<WorkspaceAndTeamDayMeetingResponseDto> workspaceAndTeamDayMeetingsResponseDtos =
                    meetingQueryUseCase.getWorkspaceAndTeamDayMeetings(DayMeetingsRequestDto
                            .of(dayMeetingsRequest.getType(), dayMeetingsRequest.getId(), dayMeetingsRequest.getDate()));
            return DayMeetingsResponse.fromWorkspaceAndTeam(workspaceAndTeamDayMeetingsResponseDtos);
        }

        List<WorkspaceUserDayMeetingResponseDto> workspaceUserDayMeetingsResponseDtos =
                meetingQueryUseCase.getWorkspaceUserDayMeetings(DayMeetingsRequestDto
                        .of(dayMeetingsRequest.getType(), dayMeetingsRequest.getId(), dayMeetingsRequest.getDate()));
        return DayMeetingsResponse.fromWorkspaceUser(workspaceUserDayMeetingsResponseDtos);
    }

    private boolean isWorkspaceUserType(DayMeetingsRequest dayMeetingsRequest) {
        return dayMeetingsRequest.getType().equals(MeetingDaysSearchType.WORKSPACE_USER.name());
    }
}
