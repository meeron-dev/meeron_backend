package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                    "MEERON-400",
                    "회의 날짜를 'yyyy-MM-dd' 형식으로 입력해주세요.");
            return;
        }
        LocalDateTime startDateTime = LocalDateTime.of(meetingDate, request.getStartTime());
        LocalDateTime now = LocalDateTime.now();
        if (startDateTime.isBefore(now)) {
            errors.rejectValue("meetingDate",
                    "MEERON-400",
                    "현재 시간보다 이전 시간으로 회의를 생성할 수 없습니다.");
            errors.rejectValue("startTime",
                    "MEERON-400",
                    "현재 시간보다 이전 시간으로 회의를 생성할 수 없습니다.");
        }
    }
}
