package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.user.UserErrorCode;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.application.port.in.response.UserResponseDto;
import com.cmc.meeron.user.application.port.in.response.UserResponseDtoBuilder;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.stream.Stream;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockJwt
class UserRestControllerTest extends RestDocsTestSupport {

    @DisplayName("?????? ?????? ???????????? - ??????")
    @Test
    void get_me_success() throws Exception {

        // given
        UserResponseDto me = UserResponseDtoBuilder.build();
        when(userQueryUseCase.getMe(any()))
                .thenReturn(me);

        // when, then, docs
        mockMvc.perform(get("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(me.getUserId().intValue())))
                .andExpect(jsonPath("$.loginEmail", is(me.getLoginEmail())))
                .andExpect(jsonPath("$.name", is(me.getName())))
                .andExpect(jsonPath("$.profileImageUrl", is(me.getProfileImageUrl())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? URL")
                        )
                ));
    }

    @DisplayName("????????? ?????? ?????? - ??????")
    @Test
    void set_user_name_success() throws Exception {

        // given
        SetNameRequest request = createSetNameRequest();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("??????").attributes(field("constraints", "1??? ?????? 5??? ??????"))
                        )
                ));
    }

    private SetNameRequest createSetNameRequest() {
        return SetNameRequest.builder()
                .name("?????????")
                .build();
    }

    @DisplayName("????????? ?????? ?????? - ??????????????? ????????? ?????? ??????")
    @ParameterizedTest
    @MethodSource("createFailSetNameRequests")
    void set_user_name_fail_name_not_blank(SetNameRequest request) throws Exception {

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    private static Stream<Arguments> createFailSetNameRequests() {
        SetNameRequest request1 = SetNameRequest.builder()
                .name("????????????????????????")
                .build();
        SetNameRequest request2 = SetNameRequest.builder()
                .name("    ")
                .build();
        return Stream.of(
                Arguments.of(request1),
                Arguments.of(request2)
        );
    }

    @DisplayName("????????? ?????? ?????? - ????????? ?????? ?????? ??????")
    @Test
    void set_user_name_fail_not_found_user() throws Exception {

        // given
        doThrow(new UserNotFoundException())
                .when(userCommandUseCase)
                .setName(any(), any());
        SetNameRequest request = createSetNameRequest();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(UserErrorCode.NOT_FOUND_USER.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("????????? ????????? ??????????????? ?????? - ?????? / ???????????? ?????? ????????? ??????")
    @Test
    void check_named_user_fail_not_found_user() throws Exception {

        // given
        when(userQueryUseCase.checkNamedUser(any()))
                .thenThrow(new UserNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(UserErrorCode.NOT_FOUND_USER.getCode())));
    }

    @DisplayName("????????? ????????? ??????????????? ?????? - ??????")
    @Test
    void check_named_user_success_named() throws Exception {

        // given
        when(userQueryUseCase.checkNamedUser(any()))
                .thenReturn(true);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.named", is(true)))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        responseFields(
                                fieldWithPath("named").type(JsonFieldType.BOOLEAN).description("????????? ?????????????????? ??????")
                        )
                ));
    }

    @DisplayName("?????? ?????? - ??????")
    @Test
    void quit_success() throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/users/quit")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        )
                ));
    }

    @DisplayName("?????????????????? ????????? ?????? ?????? ???????????? - ?????? / ???????????? ?????? ?????????????????? ????????? ??????")
    @Test
    void get_user_fail_not_found() throws Exception {

        // given
        when(userQueryUseCase.getUserByWorkspaceUserId(any()))
                .thenThrow(new UserNotFoundException());

        // when, then ,docs
        mockMvc.perform(get("/api/workspace-users/{workspaceUserId}/user", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(UserErrorCode.NOT_FOUND_USER.getCode())));
    }

    @DisplayName("?????????????????? ????????? ?????? ?????? ???????????? - ??????")
    @Test
    void get_user_success() throws Exception {

        // given
        UserResponseDto responseDto = UserResponseDtoBuilder.build();
        when(userQueryUseCase.getUserByWorkspaceUserId(any()))
                .thenReturn(responseDto);

        // when, then ,docs
        mockMvc.perform(get("/api/workspace-users/{workspaceUserId}/user", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(responseDto.getUserId().intValue())))
                .andExpect(jsonPath("$.loginEmail", is(responseDto.getLoginEmail())))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.profileImageUrl", is(responseDto.getProfileImageUrl())))
                .andExpect(handler().handlerType(UserRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceUserId").description("?????????????????? ?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("??????ID"),
                                fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("?????? ????????? url")
                        )
                ));
    }
}
