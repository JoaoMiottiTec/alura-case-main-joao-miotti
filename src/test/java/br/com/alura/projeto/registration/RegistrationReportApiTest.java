package br.com.alura.projeto.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RegistrationController.class)
class RegistrationReportApiTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean RegistrationService service; 
  @MockBean RegistrationRepository registrations;

  @Test
  void report_returns_desc_by_registrations() throws Exception {
    RegistrationReportItem item1 = new Item(
        "Java Fundamentals", "JAVA01", "Ana Silva", "ana@alura.com", 7L);
    RegistrationReportItem item2 = new Item(
        "Spring Boot API", "SPR02", "Bruno Lima", "bruno@alura.com", 3L);

    when(registrations.reportTopCourses()).thenReturn(List.of(item1, item2));

    mvc.perform(get("/registration/report").accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].courseName").value("Java Fundamentals"))
        .andExpect(jsonPath("$[0].registrations").value(7))
        .andExpect(jsonPath("$[1].courseName").value("Spring Boot API"))
        .andExpect(jsonPath("$[1].registrations").value(3));
  }

  @Test
  void report_uses_name_asc_as_tiebreaker_when_counts_equal() throws Exception {
    RegistrationReportItem a = new Item(
        "Algorithms", "ALG1", "Ana Silva", "ana@alura.com", 5L);
    RegistrationReportItem z = new Item(
        "Zebra Course", "ZEB1", "Ana Silva", "ana@alura.com", 5L);

    when(registrations.reportTopCourses()).thenReturn(List.of(a, z));

    mvc.perform(get("/registration/report").accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].courseName").value("Algorithms"))
        .andExpect(jsonPath("$[0].registrations").value(5))
        .andExpect(jsonPath("$[1].courseName").value("Zebra Course"))
        .andExpect(jsonPath("$[1].registrations").value(5));
  }

  static record Item(
      String courseName,
      String courseCode,
      String instructorName,
      String instructorEmail,
      Long registrations
  ) implements RegistrationReportItem {
    @Override public String getCourseName() { return courseName; }
    @Override public String getCourseCode() { return courseCode; }
    @Override public String getInstructorName() { return instructorName; }
    @Override public String getInstructorEmail() { return instructorEmail; }
    @Override public Long getRegistrations() { return registrations; }
  }
}
