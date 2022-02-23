package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingStatus;
import com.cmc.meeron.meeting.domain.repository.MeetingRepository;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock MeetingRepository meetingRepository;
    @InjectMocks MeetingService meetingService;

    @DisplayName("오늘 예정된 회의 가져오기 - 성공")
    @Test
    void today_expected_meeting_success() throws Exception {

        // given
        TodayMeetingRequestDto request = createTodayExpectedMeetingRequest();
        List<Meeting> response = getTodayMeetingResponse();
        when(meetingRepository.findTodayMeetings(any(), any()))
                .thenReturn(response);

        // when
        List<TodayMeetingResponseDto> result = meetingService.getTodayMeetings(request);

        // then
        assertAll(
                () -> verify(meetingRepository).findTodayMeetings(request.getWorkspaceId(), request.getWorkspaceUserId()),
                () -> assertEquals(response.size(), result.size())
        );
    }

    private List<Meeting> getTodayMeetingResponse() {
        return List.of(
                Meeting.builder()
                        .id(1L)
                        .workspace(Workspace.builder().id(1L).build())
                        .name("테스트 회의1")
                        .purpose("목적1")
                        .startDate(LocalDate.now())
                        .startTime(LocalTime.now().minusHours(3))
                        .endTime(LocalTime.now().minusHours(2))
                        .place("테스트 장소1")
                        .privateMeeting(false)
                        .meetingStatus(MeetingStatus.END)
                        .build(),
                Meeting.builder()
                        .id(2L)
                        .workspace(Workspace.builder().id(1L).build())
                        .name("테스트 회의2")
                        .purpose("목적2")
                        .startDate(LocalDate.now())
                        .startTime(LocalTime.now().plusHours(1))
                        .endTime(LocalTime.now().plusHours(2))
                        .place("테스트 장소2")
                        .privateMeeting(false)
                        .meetingStatus(MeetingStatus.EXPECT)
                        .build()
        );
    }

    private TodayMeetingRequestDto createTodayExpectedMeetingRequest() {
        return TodayMeetingRequestDto.builder()
                .workspaceId(1L)
                .workspaceUserId(2L)
                .build();
    }
}
