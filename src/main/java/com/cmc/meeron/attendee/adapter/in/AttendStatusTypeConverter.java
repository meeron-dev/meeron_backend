package com.cmc.meeron.attendee.adapter.in;

import com.cmc.meeron.attendee.adapter.in.request.AttendStatusType;
import com.cmc.meeron.common.exception.ApplicationException;
import com.cmc.meeron.common.exception.ClientErrorCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class AttendStatusTypeConverter implements Converter<String, AttendStatusType> {

    @Override
    public AttendStatusType convert(String source) {
        try {
            return AttendStatusType.valueOf(source.trim().toUpperCase());
        } catch (Exception e) {
            throw new ApplicationException(ClientErrorCode.BIND_EXCEPTION);
        }
    }
}
