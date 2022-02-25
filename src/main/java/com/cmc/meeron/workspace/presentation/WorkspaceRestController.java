package com.cmc.meeron.workspace.presentation;

import com.cmc.meeron.workspace.application.WorkspaceUseCase;
import com.cmc.meeron.workspace.application.dto.response.WorkspaceResponseDto;
import com.cmc.meeron.workspace.presentation.dto.response.MyWorkspacesResponse;
import com.cmc.meeron.workspace.presentation.dto.response.WorkspaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkspaceRestController {

    private final WorkspaceUseCase workspaceUseCase;

    @GetMapping("/users/{userId}/workspaces")
    @ResponseStatus(HttpStatus.OK)
    public MyWorkspacesResponse getMyWorkspaces(@PathVariable Long userId) {
        List<WorkspaceResponseDto> myWorkspaces = workspaceUseCase.getMyWorkspaces(userId);
        return WorkspacePresentationAssembler.fromWorkspaceResponseDtos(myWorkspaces);
    }

    @GetMapping("/workspaces/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceResponse getWorkspace(@PathVariable Long workspaceId) {
        WorkspaceResponseDto workspaceResponseDto = workspaceUseCase.getWorkspace(workspaceId);
        return WorkspacePresentationAssembler.fromWorkspaceResponseDto(workspaceResponseDto);
    }
}
