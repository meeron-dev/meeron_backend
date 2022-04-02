package com.cmc.meeron.workspace.integration;

import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.workspace.adapter.in.request.CreateWorkspaceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class WorkspaceIntegrationTest extends IntegrationTest {

    @DisplayName("유저의 속한 워크스페이스 조회 - 성공")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void get_my_workspaces_success(Long userId) throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}/workspaces", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myWorkspaces", hasSize(1)));
    }

    @DisplayName("워크스페이스 정보 조회 - 성공")
    @Test
    void get_workspace_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/workspaces/{workspaceId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId", is(1)))
                .andExpect(jsonPath("$.workspaceName", is("4tune")))
                .andExpect(jsonPath("$.workspaceLogoUrl", nullValue()));
    }

    @DisplayName("워크스페이스 생성 - 성공")
    @Test
    void create_workspace_success() throws Exception {

        // given
        CreateWorkspaceRequest request = CreateWorkspaceRequest.builder()
                .name("테스트")
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/workspaces", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId", is(3)))
                .andExpect(jsonPath("$.workspaceName", is(request.getName())))
                .andExpect(jsonPath("$.workspaceLogoUrl", emptyString()));
    }
}
