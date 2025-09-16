package br.com.alura.projeto.registration;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RegistrationController.class)
class RegistrationControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean RegistrationService service;
  @MockBean RegistrationRepository registrations;

  private NewRegistrationDTO dto(String courseCode, String email) {
    NewRegistrationDTO d = new NewRegistrationDTO();
    d.setCourseCode(courseCode);
    d.setStudentEmail(email);
    return d;
  }

  @Test
  @DisplayName("POST /registration/new -> 201 Created with body")
  void createRegistration_returns201() throws Exception {
    String courseCode = "JAVA01";
    String email = "tito@alura.com";
    LocalDateTime now = LocalDateTime.of(2025, 9, 16, 12, 0);

    RegistrationDTO response = new RegistrationDTO(courseCode, email, now);
    when(service.register(any(NewRegistrationDTO.class))).thenReturn(response);

    mvc.perform(
            post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto(courseCode, email))))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.courseCode").value(courseCode))
        .andExpect(jsonPath("$.studentEmail").value(email))
        .andExpect(jsonPath("$.registeredAt").exists());
  }

  @Test
  @DisplayName("GET /registration/report -> 200 OK with projected items")
  void report_returns200_withItems() throws Exception {
    RegistrationReportItem item1 =
        new RegistrationReportItemImpl(
            "Java Fundamentals", "JAVA01", "Ana Silva", "ana@alura.com", 5L);
    RegistrationReportItem item2 =
        new RegistrationReportItemImpl(
            "Spring Boot API", "SPR02", "Bruno Lima", "bruno@alura.com", 3L);

    when(registrations.reportTopCourses()).thenReturn(List.of(item1, item2));

    mvc.perform(get("/registration/report"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].courseName").value("Java Fundamentals"))
        .andExpect(jsonPath("$[0].courseCode").value("JAVA01"))
        .andExpect(jsonPath("$[0].instructorName").value("Ana Silva"))
        .andExpect(jsonPath("$[0].instructorEmail").value("ana@alura.com"))
        .andExpect(jsonPath("$[0].registrations").value(5))
        .andExpect(jsonPath("$[1].courseName").value("Spring Boot API"))
        .andExpect(jsonPath("$[1].courseCode").value("SPR02"))
        .andExpect(jsonPath("$[1].instructorName").value("Bruno Lima"))
        .andExpect(jsonPath("$[1].instructorEmail").value("bruno@alura.com"))
        .andExpect(jsonPath("$[1].registrations").value(3));
  }

  static class RegistrationReportItemImpl implements RegistrationReportItem {
    private final String courseName;
    private final String courseCode;
    private final String instructorName;
    private final String instructorEmail;
    private final Long registrations;

    RegistrationReportItemImpl(
        String courseName,
        String courseCode,
        String instructorName,
        String instructorEmail,
        Long registrations) {
      this.courseName = courseName;
      this.courseCode = courseCode;
      this.instructorName = instructorName;
      this.instructorEmail = instructorEmail;
      this.registrations = registrations;
    }

    @Override
    public String getCourseName() {
      return courseName;
    }

    @Override
    public String getCourseCode() {
      return courseCode;
    }

    @Override
    public String getInstructorName() {
      return instructorName;
    }

    @Override
    public String getInstructorEmail() {
      return instructorEmail;
    }

    @Override
    public Long getRegistrations() {
      return registrations;
    }
  }
}
