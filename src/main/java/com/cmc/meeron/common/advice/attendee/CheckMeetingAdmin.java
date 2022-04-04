package com.cmc.meeron.common.advice.attendee;

import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckMeetingAdmin {
}
