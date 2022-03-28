package com.cmc.meeron.common.advice.workspaceuser;

import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckWorkspaceAdmin {
}
