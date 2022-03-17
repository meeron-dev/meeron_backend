package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.common.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.Year;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthMeetingsCountRequest {

    @EnumValid(enumClass = MeetingDaysSearchType.class, message = "지원하지 않는 검색 유형입니다. 'WORKSPACE', 'WORKSPACE_USER', 'TEAM' 중 하나를 입력해주세요.")
    private String type;

    @NotNull(message = "검색 유형의 ID를 입력해주세요.")
    private Long id;

    @NotNull(message = "찾을 회의의 년도를 'yyyy' 형식으로 입력해주세요.")
    private Year year;

    public String getType() {
        return type.toUpperCase();
    }
}
