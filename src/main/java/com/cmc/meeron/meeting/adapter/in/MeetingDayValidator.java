package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class MeetingDayValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateMeetingRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateMeetingRequest request = (CreateMeetingRequest) target;
        LocalDate meetingDate = request.getMeetingDate();
        if (meetingDate == null) {
            errors.rejectValue("meetingDate",
                    String.valueOf(CommonErrorCode.BIND_EXCEPTION.getCode()),
                    "회의 날짜를 'yyyy-MM-dd' 형식으로 입력해주세요.");
        }
    }
}
