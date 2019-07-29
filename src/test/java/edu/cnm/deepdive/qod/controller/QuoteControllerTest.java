package edu.cnm.deepdive.qod.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;

import edu.cnm.deepdive.qod.SpringRestDocsApplication;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = SpringRestDocsApplication.class)
class QuoteControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(provider))
        .alwaysDo(document("{method-name}",
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .build();
  }

  @Test
  void getRandom() throws Exception {

    mockMvc.perform(get("/quotes/random"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(document("get-random",
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//            links(linkWithRel("self").description("URL of this quote")),
            responseHeaders(headerWithName("Content-type").description("Content type of payload"))
        ));
  }

  @Test
  void get404() throws Exception {
    mockMvc.perform(get("/quotes/01234567-89AB-CDEF-0123-456789ABCDEF"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void postQuote() throws Exception {
    mockMvc.perform(
        post("/quotes")
          .content("{\"text\": \"Be excellent to each other.\"}")
          .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)


    )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
  }
}