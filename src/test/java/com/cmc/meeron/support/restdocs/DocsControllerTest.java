package com.cmc.meeron.support.restdocs;

import org.junit.jupiter.api.Disabled;

@Disabled
public class DocsControllerTest /*extends RestDocsTestSupport*/ {

    // TODO: 2022/02/17 kobeomseok95 EnumType 생성 시 작성할 것
//    @Test
//    void enums_docs_test() throws Exception {
//
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/test/enums")
//                .contentType(MediaType.APPLICATION_JSON));
//        MvcResult mvcResult = resultActions.andReturn();
//        EnumDocs enumDocs = getData(mvcResult);
//        resultActions.andExpect(status().isOk())
//                .andDo(restDocumentationResultHandler.document(
//
//                ));
//    }

//    private EnumDocs getData(MvcResult result) throws IOException {
//        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<ApiResponseDto<EnumDocs>>() {})
//                .getData();
//    }
}
