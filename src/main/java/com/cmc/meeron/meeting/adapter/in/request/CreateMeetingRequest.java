package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMeetingRequest {

    @NotNull(message = "회의 시작 날짜를 'yyyy/M/d' 형식으로 입력해주세요.")
    private LocalDate meetingDate;

    @NotNull(message = "회의 시작 시간을 'hh:mm a' 형식으로 입력해주세요.")
    private LocalTime startTime;

    @NotNull(message = "회의 마감 시간을 'hh:mm a' 형식으로 입력해주세요.")
    private LocalTime endTime;

    @NotBlank(message = "회의명을 3자 이상 30자 이하로 입력해주세요.")
    @Length(min = 3, max = 30, message = "회의명을 3자 이상 30자 이하로 입력해주세요.")
    private String meetingName;

    @NotBlank(message = "회의 성격을 1자 이상 10자 이하로 입력해주세요.")
    @Length(max = 10, message = "회의 성격을 1자 이상 10자 이하로 입력해주세요.")
    private String meetingPurpose;

    @NotNull(message = "담당 팀 ID를 입력해주세요.")
    private Long operationTeamId;

    @NotNull(message = "공동 관리자는 본인이 반드시 포함되어야 합니다.")
    @Size(min = 1, message = "공동 관리자는 본인이 반드시 포함되어야 합니다.")
    private List<Long> meetingAdminIds;

    public CreateMeetingRequestDto toRequestDto() {
        return CreateMeetingRequestDto.builder()
                .startDate(meetingDate)
                .startTime(startTime)
                .endTime(endTime)
                .meetingName(meetingName)
                .meetingPurpose(meetingPurpose)
                .operationTeamId(operationTeamId)
                .meetingManagerIds(meetingAdminIds)
                .build();
    }
}
