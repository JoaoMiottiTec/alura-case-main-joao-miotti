package br.com.alura.projeto.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationReportApiTest {

    @Autowired MockMvc mvc;

    @MockBean RegistrationService service;
    @MockBean RegistrationRepository registrations;

    SpelAwareProxyProjectionFactory factory;

    @BeforeEach void setUp() { factory = new SpelAwareProxyProjectionFactory(); }

    private RegistrationReportItem item(String name, String code,
                                        String instrName, String instrEmail, long count) {
        return factory.createProjection(RegistrationReportItem.class, java.util.Map.of(
                "courseName", name,
                "courseCode", code,
                "instructorName", instrName,
                "instructorEmail", instrEmail,
                "registrations", count
        ));
    }

    @Test
    void report__returns_ordered_list() throws Exception {
        var first  = item("Java Web", "java-web", "Ana", "ana@alura.com", 10);
        var second = item("Spring Boot", "spring-boot", "Ana", "ana@alura.com", 7);

        when(registrations.reportTopCourses()).thenReturn(List.of(first, second));

        mvc.perform(get("/registration/report").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseCode").value("java-web"))
                .andExpect(jsonPath("$[0].registrations").value(10))
                .andExpect(jsonPath("$[1].courseCode").value("spring-boot"))
                .andExpect(jsonPath("$[1].registrations").value(7));
    }
}
