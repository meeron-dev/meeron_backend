package com.cmc.meeron.support.restdocs;

import com.cmc.meeron.HealthRestController;
import com.cmc.meeron.attendee.adapter.in.AttendeeRestController;
import com.cmc.meeron.attendee.application.port.in.AttendeeCommandUseCase;
import com.cmc.meeron.attendee.application.port.in.AttendeeQueryUseCase;
import com.cmc.meeron.auth.adapter.in.AuthRestController;
import com.cmc.meeron.auth.application.port.in.AuthUseCase;
import com.cmc.meeron.common.exception.GlobalExceptionHandler;
import com.cmc.meeron.config.JacksonTimeFormatConfig;
import com.cmc.meeron.config.RestDocsConfig;
import com.cmc.meeron.file.adapter.in.FileRestController;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.meeting.adapter.in.MeetingCalendarQueryUseCaseFactory;
import com.cmc.meeron.meeting.adapter.in.MeetingCommandRestController;
import com.cmc.meeron.meeting.adapter.in.MeetingQueryRestController;
import com.cmc.meeron.meeting.application.port.in.MeetingCommandUseCase;
import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.support.security.SecuritySupport;
import com.cmc.meeron.team.adapter.in.TeamRestController;
import com.cmc.meeron.team.application.port.in.TeamCommandUseCase;
import com.cmc.meeron.team.application.port.in.TeamMemberManageUseCase;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.topic.agenda.adapter.in.AgendaRestController;
import com.cmc.meeron.topic.agenda.application.port.in.AgendaCommandUseCase;
import com.cmc.meeron.topic.agenda.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.topic.issue.adapter.in.IssueRestController;
import com.cmc.meeron.topic.issue.application.port.in.IssueQueryUseCase;
import com.cmc.meeron.user.adapter.in.UserRestController;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.workspace.adapter.in.WorkspaceRestController;
import com.cmc.meeron.workspaceuser.adapter.in.WorkspaceUserRestController;
import com.cmc.meeron.workspace.application.port.in.WorkspaceCommandUseCase;
import com.cmc.meeron.workspace.application.port.in.WorkspaceQueryUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserCommandUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserQueryUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Disabled
@WebMvcTest(value = {
        HealthRestController.class,
        AuthRestController.class,
        GlobalExceptionHandler.class,
        MeetingQueryRestController.class,
        MeetingCommandRestController.class,
        UserRestController.class,
        WorkspaceRestController.class,
        TeamRestController.class,
        FileRestController.class,
        WorkspaceUserRestController.class,
        AgendaRestController.class,
        AttendeeRestController.class,
        IssueRestController.class,
})
@ExtendWith(RestDocumentationExtension.class)
@Import({
        RestDocsConfig.class,
        JacksonTimeFormatConfig.class,
})
public abstract class RestDocsTestSupport extends SecuritySupport {

    @MockBean protected AuthUseCase authUseCase;
    @MockBean protected MeetingQueryUseCase meetingQueryUseCase;
    @MockBean protected UserQueryUseCase userQueryUseCase;
    @MockBean protected WorkspaceQueryUseCase workspaceQueryUseCase;
    @MockBean protected TeamQueryUseCase teamQueryUseCase;
    @MockBean protected MeetingCommandUseCase meetingCommandUseCase;
    @MockBean protected FileManager fileManager;
    @MockBean protected MeetingCalendarQueryUseCaseFactory meetingCalendarQueryUseCaseFactory;
    @MockBean protected UserCommandUseCase userCommandUseCase;
    @MockBean protected WorkspaceCommandUseCase workspaceCommandUseCase;
    @MockBean protected TeamCommandUseCase teamCommandUseCase;
    @MockBean protected WorkspaceUserQueryUseCase workspaceUserQueryUseCase;
    @MockBean protected WorkspaceUserCommandUseCase workspaceUserCommandUseCase;
    @MockBean protected AttendeeQueryUseCase attendeeQueryUseCase;
    @MockBean protected AgendaQueryUseCase agendaQueryUseCase;
    @MockBean protected AgendaCommandUseCase agendaCommandUseCase;
    @MockBean protected AttendeeCommandUseCase attendeeCommandUseCase;
    @MockBean protected IssueQueryUseCase issueQueryUseCase;
    @MockBean protected TeamMemberManageUseCase teamMemberManageUseCase;

    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected RestDocumentationResultHandler restDocumentationResultHandler;
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(final WebApplicationContext webApplicationContext,
               final RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider)
                        .uris()
                        .withScheme("https")
                        .withHost("dev.meeron.click")
                        .withPort(443))
                .apply(springSecurity())
                .alwaysDo(print())
                .alwaysDo(restDocumentationResultHandler)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected MockMultipartFile createJsonFile(Object request) throws JsonProcessingException {
        return new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );
    }
}
