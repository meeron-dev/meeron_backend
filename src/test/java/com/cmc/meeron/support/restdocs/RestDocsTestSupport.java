package com.cmc.meeron.support.restdocs;

import com.cmc.meeron.HealthRestController;
import com.cmc.meeron.auth.application.AuthUseCase;
import com.cmc.meeron.auth.presentation.AuthRestController;
import com.cmc.meeron.common.exception.GlobalExceptionHandler;
import com.cmc.meeron.config.RestDocsConfig;
import com.cmc.meeron.meeting.application.MeetingUseCase;
import com.cmc.meeron.meeting.presentation.MeetingRestController;
import com.cmc.meeron.support.security.SecuritySupport;
import com.cmc.meeron.user.application.UserQueryUseCase;
import com.cmc.meeron.user.presentation.UserRestController;
import com.cmc.meeron.workspace.application.WorkspaceUseCase;
import com.cmc.meeron.workspace.presentation.WorkspaceRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
        MeetingRestController.class,
        UserRestController.class,
        WorkspaceRestController.class
})
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
public abstract class RestDocsTestSupport extends SecuritySupport {

    @MockBean protected AuthUseCase authUseCase;
    @MockBean protected MeetingUseCase meetingUseCase;
    @MockBean protected UserQueryUseCase userQueryUseCase;
    @MockBean protected WorkspaceUseCase workspaceUseCase;

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
                        .withHost("dev.meeron.net")
                        .withPort(443))
                .apply(springSecurity())
                .alwaysDo(print())
                .alwaysDo(restDocumentationResultHandler)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
