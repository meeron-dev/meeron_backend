package com.cmc.meeron.workspace.adapter.in;

import com.cmc.meeron.workspace.adapter.in.request.CreateWorkspaceRequest;
import com.cmc.meeron.workspace.application.port.in.WorkspaceCommandUseCase;
import com.cmc.meeron.workspace.application.port.in.WorkspaceQueryUseCase;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.adapter.in.response.MyWorkspacesResponse;
import com.cmc.meeron.workspace.adapter.in.response.WorkspaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkspaceRestController {

    private final WorkspaceQueryUseCase workspaceQueryUseCase;
    private final WorkspaceCommandUseCase workspaceCommandUseCase;

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

    @PostMapping("/workspaces")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceResponse createWorkspace(@RequestBody @Valid CreateWorkspaceRequest createWorkspaceRequest) {
        WorkspaceResponseDto workspaceResponseDto = workspaceCommandUseCase.createWorkspace(createWorkspaceRequest.getName());
        return WorkspaceResponse.of(workspaceResponseDto);
    }
}
