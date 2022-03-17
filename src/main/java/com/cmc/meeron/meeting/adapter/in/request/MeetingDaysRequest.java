package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.common.validator.EnumValid;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDaysRequest {

    @EnumValid(enumClass = MeetingDaysSearchType.class, message = "지원하지 않는 검색 유형입니다. 'WORKSPACE', 'WORKSPACE_USER', 'TEAM' 중 하나를 입력해주세요.")
    private String type;

    @NotNull(message = "검색 유형의 ID를 입력해주세요.")
    private Long id;

    @DateTimeFormat(pattern = "yyyy/M")
    @NotNull(message = "찾을 회의를 'yyyy/M' 형식으로 입력해주세요.")
    private YearMonth date;

    public String getType() {
        return type.toUpperCase();
    }
}
