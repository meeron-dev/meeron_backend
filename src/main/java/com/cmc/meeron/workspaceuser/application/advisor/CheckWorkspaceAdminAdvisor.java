package com.cmc.meeron.workspaceuser.application.advisor;

import com.cmc.meeron.common.advice.workspaceuser.WorkspaceUserAuthorityCheckable;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.workspaceuser.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
class CheckWorkspaceAdminAdvisor {

    private final WorkspaceUserQueryPort workspaceUserQueryPort;

    @Before("@annotation(com.cmc.meeron.common.advice.workspaceuser.CheckWorkspaceAdmin)")
    public void checkAdminWorkspaceUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        WorkspaceUserAuthorityCheckable checkable = (WorkspaceUserAuthorityCheckable) args[0];
        WorkspaceUser workspaceUser = workspaceUserQueryPort.findById(checkable.getWorkspaceUserId())
                .orElseThrow(WorkspaceUserNotFoundException::new);
        workspaceUser.isAdminOrThrow();
    }
}
