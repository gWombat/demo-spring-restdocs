package fr.gwombat.demospringrestdocs.controllers;

import fr.gwombat.demospringrestdocs.domain.Person;
import fr.gwombat.demospringrestdocs.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class PersonControllerTests {

    @Autowired
    private MockMvc          mockMvc;
    @MockBean
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        given(personRepository.findAll()).willReturn(Arrays.asList(
                new Person(1L, "DOE", "John"),
                new Person(2L, "OTHER", "Person")
        ));

        given(personRepository.findById(1L)).willReturn(
                Optional.of(new Person(1L, "DOE", "John"))
        );
    }

    @Test
    public void list_all_persons() throws Exception {
        this.mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("persons/list", responseFields(
                        fieldWithPath("[].id").description("The id of the person"),
                        fieldWithPath("[].name").description("The last name of the person"),
                        fieldWithPath("[].firstname").description("The first name of the person")
                )));
    }

    @Test
    public void find_person_by_id_1_should_be_ok() throws Exception {
        this.mockMvc.perform(get("/persons/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("persons/findOne",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        //pathParameters(
                        //        parameterWithName("id").description("The id of the person")),
                        responseFields(
                                fieldWithPath("id").description("The id of the person"),
                                fieldWithPath("name").description("The last name of the person"),
                                fieldWithPath("firstname").description("The first name of the person")
                        )));
    }
}
