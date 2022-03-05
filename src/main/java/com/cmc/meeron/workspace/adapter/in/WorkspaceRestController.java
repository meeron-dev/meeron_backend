package com.cmc.meeron.workspace.adapter.in;

import com.cmc.meeron.workspace.application.port.in.WorkspaceQueryUseCase;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.adapter.in.response.MyWorkspacesResponse;
import com.cmc.meeron.workspace.adapter.in.response.WorkspaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkspaceRestController {

    private final WorkspaceQueryUseCase workspaceQueryUseCase;

    // FIXME: 2022/03/04 kobeomseok95 workspaces?userId=12
    @GetMapping("/users/{userId}/workspaces")
    @ResponseStatus(HttpStatus.OK)
    public MyWorkspacesResponse getMyWorkspaces(@PathVariable Long userId) {
        List<WorkspaceResponseDto> myWorkspaces = workspaceQueryUseCase.getMyWorkspaces(userId);
        return MyWorkspacesResponse.of(myWorkspaces);
    }

    @GetMapping("/workspaces/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceResponse getWorkspace(@PathVariable Long workspaceId) {
        WorkspaceResponseDto workspaceResponseDto = workspaceQueryUseCase.getWorkspace(workspaceId);
        return WorkspaceResponse.of(workspaceResponseDto);
    }
}
