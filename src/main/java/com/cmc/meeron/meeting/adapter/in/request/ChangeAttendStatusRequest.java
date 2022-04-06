package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.common.validator.EnumValid;
import com.cmc.meeron.meeting.application.port.in.request.ChangeAttendStatusRequestDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeAttendStatusRequest {

    @NotNull(message = "회의 ID를 입력해주세요.")
    private Long meetingId;

    @EnumValid(enumClass = AttendStatusType.class,
            ignoreCase = true,
            message = "지원하지 않는 참여 상태입니다. 'attend', 'absent' 중 하나를 입력해주세요.")
    private String status;

    public ChangeAttendStatusRequestDto toRequestDto(Long workspaceUserId) {
        return ChangeAttendStatusRequestDto.builder()
                .meetingId(meetingId)
                .status(status.toUpperCase())
                .workspaceUserId(workspaceUserId)
                .build();
    }
}
