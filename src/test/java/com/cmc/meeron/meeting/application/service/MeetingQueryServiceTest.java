package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.MeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingQueryServiceTest {

    @Mock
    MeetingQueryPort meetingQueryPort;
    @Mock
    AttendeeQueryPort attendeeQueryPort;
    @InjectMocks
    MeetingQueryService meetingQueryService;

    @DisplayName("오늘 예정된 회의 가져오기 - 성공")
    @Test
    void today_expected_meeting_success() throws Exception {

        // given
        TodayMeetingRequestDto request = TodayMeetingRequestDtoBuilder.build();
        List<TodayMeetingsQueryDto> responseDtos = TodayMeetingsQueryDtoBuilder.buildList();
        when(meetingQueryPort.findTodayMeetings(any(), any()))
                .thenReturn(responseDtos);
        List<AttendStatusCountQueryDto> countResponseDtos = AttendStatusCountResponseDtoBuilder.buildList();
        when(attendeeQueryPort.countAttendStatusByMeetingIds(any()))
                .thenReturn(countResponseDtos);

        // when
        List<TodayMeetingResponseDto> result = meetingQueryService.getTodayMeetings(request);

        // then
        TodayMeetingResponseDto one = result.stream().filter(res -> res.getMeetingId().equals(1L)).findFirst().orElseThrow();
        TodayMeetingResponseDto two = result.stream().filter(res -> res.getMeetingId().equals(2L)).findFirst().orElseThrow();
        assertAll(
                () -> verify(meetingQueryPort).findTodayMeetings(request.getWorkspaceId(), request.getWorkspaceUserId()),
                () -> verify(attendeeQueryPort).countAttendStatusByMeetingIds(responseDtos
                        .stream()
                        .map(TodayMeetingsQueryDto::getMeetingId)
                        .collect(Collectors.toList())),
                () -> assertEquals(responseDtos.size(), result.size()),
                () -> assertEquals(3, one.getAttends()),
                () -> assertEquals(2, one.getUnknowns()),
                () -> assertEquals(5, one.getAbsents()),
                () -> assertEquals(1, two.getAttends()),
                () -> assertEquals(1, two.getUnknowns())
        );
    }

    @DisplayName("회의 상세 조회 - 성공")
    @Test
    void get_meeting_success() throws Exception {

        // given
        MeetingAndAdminsQueryDto queryDto = MeetingAndAdminsQueryDtoBuilder.build();
        when(meetingQueryPort.findWithTeamAndAdminsById(any()))
                .thenReturn(Optional.of(queryDto));

        // when
        MeetingResponseDto responseDto = meetingQueryService.getMeeting(1L);

        // then
        assertAll(
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingId(), responseDto.getMeetingId()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingName(), responseDto.getMeetingName()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingPurpose(), responseDto.getMeetingPurpose()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getMeetingDate(), responseDto.getMeetingDate()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getStartTime(), responseDto.getStartTime()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getEndTime(), responseDto.getEndTime()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getOperationTeamId(), responseDto.getOperationTeamId()),
                () -> assertEquals(queryDto.getMeetingQueryDto().getOperationTeamName(), responseDto.getOperationTeamName()),
                () -> assertEquals(queryDto.getAdminQueryDtos().get(0).getWorkspaceUserId(),
                        responseDto.getAdmins().get(0).getWorkspaceUserId()),
                () -> assertEquals(queryDto.getAdminQueryDtos().get(1).getWorkspaceUserId(),
                        responseDto.getAdmins().get(1).getWorkspaceUserId())
        );
    }
}
