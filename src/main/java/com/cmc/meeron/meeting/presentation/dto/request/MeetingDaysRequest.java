package com.cmc.meeron.meeting.presentation.dto.request;

import com.cmc.meeron.common.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDaysRequest {

    @EnumValid(enumClass = MeetingDaysSearchType.class, message = "지원하지 않는 검색 유형입니다. 'WORKSPACE', 'WORKSPACE_USER', 'TEAM' 중 하나를 입력해주세요.")
    private String type;

    @NotEmpty(message = "검색 유형의 ID를 입력해주세요.")
    private List<Long> id;

    @NotNull(message = "찾을 회의를 'yyyy-MM' 형식으로 입력해주세요.")
    private YearMonth date;

    public String getType() {
        return type.toUpperCase();
    }
}
