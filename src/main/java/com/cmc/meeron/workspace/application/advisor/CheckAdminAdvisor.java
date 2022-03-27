package com.cmc.meeron.workspace.application.advisor;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserIdCheckable;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
class CheckAdminAdvisor {

    private final WorkspaceUserQueryPort workspaceUserQueryPort;

    @Before("@annotation(com.cmc.meeron.common.advice.workspaceuser.CheckWorkspaceAdmin)")
    public void checkAdminWorkspaceUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        WorkspaceUserIdCheckable checkable = (WorkspaceUserIdCheckable) args[0];
        WorkspaceUser workspaceUser = workspaceUserQueryPort.findById(checkable.getAdminWorkspaceUserId())
                .orElseThrow(WorkspaceUserNotFoundException::new);
        workspaceUser.isAdminOrThrow();
    }
}
