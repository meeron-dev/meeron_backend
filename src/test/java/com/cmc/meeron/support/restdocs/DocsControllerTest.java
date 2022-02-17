package com.cmc.meeron.support.restdocs;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
public class DocsControllerTest extends RestDocsTestSupport{

    // TODO: 2022/02/17 kobeomseok95 EnumType 생성 시 작성할 것
    @Test
    void enums_docs_test() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/test/enums")
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResult = resultActions.andReturn();
        EnumDocs enumDocs = getData(mvcResult);
        resultActions.andExpect(status().isOk())
                .andDo(restDocumentationResultHandler.document(

                ));
    }

    private EnumDocs getData(MvcResult result) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<ApiResponseDto<EnumDocs>>() {})
                .getData();
    }
}
