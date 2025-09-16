package br.com.alura.projeto.login;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.alura.projeto.course.CourseDTO;
import br.com.alura.projeto.course.CourseService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = LoginController.class)
@TestPropertySource(
    properties = {"spring.mvc.view.prefix=/WEB-INF/views/", "spring.mvc.view.suffix=.jsp"})
class LoginIntegrationTest {

  @Autowired MockMvc mockMvc;

  @MockBean CourseService courseService;

  @Test
  @DisplayName("GET / → renderiza login.jsp com categories e courses (sem filtro)")
  void home_semFiltro_ok() throws Exception {
    List<String> categories = List.of("Back-end", "Front-end");

    CourseDTO spring =
        new CourseDTO(
            1L,
            "Spring Boot",
            "SPRING-BOOT",
            "instrutor@alura.com",
            "Back-end",
            "API REST",
            "ACTIVE");
    CourseDTO react =
        new CourseDTO(2L, "React", "REACT", "instrutor@alura.com", "Front-end", "SPA", "ACTIVE");

    given(courseService.listActiveCategoryNames()).willReturn(categories);
    given(courseService.listActive(null)).willReturn(List.of(spring, react));

    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("categories", "courses"))
        .andExpect(model().attribute("categories", contains("Back-end", "Front-end")))
        .andExpect(model().attribute("selectedCategory", nullValue()))
        .andExpect(model().attribute("courses", hasSize(2)))
        .andExpect(
            model()
                .attribute(
                    "courses",
                    everyItem(
                        allOf(
                            hasProperty("status", is("ACTIVE")),
                            hasProperty("categoryName", anyOf(is("Back-end"), is("Front-end")))))));
  }

  @Test
  @DisplayName("GET /?category=Back-end → aplica filtro e mantém selectedCategory")
  void home_comFiltro_ok() throws Exception {
    List<String> categories = List.of("Back-end", "Front-end");

    CourseDTO spring =
        new CourseDTO(
            1L,
            "Spring Boot",
            "SPRING-BOOT",
            "instrutor@alura.com",
            "Back-end",
            "API REST",
            "ACTIVE");

    given(courseService.listActiveCategoryNames()).willReturn(categories);
    given(courseService.listActive("Back-end")).willReturn(List.of(spring));

    mockMvc
        .perform(get("/").param("category", "Back-end"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("categories", "courses", "selectedCategory"))
        .andExpect(model().attribute("categories", contains("Back-end", "Front-end")))
        .andExpect(model().attribute("selectedCategory", is("Back-end")))
        .andExpect(model().attribute("courses", hasSize(1)))
        .andExpect(
            model()
                .attribute(
                    "courses",
                    everyItem(
                        allOf(
                            hasProperty("status", is("ACTIVE")),
                            hasProperty("categoryName", is("Back-end"))))));
  }
}
